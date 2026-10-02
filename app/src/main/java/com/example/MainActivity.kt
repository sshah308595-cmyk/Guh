package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddressPickerDialog
import com.example.ui.components.AuthModalSheet
import com.example.ui.components.CheckoutBottomSheet
import com.example.ui.components.MockPaymentGatewayModal
import com.example.ui.components.NotificationsModalSheet
import com.example.ui.components.OrderFeedbackModal
import com.example.ui.components.ProfileModalSheet
import com.example.ui.components.StickyCartBar
import com.example.ui.components.TopNotificationBanner
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderHistoryScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.VendorMenuScreen
import com.example.ui.theme.StreetoTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StreetoViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreetoTheme {
                StreetoApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreetoApp(
    viewModel: StreetoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val checkoutSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val paymentSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val authSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val profileSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val feedbackSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val notificationsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Permission response handled */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB)),
        contentAlignment = Alignment.Center
    ) {
        // Mobile-first responsive container (max 600.dp on tablets/desktop)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .background(Color.White)
        ) {
            when (uiState.currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }

                AppScreen.VENDOR_MENU -> {
                    val vendor = uiState.selectedVendor
                    if (vendor != null) {
                        VendorMenuScreen(
                            vendor = vendor,
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    } else {
                        HomeScreen(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }
                }

                AppScreen.TRACKING -> {
                    OrderTrackingScreen(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }

                AppScreen.ORDER_HISTORY -> {
                    OrderHistoryScreen(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }
            }

            // Sticky Cart Bar (Shown when items in cart and not on tracking screen)
            if (uiState.currentScreen != AppScreen.TRACKING && uiState.totalItemCount > 0) {
                StickyCartBar(
                    itemCount = uiState.totalItemCount,
                    totalAmount = uiState.itemTotal,
                    onViewCartClick = { viewModel.openCheckoutSheet() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            // Checkout Bottom Sheet
            if (uiState.showCheckoutSheet) {
                CheckoutBottomSheet(
                    uiState = uiState,
                    sheetState = checkoutSheetState,
                    onDismiss = {
                        coroutineScope.launch {
                            checkoutSheetState.hide()
                            viewModel.closeCheckoutSheet()
                        }
                    },
                    onApplyCoupon = { viewModel.applyCoupon(it) },
                    onRemoveCoupon = { viewModel.removeCoupon() },
                    onAddItem = { viewModel.addToCart(it) },
                    onRemoveItem = { viewModel.removeFromCart(it) },
                    onSelectPaymentMethod = { viewModel.setPaymentMethod(it) },
                    onSetDeliveryInstructions = { viewModel.setDeliveryInstructions(it) },
                    onChangeAddressClick = { viewModel.showAddressDialog(true) },
                    onPlaceOrder = {
                        coroutineScope.launch {
                            checkoutSheetState.hide()
                            viewModel.startPaymentFlow()
                        }
                    }
                )
            }

            // Mock Payment Gateway Modal
            if (uiState.showPaymentModal) {
                MockPaymentGatewayModal(
                    paymentMethod = uiState.paymentMethod,
                    amount = uiState.finalTotal,
                    paymentStatus = uiState.paymentStatus,
                    sheetState = paymentSheetState,
                    onDismiss = { viewModel.dismissPaymentModal() },
                    onProceedPayment = { chosenAppOrMethod ->
                        viewModel.executeMockPayment(chosenAppOrMethod)
                    }
                )
            }

            // Authentication Modal
            if (uiState.showAuthModal) {
                AuthModalSheet(
                    sheetState = authSheetState,
                    onDismiss = {
                        coroutineScope.launch {
                            authSheetState.hide()
                            viewModel.closeAuthModal()
                        }
                    },
                    onLogin = { email, pass ->
                        viewModel.logIn(email, pass)
                    },
                    onSignUp = { name, email, pass, phone ->
                        viewModel.signUp(name, email, pass, phone)
                    },
                    errorMessage = uiState.authErrorMessage
                )
            }

            // Profile & Order History Modal
            if (uiState.showProfileModal) {
                ProfileModalSheet(
                    user = uiState.currentUser,
                    addresses = uiState.savedAddresses,
                    orders = uiState.pastOrders,
                    favoriteVendors = viewModel.getFavoriteVendors(),
                    sheetState = profileSheetState,
                    onDismiss = {
                        coroutineScope.launch {
                            profileSheetState.hide()
                            viewModel.closeProfileModal()
                        }
                    },
                    onLogout = { viewModel.logOut() },
                    onAddAddress = { title, addr ->
                        viewModel.addSavedAddress(title, addr)
                    },
                    onDeleteAddress = { id ->
                        viewModel.deleteSavedAddress(id)
                    },
                    onViewOrderHistory = {
                        coroutineScope.launch {
                            profileSheetState.hide()
                            viewModel.openOrderHistoryScreen()
                        }
                    },
                    onToggleFavorite = { vendorId ->
                        viewModel.toggleFavorite(vendorId)
                    },
                    onSelectVendor = { vendor ->
                        coroutineScope.launch {
                            profileSheetState.hide()
                            viewModel.closeProfileModal()
                            viewModel.openVendorMenu(vendor)
                        }
                    }
                )
            }

            // Post-Order Feedback Modal
            if (uiState.showFeedbackModal && uiState.activeOrder != null) {
                OrderFeedbackModal(
                    order = uiState.activeOrder!!,
                    sheetState = feedbackSheetState,
                    onDismiss = {
                        coroutineScope.launch {
                            feedbackSheetState.hide()
                            viewModel.closeFeedbackModal()
                        }
                    },
                    onSubmitFeedback = { foodRating, deliveryRating, foodTags, deliveryTags, comment, tip ->
                        viewModel.submitOrderFeedback(foodRating, deliveryRating, foodTags, deliveryTags, comment, tip)
                    }
                )
            }

            // Address Selector Dialog
            if (uiState.showAddressDialog) {
                AddressPickerDialog(
                    currentAddress = uiState.selectedAddress,
                    onSelectAddress = { viewModel.selectAddress(it) },
                    onDismiss = { viewModel.showAddressDialog(false) }
                )
            }

            // Notifications Modal Sheet
            if (uiState.showNotificationsSheet) {
                NotificationsModalSheet(
                    notifications = uiState.inAppNotifications,
                    sheetState = notificationsSheetState,
                    onDismiss = {
                        coroutineScope.launch {
                            notificationsSheetState.hide()
                            viewModel.closeNotificationsSheet()
                        }
                    },
                    onClearAll = { viewModel.clearNotifications() },
                    onNotificationClick = { notif ->
                        coroutineScope.launch {
                            notificationsSheetState.hide()
                            viewModel.closeNotificationsSheet()
                            if (uiState.activeOrder != null) {
                                viewModel.navigateTo(AppScreen.TRACKING)
                            }
                        }
                    }
                )
            }

            // Top Floating Notification Banner
            uiState.latestNotificationBanner?.let { bannerItem ->
                TopNotificationBanner(
                    item = bannerItem,
                    onDismiss = { viewModel.dismissNotificationBanner() },
                    onClick = {
                        viewModel.dismissNotificationBanner()
                        if (uiState.activeOrder != null) {
                            viewModel.navigateTo(AppScreen.TRACKING)
                        }
                    }
                )
            }
        }
    }
}
