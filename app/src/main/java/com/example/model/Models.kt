package com.example.model

data class FoodCategory(
    val id: String,
    val name: String,
    val emoji: String,
    val subtitle: String = ""
)

data class Vendor(
    val id: String,
    val name: String,
    val tagLine: String,
    val rating: Double,
    val reviewsCount: String,
    val deliveryTimeMin: String,
    val distanceKm: String,
    val offerTag: String,
    val imageDrawableRes: Int,
    val isPureVeg: Boolean,
    val address: String,
    val cuisines: List<String>,
    val priceForTwo: String
)

data class Dish(
    val id: String,
    val vendorId: String,
    val name: String,
    val price: Int,
    val description: String,
    val isVeg: Boolean,
    val imageDrawableRes: Int = 0,
    val rating: Double = 4.5,
    val ratingCount: Int = 120,
    val isBestseller: Boolean = false,
    val category: String = "Special"
)

data class CartItem(
    val dish: Dish,
    val quantity: Int
)

data class Coupon(
    val code: String,
    val title: String,
    val description: String,
    val discountPercent: Int = 0,
    val maxDiscount: Int = 100,
    val minOrder: Int = 0,
    val flatDiscount: Int = 0
)

enum class OrderState(val stepIndex: Int, val label: String, val subtitle: String) {
    CONFIRMED(0, "Order Confirmed", "Restaurant accepted your order"),
    PREPARING(1, "Preparing in Kitchen", "Chef is cooking authentic street food"),
    OUT_FOR_DELIVERY(2, "Out for Delivery", "Partner is on the way to you"),
    ARRIVED(3, "Arrived at Doorstep", "Enjoy your delicious street feast!")
}

data class Driver(
    val name: String,
    val phone: String,
    val rating: Double,
    val vehicle: String,
    val trips: String
)

data class Order(
    val id: String,
    val vendor: Vendor,
    val items: List<CartItem>,
    val itemTotal: Int,
    val deliveryFee: Int,
    val gstTax: Int,
    val platformFee: Int,
    val couponDiscount: Int,
    val finalTotal: Int,
    val status: OrderState,
    val paymentMethod: String,
    val deliveryAddress: String,
    val deliveryInstructions: String,
    val createdAt: String,
    val driver: Driver
)

data class SavedAddress(
    val id: String,
    val title: String, // "Home", "Work", "Other"
    val address: String,
    val isDefault: Boolean = false
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val addresses: List<SavedAddress> = emptyList()
)

enum class PaymentGatewayStatus {
    IDLE,
    PROCESSING,
    SUCCESS,
    FAILED
}

data class StreetoNotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val state: OrderState,
    val orderId: String
)

