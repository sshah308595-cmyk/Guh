package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreetoSampleData
import com.example.model.Dish
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DiscountBlue
import com.example.ui.theme.Neutral100
import com.example.ui.theme.Neutral300
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral700
import com.example.ui.theme.Neutral900
import com.example.ui.theme.RatingGreen
import com.example.ui.theme.StreetoOrange
import com.example.ui.theme.StreetoRed
import com.example.ui.theme.StreetoWarmBg
import com.example.ui.theme.VegGreen
import com.example.viewmodel.StreetoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBottomSheet(
    uiState: StreetoUiState,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onApplyCoupon: (String) -> Unit,
    onRemoveCoupon: () -> Unit,
    onAddItem: (Dish) -> Unit,
    onRemoveItem: (Dish) -> Unit,
    onSelectPaymentMethod: (String) -> Unit,
    onSetDeliveryInstructions: (String) -> Unit,
    onChangeAddressClick: () -> Unit,
    onPlaceOrder: () -> Unit
) {
    var couponInput by remember { mutableStateOf("") }
    var showAllCoupons by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .background(Neutral300, CircleShape)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Cart & Checkout",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Text(
                        text = "${uiState.totalItemCount} items from ${uiState.selectedVendor?.name ?: "Street Food Vendor"}",
                        fontSize = 12.sp,
                        color = Neutral700
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Neutral700)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Address Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Neutral100),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(StreetoWarmBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = StreetoRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Deliver to:",
                            fontSize = 11.sp,
                            color = Neutral500,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = uiState.selectedAddress,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral900,
                            maxLines = 1
                        )
                    }
                    TextButton(onClick = onChangeAddressClick) {
                        Text(
                            text = "CHANGE",
                            color = StreetoRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cart Items List
            Text(
                text = "Order Items",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900
            )
            Spacer(modifier = Modifier.height(8.dp))

            uiState.cartItems.values.forEach { cartItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VegNonVegBadge(isVeg = cartItem.dish.isVeg)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cartItem.dish.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Neutral900
                        )
                        Text(
                            text = "₹${cartItem.dish.price} each",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }

                    AddItemButton(
                        count = cartItem.quantity,
                        onAdd = { onAddItem(cartItem.dish) },
                        onRemove = { onRemoveItem(cartItem.dish) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "₹${cartItem.dish.price * cartItem.quantity}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Neutral300.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Instructions
            Text(
                text = "Delivery Instructions",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Leave at door", "Avoid calling", "Don't ring bell").forEach { note ->
                    val isSelected = uiState.deliveryInstructions == note
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onSetDeliveryInstructions(if (isSelected) "" else note)
                            },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) StreetoWarmBg else Color(0xFFF7F7F7),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StreetoRed else CardBorder
                        )
                    ) {
                        Text(
                            text = note,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StreetoRed else Neutral700,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coupon Engine Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = StreetoRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Offers & Coupons",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                }

                TextButton(onClick = { showAllCoupons = !showAllCoupons }) {
                    Text(
                        text = if (showAllCoupons) "Hide Offers" else "View All (${StreetoSampleData.coupons.size})",
                        color = StreetoRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (showAllCoupons) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = StreetoRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Active Applied Coupon Card
            if (uiState.appliedCoupon != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F5E9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RatingGreen.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = RatingGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "'${uiState.appliedCoupon.code}' Applied",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RatingGreen
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = RatingGreen
                                        ) {
                                            Text(
                                                text = "SAVED ₹${uiState.couponDiscount}",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = uiState.appliedCoupon.title,
                                        fontSize = 11.sp,
                                        color = Neutral700
                                    )
                                }
                            }
                            TextButton(onClick = onRemoveCoupon) {
                                Text("REMOVE", color = StreetoRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Promo Code Input Box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = couponInput,
                        onValueChange = { couponInput = it.uppercase() },
                        placeholder = { Text("Enter promo code (e.g. STREET50)", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("coupon_input_field"),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Discount,
                                contentDescription = null,
                                tint = Neutral500,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            if (couponInput.isNotEmpty()) {
                                IconButton(onClick = { couponInput = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = Neutral500,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (couponInput.isNotBlank()) {
                                onApplyCoupon(couponInput)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("apply_coupon_button")
                    ) {
                        Text("Apply", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Dynamic Validation Feedback Banner
            if (uiState.couponMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = if (uiState.isCouponError) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (uiState.isCouponError) StreetoRed.copy(alpha = 0.4f) else RatingGreen.copy(alpha = 0.4f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (uiState.isCouponError) Icons.Default.ErrorOutline else Icons.Default.Celebration,
                            contentDescription = null,
                            tint = if (uiState.isCouponError) StreetoRed else RatingGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.couponMessage,
                            color = if (uiState.isCouponError) StreetoRed else Color(0xFF1B5E20),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Available Promo Codes with Dynamic Calculation
            if (showAllCoupons) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StreetoSampleData.coupons.forEach { cp ->
                        val currentItemTotal = uiState.itemTotal
                        val isEligible = currentItemTotal >= cp.minOrder
                        val isApplied = uiState.appliedCoupon?.code == cp.code
                        val potentialSavings = if (cp.discountPercent > 0) {
                            ((currentItemTotal * cp.discountPercent) / 100).coerceAtMost(cp.maxDiscount)
                        } else {
                            cp.flatDiscount
                        }
                        val gap = cp.minOrder - currentItemTotal

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .testTag("coupon_card_${cp.code}"),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isApplied) Color(0xFFF1F8E9) else Neutral100),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isApplied) RatingGreen else CardBorder
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Promo Tag
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isApplied) Color(0xFFE8F5E9) else StreetoWarmBg,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isApplied) RatingGreen else StreetoRed.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Text(
                                            text = "🏷️ ${cp.code}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isApplied) RatingGreen else StreetoRed,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    // Dynamic savings tag or Apply action
                                    if (isApplied) {
                                        Text(
                                            text = "APPLIED ✓",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = RatingGreen
                                        )
                                    } else if (isEligible) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFFE8F5E9)
                                            ) {
                                                Text(
                                                    text = "Save ₹$potentialSavings",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = RatingGreen,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Button(
                                                onClick = { onApplyCoupon(cp.code) },
                                                colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                                                shape = RoundedCornerShape(6.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                modifier = Modifier.height(28.dp)
                                            ) {
                                                Text("APPLY", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    } else {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFFFF3E0)
                                        ) {
                                            Text(
                                                text = "Add ₹$gap more",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = StreetoOrange,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = cp.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Neutral900
                                )
                                Text(
                                    text = cp.description,
                                    fontSize = 11.sp,
                                    color = Neutral500
                                )

                                // Progress to unlock if not eligible
                                if (!isEligible && currentItemTotal > 0) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    val progress = (currentItemTotal.toFloat() / cp.minOrder.toFloat()).coerceIn(0f, 1f)
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp)),
                                        color = StreetoOrange,
                                        trackColor = Neutral300.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Cart: ₹$currentItemTotal / ₹${cp.minOrder} min order",
                                        fontSize = 10.sp,
                                        color = Neutral500
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bill Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Bill Details",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    BillRow(label = "Item Total", value = "₹${uiState.itemTotal}")
                    BillRow(
                        label = "Delivery Partner Fee",
                        value = if (uiState.deliveryFee == 0) "FREE" else "₹${uiState.deliveryFee}",
                        isDiscount = uiState.deliveryFee == 0
                    )
                    BillRow(label = "Platform Fee", value = "₹${uiState.platformFee}")
                    BillRow(label = "Govt. Taxes & GST (5%)", value = "₹${uiState.gstTax}")

                    if (uiState.couponDiscount > 0) {
                        BillRow(
                            label = "Coupon Discount (${uiState.appliedCoupon?.code})",
                            value = "-₹${uiState.couponDiscount}",
                            isDiscount = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Neutral300)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "To Pay",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Neutral900
                        )
                        Text(
                            text = "₹${uiState.finalTotal}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StreetoRed
                        )
                    }

                    val totalSavings = uiState.couponDiscount + (if (uiState.deliveryFee == 0 && uiState.itemTotal < 199) 25 else 0)
                    if (totalSavings > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Celebration,
                                    contentDescription = null,
                                    tint = RatingGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Yay! You save ₹$totalSavings on this street order",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Option
            Text(
                text = "Payment Method",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // UPI
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectPaymentMethod("UPI") },
                    shape = RoundedCornerShape(10.dp),
                    color = if (uiState.paymentMethod == "UPI") StreetoWarmBg else Color(0xFFF7F7F7),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (uiState.paymentMethod == "UPI") StreetoRed else CardBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.paymentMethod == "UPI",
                            onClick = { onSelectPaymentMethod("UPI") },
                            colors = RadioButtonDefaults.colors(selectedColor = StreetoRed)
                        )
                        Column {
                            Text("UPI / GPay", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Instant refund", fontSize = 10.sp, color = RatingGreen)
                        }
                    }
                }

                // COD
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectPaymentMethod("COD") },
                    shape = RoundedCornerShape(10.dp),
                    color = if (uiState.paymentMethod == "COD") StreetoWarmBg else Color(0xFFF7F7F7),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (uiState.paymentMethod == "COD") StreetoRed else CardBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.paymentMethod == "COD",
                            onClick = { onSelectPaymentMethod("COD") },
                            colors = RadioButtonDefaults.colors(selectedColor = StreetoRed)
                        )
                        Column {
                            Text("Cash on Delivery", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Pay via cash", fontSize = 10.sp, color = Neutral500)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Place Order Button
            Button(
                onClick = onPlaceOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("pay_and_place_order_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StreetoRed)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (uiState.paymentMethod == "UPI") "Pay via UPI" else "Pay on Delivery",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "₹${uiState.finalTotal}  ➔",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun BillRow(label: String, value: String, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Neutral700)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (isDiscount) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isDiscount) RatingGreen else Neutral900
        )
    }
}
