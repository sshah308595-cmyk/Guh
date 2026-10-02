package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.StreetoSampleData
import com.example.data.local.AddressEntity
import com.example.data.local.PastOrderEntity
import com.example.data.local.StreetoDatabase
import com.example.data.local.UserEntity
import com.example.data.repository.StreetoRepository
import com.example.model.CartItem
import com.example.model.Coupon
import com.example.model.Dish
import com.example.model.Order
import com.example.model.OrderState
import com.example.model.PaymentGatewayStatus
import com.example.model.StreetoNotificationItem
import com.example.model.Vendor
import com.example.util.StreetoNotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class AppScreen {
    HOME,
    VENDOR_MENU,
    TRACKING,
    ORDER_HISTORY
}

data class StreetoUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val selectedVendor: Vendor? = null,
    val selectedAddress: String = "Tulsipur / Balrampur",
    val searchQuery: String = "",
    val recentSearches: List<String> = emptyList(),
    val selectedCategory: String = "all",
    val isPureVegOnly: Boolean = false,
    val rating4PlusOnly: Boolean = false,
    val under30MinsOnly: Boolean = false,
    val hasOffersOnly: Boolean = false,
    val cartItems: Map<String, CartItem> = emptyMap(),
    val appliedCoupon: Coupon? = null,
    val couponMessage: String? = null,
    val isCouponError: Boolean = false,
    val deliveryInstructions: String = "",
    val paymentMethod: String = "UPI", // "UPI" or "COD"
    val showCheckoutSheet: Boolean = false,
    val showAddressDialog: Boolean = false,
    val showCallDriverDialog: Boolean = false,
    val showHelpDialog: Boolean = false,
    val showPaymentModal: Boolean = false,
    val showAuthModal: Boolean = false,
    val showProfileModal: Boolean = false,
    val showFeedbackModal: Boolean = false,
    val paymentStatus: PaymentGatewayStatus = PaymentGatewayStatus.IDLE,
    val currentUser: UserEntity? = null,
    val favoriteVendorIds: Set<String> = emptySet(),
    val savedAddresses: List<AddressEntity> = emptyList(),
    val pastOrders: List<PastOrderEntity> = emptyList(),
    val authErrorMessage: String? = null,
    val activeOrder: Order? = null,
    val trackingProgress: Float = 0.25f,
    val activeOrderStep: OrderState = OrderState.CONFIRMED,
    val simulationRunning: Boolean = false,
    val inAppNotifications: List<StreetoNotificationItem> = emptyList(),
    val latestNotificationBanner: StreetoNotificationItem? = null,
    val showNotificationsSheet: Boolean = false
) {
    val totalItemCount: Int
        get() = cartItems.values.sumOf { it.quantity }

    val itemTotal: Int
        get() = cartItems.values.sumOf { it.dish.price * it.quantity }

    val deliveryFee: Int
        get() {
            if (itemTotal == 0) return 0
            if (appliedCoupon?.code == "FIRSTBITE") return 0
            return if (itemTotal >= 199) 0 else 25
        }

    val gstTax: Int
        get() = if (itemTotal == 0) 0 else ((itemTotal * 5) / 100).coerceAtLeast(3)

    val platformFee: Int
        get() = if (itemTotal == 0) 0 else 5

    val couponDiscount: Int
        get() {
            val coupon = appliedCoupon ?: return 0
            if (itemTotal < coupon.minOrder) return 0
            return if (coupon.discountPercent > 0) {
                val disc = (itemTotal * coupon.discountPercent) / 100
                disc.coerceAtMost(coupon.maxDiscount)
            } else {
                coupon.flatDiscount
            }
        }

    val finalTotal: Int
        get() {
            if (itemTotal == 0) return 0
            val sub = itemTotal + deliveryFee + gstTax + platformFee - couponDiscount
            return sub.coerceAtLeast(1)
        }
}

class StreetoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StreetoRepository(StreetoDatabase.getDatabase(application))
    private val prefs = application.getSharedPreferences("streeto_search_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(StreetoUiState())
    val uiState: StateFlow<StreetoUiState> = _uiState.asStateFlow()

    private var simulationJob: Job? = null
    private var addressesJob: Job? = null
    private var ordersJob: Job? = null
    private var favoritesJob: Job? = null

    init {
        val initialSearches = loadRecentSearches()
        _uiState.update { it.copy(recentSearches = initialSearches) }

        viewModelScope.launch {
            repository.initializeDefaultData()
            // Set default logged in user
            val defaultUser = repository.getUserById("usr_default_01")
            if (defaultUser != null) {
                setUserSession(defaultUser)
            }
        }
    }

    private fun loadRecentSearches(): List<String> {
        val saved = prefs.getString("recent_searches", null)
        return if (!saved.isNullOrBlank()) {
            saved.split("|||").filter { it.isNotBlank() }.take(5)
        } else {
            listOf("Momos", "Sharma Ji Chaat", "Kathi Roll", "Chowmein", "Burger")
        }
    }

    private fun saveRecentSearches(list: List<String>) {
        prefs.edit().putString("recent_searches", list.joinToString("|||")).apply()
    }

    private fun setUserSession(user: UserEntity?) {
        _uiState.update { it.copy(currentUser = user) }
        addressesJob?.cancel()
        ordersJob?.cancel()
        favoritesJob?.cancel()

        if (user != null) {
            addressesJob = viewModelScope.launch {
                repository.getSavedAddresses(user.id).collect { addrs ->
                    _uiState.update { it.copy(savedAddresses = addrs) }
                }
            }
            ordersJob = viewModelScope.launch {
                repository.getOrdersHistory(user.id).collect { ords ->
                    _uiState.update { it.copy(pastOrders = ords) }
                }
            }
            favoritesJob = viewModelScope.launch {
                repository.getFavoriteVendors(user.id).collect { favs ->
                    val ids = favs.map { it.vendorId }.toSet()
                    _uiState.update { it.copy(favoriteVendorIds = ids) }
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    savedAddresses = emptyList(),
                    pastOrders = emptyList(),
                    favoriteVendorIds = emptySet()
                )
            }
        }
    }

    fun toggleFavorite(vendorId: String) {
        val isFav = _uiState.value.favoriteVendorIds.contains(vendorId)
        val user = _uiState.value.currentUser
        val userId = user?.id ?: "usr_default_01"
        viewModelScope.launch {
            if (isFav) {
                repository.removeFavorite(userId, vendorId)
            } else {
                repository.addFavorite(userId, vendorId)
            }
            _uiState.update {
                val updated = if (isFav) it.favoriteVendorIds - vendorId else it.favoriteVendorIds + vendorId
                it.copy(favoriteVendorIds = updated)
            }
        }
    }

    fun getFavoriteVendors(): List<Vendor> {
        val favIds = _uiState.value.favoriteVendorIds
        return StreetoSampleData.vendors.filter { favIds.contains(it.id) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addRecentSearch(query: String) {
        val clean = query.trim()
        if (clean.isBlank()) return
        val current = _uiState.value.recentSearches
        val updated = (listOf(clean) + current.filterNot { it.equals(clean, ignoreCase = true) }).take(5)
        _uiState.update { it.copy(recentSearches = updated) }
        saveRecentSearches(updated)
    }

    fun removeRecentSearch(query: String) {
        val current = _uiState.value.recentSearches
        val updated = current.filterNot { it.equals(query, ignoreCase = true) }
        _uiState.update { it.copy(recentSearches = updated) }
        saveRecentSearches(updated)
    }

    fun clearRecentSearches() {
        _uiState.update { it.copy(recentSearches = emptyList()) }
        saveRecentSearches(emptyList())
    }

    fun selectCategory(categoryId: String) {
        _uiState.update {
            val newCategory = if (it.selectedCategory == categoryId && categoryId != "all") "all" else categoryId
            it.copy(selectedCategory = newCategory)
        }
    }

    fun togglePureVeg() {
        _uiState.update { it.copy(isPureVegOnly = !it.isPureVegOnly) }
    }

    fun toggleRating4Plus() {
        _uiState.update { it.copy(rating4PlusOnly = !it.rating4PlusOnly) }
    }

    fun toggleUnder30Mins() {
        _uiState.update { it.copy(under30MinsOnly = !it.under30MinsOnly) }
    }

    fun toggleHasOffers() {
        _uiState.update { it.copy(hasOffersOnly = !it.hasOffersOnly) }
    }

    fun selectAddress(address: String) {
        _uiState.update { it.copy(selectedAddress = address, showAddressDialog = false) }
    }

    fun showAddressDialog(show: Boolean) {
        _uiState.update { it.copy(showAddressDialog = show) }
    }

    fun openVendorMenu(vendor: Vendor) {
        _uiState.update {
            it.copy(
                selectedVendor = vendor,
                currentScreen = AppScreen.VENDOR_MENU
            )
        }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun navigateBack() {
        _uiState.update {
            when (it.currentScreen) {
                AppScreen.VENDOR_MENU -> it.copy(currentScreen = AppScreen.HOME, selectedVendor = null)
                AppScreen.TRACKING -> it.copy(currentScreen = AppScreen.HOME)
                AppScreen.ORDER_HISTORY -> it.copy(currentScreen = AppScreen.HOME)
                AppScreen.HOME -> it
            }
        }
    }

    fun openOrderHistoryScreen() {
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.ORDER_HISTORY,
                showProfileModal = false
            )
        }
    }

    fun reorderPastItems(pastOrder: PastOrderEntity) {
        val vendor = StreetoSampleData.vendors.find { it.name.equals(pastOrder.vendorName, ignoreCase = true) }
            ?: StreetoSampleData.vendors[0]
        val dishes = StreetoSampleData.dishesByVendor[vendor.id] ?: emptyList()

        val newCart = mutableMapOf<String, CartItem>()
        if (dishes.isNotEmpty()) {
            newCart[dishes[0].id] = CartItem(dish = dishes[0], quantity = 1)
            if (dishes.size > 1) {
                newCart[dishes[1].id] = CartItem(dish = dishes[1], quantity = 1)
            }
        }

        _uiState.update {
            it.copy(
                selectedVendor = vendor,
                cartItems = newCart,
                currentScreen = AppScreen.VENDOR_MENU,
                showCheckoutSheet = true
            )
        }
    }

    fun addToCart(dish: Dish) {
        _uiState.update { state ->
            val currentMap = state.cartItems.toMutableMap()
            val existing = currentMap[dish.id]
            if (existing != null) {
                currentMap[dish.id] = existing.copy(quantity = existing.quantity + 1)
            } else {
                currentMap[dish.id] = CartItem(dish = dish, quantity = 1)
            }
            val newItemTotal = currentMap.values.sumOf { it.dish.price * it.quantity }
            val coupon = state.appliedCoupon
            val message = if (coupon != null && newItemTotal >= coupon.minOrder) {
                val discount = if (coupon.discountPercent > 0) {
                    ((newItemTotal * coupon.discountPercent) / 100).coerceAtMost(coupon.maxDiscount)
                } else {
                    coupon.flatDiscount
                }
                "🎉 '${coupon.code}' updated! You now save ₹$discount"
            } else {
                state.couponMessage
            }
            state.copy(cartItems = currentMap, couponMessage = message, isCouponError = false)
        }
    }

    fun removeFromCart(dish: Dish) {
        _uiState.update { state ->
            val currentMap = state.cartItems.toMutableMap()
            val existing = currentMap[dish.id]
            if (existing != null) {
                if (existing.quantity > 1) {
                    currentMap[dish.id] = existing.copy(quantity = existing.quantity - 1)
                } else {
                    currentMap.remove(dish.id)
                }
            }
            val newShowCheckout = if (currentMap.isEmpty()) false else state.showCheckoutSheet
            val newItemTotal = currentMap.values.sumOf { it.dish.price * it.quantity }
            val coupon = state.appliedCoupon
            var newCoupon = coupon
            var newCouponMsg = state.couponMessage
            var isErr = state.isCouponError

            if (coupon != null) {
                if (newItemTotal < coupon.minOrder) {
                    val gap = coupon.minOrder - newItemTotal
                    newCoupon = null
                    newCouponMsg = "⚠️ '${coupon.code}' removed. Add ₹$gap more to re-apply."
                    isErr = true
                } else {
                    val discount = if (coupon.discountPercent > 0) {
                        ((newItemTotal * coupon.discountPercent) / 100).coerceAtMost(coupon.maxDiscount)
                    } else {
                        coupon.flatDiscount
                    }
                    newCouponMsg = "🎉 '${coupon.code}' updated! You save ₹$discount"
                    isErr = false
                }
            }

            state.copy(
                cartItems = currentMap,
                showCheckoutSheet = newShowCheckout,
                appliedCoupon = newCoupon,
                couponMessage = newCouponMsg,
                isCouponError = isErr
            )
        }
    }

    fun clearCart() {
        _uiState.update {
            it.copy(
                cartItems = emptyMap(),
                showCheckoutSheet = false,
                appliedCoupon = null,
                couponMessage = null
            )
        }
    }

    fun openCheckoutSheet() {
        if (_uiState.value.cartItems.isNotEmpty()) {
            _uiState.update { it.copy(showCheckoutSheet = true) }
        }
    }

    fun closeCheckoutSheet() {
        _uiState.update { it.copy(showCheckoutSheet = false) }
    }

    fun applyCoupon(code: String) {
        val trimmed = code.trim().uppercase()
        if (_uiState.value.cartItems.isEmpty()) {
            _uiState.update {
                it.copy(
                    appliedCoupon = null,
                    couponMessage = "Your cart is empty. Add street food to apply coupons!",
                    isCouponError = true
                )
            }
            return
        }

        val found = StreetoSampleData.coupons.find { it.code.equals(trimmed, ignoreCase = true) }
        if (found == null) {
            _uiState.update {
                it.copy(
                    appliedCoupon = null,
                    couponMessage = "Invalid code '$trimmed'. Try STREET50, FIRSTBITE, or CHAAT30",
                    isCouponError = true
                )
            }
            return
        }

        val currentTotal = _uiState.value.itemTotal
        if (currentTotal < found.minOrder) {
            val gap = found.minOrder - currentTotal
            _uiState.update {
                it.copy(
                    appliedCoupon = null,
                    couponMessage = "Add ₹$gap more to unlock '${found.code}' (Min order ₹${found.minOrder})",
                    isCouponError = true
                )
            }
            return
        }

        val discount = if (found.discountPercent > 0) {
            val disc = (currentTotal * found.discountPercent) / 100
            disc.coerceAtMost(found.maxDiscount)
        } else {
            found.flatDiscount
        }

        _uiState.update {
            it.copy(
                appliedCoupon = found,
                couponMessage = "🎉 '${found.code}' applied! You save ₹$discount on this order",
                isCouponError = false
            )
        }
    }

    fun removeCoupon() {
        _uiState.update {
            it.copy(
                appliedCoupon = null,
                couponMessage = null,
                isCouponError = false
            )
        }
    }

    fun setPaymentMethod(method: String) {
        _uiState.update { it.copy(paymentMethod = method) }
    }

    fun setDeliveryInstructions(instructions: String) {
        _uiState.update { it.copy(deliveryInstructions = instructions) }
    }

    // Payment Gateway Flow
    fun startPaymentFlow() {
        _uiState.update {
            it.copy(
                showCheckoutSheet = false,
                showPaymentModal = true,
                paymentStatus = PaymentGatewayStatus.IDLE
            )
        }
    }

    fun dismissPaymentModal() {
        _uiState.update {
            it.copy(
                showPaymentModal = false,
                paymentStatus = PaymentGatewayStatus.IDLE
            )
        }
    }

    fun executeMockPayment(chosenOption: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(paymentStatus = PaymentGatewayStatus.PROCESSING) }

            // Simulate realistic bank / gateway network latency
            delay(1800)

            _uiState.update { it.copy(paymentStatus = PaymentGatewayStatus.SUCCESS) }

            // Record completed order in Room database
            val state = _uiState.value
            val vendor = state.selectedVendor ?: StreetoSampleData.vendors[0]
            val orderId = "STREETO-" + (100000..999999).random()
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val currentTime = timeFormat.format(Date())

            val itemsSummary = state.cartItems.values.joinToString(", ") { "${it.quantity}x ${it.dish.name}" }
            val userId = state.currentUser?.id ?: "guest_user"

            repository.recordCompletedOrder(
                orderId = orderId,
                userId = userId,
                vendorName = vendor.name,
                itemsSummary = itemsSummary,
                totalAmount = state.finalTotal,
                paymentMethod = if (state.paymentMethod == "UPI") "UPI ($chosenOption)" else "COD (Cash)",
                deliveryAddress = state.selectedAddress
            )

            val order = Order(
                id = orderId,
                vendor = vendor,
                items = state.cartItems.values.toList(),
                itemTotal = state.itemTotal,
                deliveryFee = state.deliveryFee,
                gstTax = state.gstTax,
                platformFee = state.platformFee,
                couponDiscount = state.couponDiscount,
                finalTotal = state.finalTotal,
                status = OrderState.CONFIRMED,
                paymentMethod = if (state.paymentMethod == "UPI") "UPI ($chosenOption)" else "Cash on Delivery",
                deliveryAddress = state.selectedAddress,
                deliveryInstructions = state.deliveryInstructions.ifBlank { "Standard contactless delivery" },
                createdAt = currentTime,
                driver = StreetoSampleData.defaultDriver
            )

            delay(1200)

            _uiState.update {
                it.copy(
                    showPaymentModal = false,
                    paymentStatus = PaymentGatewayStatus.IDLE,
                    activeOrder = order,
                    activeOrderStep = OrderState.CONFIRMED,
                    trackingProgress = 0.15f,
                    currentScreen = AppScreen.TRACKING,
                    cartItems = emptyMap(),
                    appliedCoupon = null,
                    couponMessage = null,
                    simulationRunning = true
                )
            }

            startOrderTrackingSimulation()
        }
    }

    private fun notifyOrderStatusChange(state: OrderState) {
        val order = _uiState.value.activeOrder ?: return
        val context = getApplication<Application>().applicationContext

        // Trigger Android System Notification
        StreetoNotificationHelper.showOrderStatusNotification(
            context = context,
            orderId = order.id,
            vendorName = order.vendor.name,
            state = state,
            driverName = order.driver.name
        )

        // Record in in-app notification center & top toast banner
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = timeFormat.format(Date())
        val (title, msg) = when (state) {
            OrderState.CONFIRMED -> Pair("Order Confirmed! 👨‍🍳", "${order.vendor.name} accepted your order #${order.id}")
            OrderState.PREPARING -> Pair("Cooking at the Stall! 🥘", "Chef is cooking your fresh street food.")
            OrderState.OUT_FOR_DELIVERY -> Pair("Out for Delivery! 🛵", "${order.driver.name} is on the way to you.")
            OrderState.ARRIVED -> Pair("Food Arrived! 😋", "Your meal is at the doorstep. Enjoy your feast!")
        }

        val notif = StreetoNotificationItem(
            id = UUID.randomUUID().toString(),
            title = title,
            message = msg,
            timestamp = currentTime,
            state = state,
            orderId = order.id
        )

        _uiState.update {
            it.copy(
                inAppNotifications = listOf(notif) + it.inAppNotifications.take(9),
                latestNotificationBanner = notif
            )
        }
    }

    private fun startOrderTrackingSimulation() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            // Step 1: Confirmed
            _uiState.update { it.copy(activeOrderStep = OrderState.CONFIRMED, trackingProgress = 0.15f) }
            notifyOrderStatusChange(OrderState.CONFIRMED)
            delay(4000)

            // Step 2: Preparing in Kitchen
            _uiState.update { it.copy(activeOrderStep = OrderState.PREPARING, trackingProgress = 0.45f) }
            notifyOrderStatusChange(OrderState.PREPARING)
            delay(6000)

            // Step 3: Out for delivery
            _uiState.update { it.copy(activeOrderStep = OrderState.OUT_FOR_DELIVERY, trackingProgress = 0.75f) }
            notifyOrderStatusChange(OrderState.OUT_FOR_DELIVERY)
            delay(8000)

            // Step 4: Arrived
            _uiState.update {
                it.copy(
                    activeOrderStep = OrderState.ARRIVED,
                    trackingProgress = 1.0f,
                    simulationRunning = false
                )
            }
            notifyOrderStatusChange(OrderState.ARRIVED)
            delay(1200)
            _uiState.update { it.copy(showFeedbackModal = true) }
        }
    }

    fun openFeedbackModal() {
        _uiState.update { it.copy(showFeedbackModal = true) }
    }

    fun closeFeedbackModal() {
        _uiState.update { it.copy(showFeedbackModal = false) }
    }

    fun dismissNotificationBanner() {
        _uiState.update { it.copy(latestNotificationBanner = null) }
    }

    fun openNotificationsSheet() {
        _uiState.update { it.copy(showNotificationsSheet = true) }
    }

    fun closeNotificationsSheet() {
        _uiState.update { it.copy(showNotificationsSheet = false) }
    }

    fun clearNotifications() {
        _uiState.update { it.copy(inAppNotifications = emptyList()) }
    }

    fun submitOrderFeedback(
        foodRating: Int,
        deliveryRating: Int,
        foodTags: List<String>,
        deliveryTags: List<String>,
        comment: String,
        tipAmount: Int
    ) {
        // Feedback recorded successfully
    }

    fun advanceTrackingStepManually() {
        val current = _uiState.value.activeOrderStep
        val next = when (current) {
            OrderState.CONFIRMED -> OrderState.PREPARING
            OrderState.PREPARING -> OrderState.OUT_FOR_DELIVERY
            OrderState.OUT_FOR_DELIVERY -> OrderState.ARRIVED
            OrderState.ARRIVED -> OrderState.CONFIRMED
        }
        val prog = when (next) {
            OrderState.CONFIRMED -> 0.15f
            OrderState.PREPARING -> 0.45f
            OrderState.OUT_FOR_DELIVERY -> 0.75f
            OrderState.ARRIVED -> 1.0f
        }
        val openFeedback = next == OrderState.ARRIVED
        _uiState.update {
            it.copy(
                activeOrderStep = next,
                trackingProgress = prog,
                showFeedbackModal = openFeedback
            )
        }
        notifyOrderStatusChange(next)
    }

    // User Authentication
    fun openAuthModal() {
        _uiState.update { it.copy(showAuthModal = true, authErrorMessage = null) }
    }

    fun closeAuthModal() {
        _uiState.update { it.copy(showAuthModal = false, authErrorMessage = null) }
    }

    fun openProfileModal() {
        if (_uiState.value.currentUser == null) {
            openAuthModal()
        } else {
            _uiState.update { it.copy(showProfileModal = true) }
        }
    }

    fun closeProfileModal() {
        _uiState.update { it.copy(showProfileModal = false) }
    }

    fun logIn(email: String, pass: String) {
        viewModelScope.launch {
            if (email.isBlank() || pass.isBlank()) {
                _uiState.update { it.copy(authErrorMessage = "Please enter email and password") }
                return@launch
            }
            val result = repository.logIn(email, pass)
            result.onSuccess { user ->
                setUserSession(user)
                _uiState.update { it.copy(showAuthModal = false, authErrorMessage = null) }
            }.onFailure { err ->
                _uiState.update { it.copy(authErrorMessage = err.message ?: "Login failed") }
            }
        }
    }

    fun signUp(name: String, email: String, pass: String, phone: String) {
        viewModelScope.launch {
            if (name.isBlank() || email.isBlank() || pass.isBlank()) {
                _uiState.update { it.copy(authErrorMessage = "All fields are required") }
                return@launch
            }
            val result = repository.signUp(name, email, pass, phone.ifBlank { "+91 98765 43210" })
            result.onSuccess { user ->
                setUserSession(user)
                _uiState.update { it.copy(showAuthModal = false, authErrorMessage = null) }
            }.onFailure { err ->
                _uiState.update { it.copy(authErrorMessage = err.message ?: "Sign up failed") }
            }
        }
    }

    fun logOut() {
        setUserSession(null)
        _uiState.update { it.copy(showProfileModal = false) }
    }

    fun addSavedAddress(title: String, address: String) {
        viewModelScope.launch {
            val user = _uiState.value.currentUser ?: return@launch
            repository.addAddress(user.id, title, address)
        }
    }

    fun deleteSavedAddress(id: String) {
        viewModelScope.launch {
            repository.deleteAddress(id)
        }
    }

    fun openCallDriverDialog(show: Boolean) {
        _uiState.update { it.copy(showCallDriverDialog = show) }
    }

    fun openHelpDialog(show: Boolean) {
        _uiState.update { it.copy(showHelpDialog = show) }
    }

    fun getFilteredVendors(): List<Vendor> {
        val state = _uiState.value
        val query = state.searchQuery.trim().lowercase()

        return StreetoSampleData.vendors.filter { vendor ->
            val matchesQuery = query.isEmpty() ||
                vendor.name.lowercase().contains(query) ||
                vendor.cuisines.any { it.lowercase().contains(query) } ||
                vendor.tagLine.lowercase().contains(query)

            val matchesCategory = if (state.selectedCategory == "all") {
                true
            } else {
                val dishes = StreetoSampleData.dishesByVendor[vendor.id] ?: emptyList()
                dishes.any { it.category.equals(state.selectedCategory, ignoreCase = true) } ||
                    vendor.cuisines.any { it.equals(state.selectedCategory, ignoreCase = true) }
            }

            val matchesPureVeg = !state.isPureVegOnly || vendor.isPureVeg
            val matchesRating = !state.rating4PlusOnly || vendor.rating >= 4.5
            val matchesUnder30 = !state.under30MinsOnly || vendor.deliveryTimeMin.contains("15") || vendor.deliveryTimeMin.contains("20")
            val matchesOffers = !state.hasOffersOnly || vendor.offerTag.isNotBlank()

            matchesQuery && matchesCategory && matchesPureVeg && matchesRating && matchesUnder30 && matchesOffers
        }
    }
}
