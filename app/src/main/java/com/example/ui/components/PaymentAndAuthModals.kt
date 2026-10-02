package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AddressEntity
import com.example.data.local.PastOrderEntity
import com.example.data.local.UserEntity
import com.example.model.PaymentGatewayStatus
import com.example.model.Vendor
import com.example.ui.theme.CardBorder
import com.example.ui.theme.Neutral100
import com.example.ui.theme.Neutral300
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral700
import com.example.ui.theme.Neutral900
import com.example.ui.theme.RatingGreen
import com.example.ui.theme.StreetoOrange
import com.example.ui.theme.StreetoRed
import com.example.ui.theme.StreetoWarmBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockPaymentGatewayModal(
    paymentMethod: String, // "UPI" or "COD"
    amount: Int,
    paymentStatus: PaymentGatewayStatus,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onProceedPayment: (upiApp: String) -> Unit
) {
    var selectedUpiApp by remember { mutableStateOf("Google Pay") }
    var customUpiId by remember { mutableStateOf("streeto.user@okhdfcbank") }

    ModalBottomSheet(
        onDismissRequest = {
            if (paymentStatus != PaymentGatewayStatus.PROCESSING) {
                onDismiss()
            }
        },
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (paymentStatus) {
                PaymentGatewayStatus.PROCESSING -> {
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(
                        color = StreetoRed,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = if (paymentMethod == "UPI") "Connecting to UPI Gateway..." else "Confirming COD Order...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (paymentMethod == "UPI") "Processing secure transaction of ₹$amount via $selectedUpiApp"
                        else "Notifying restaurant kitchen for ₹$amount order",
                        fontSize = 13.sp,
                        color = Neutral700,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "🔒 256-Bit SSL Mock Gateway • Do not press back",
                        fontSize = 11.sp,
                        color = Neutral500
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                PaymentGatewayStatus.SUCCESS -> {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color(0xFFE8F5E9), CircleShape)
                            .border(2.dp, RatingGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = RatingGreen,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (paymentMethod == "UPI") "UPI Payment Successful! 🎉" else "COD Order Placed! 🎉",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RatingGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (paymentMethod == "UPI") "₹$amount paid via $selectedUpiApp\nRef ID: UPI-${(10000000..99999999).random()}"
                        else "₹$amount to be paid in cash upon delivery to the partner.",
                        fontSize = 13.sp,
                        color = Neutral700,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Redirecting to Live Order Tracking...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StreetoRed
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                PaymentGatewayStatus.IDLE, PaymentGatewayStatus.FAILED -> {
                    // Gateway Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(StreetoWarmBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (paymentMethod == "UPI") Icons.Default.PhoneAndroid else Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = StreetoRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (paymentMethod == "UPI") "UPI Payment Gateway" else "Cash On Delivery (COD)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Neutral900
                                )
                                Text(
                                    text = "Payable Amount: ₹$amount",
                                    fontSize = 12.sp,
                                    color = StreetoRed,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Neutral700)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Neutral300.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))

                    if (paymentMethod == "UPI") {
                        Text(
                            text = "Select UPI Application",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral700,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val upiOptions = listOf(
                            Triple("Google Pay", "Instant payment with UPI PIN", "🟢"),
                            Triple("PhonePe", "India's preferred payments app", "🟣"),
                            Triple("Paytm", "Paytm UPI / Wallet linked", "🔵")
                        )

                        upiOptions.forEach { (name, desc, badge) ->
                            val isSelected = selectedUpiApp == name
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { selectedUpiApp = name },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) StreetoWarmBg else Color(0xFFFAFAFA),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StreetoRed else CardBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedUpiApp = name },
                                        colors = RadioButtonDefaults.colors(selectedColor = StreetoRed)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = badge, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Neutral900)
                                        Text(text = desc, fontSize = 11.sp, color = Neutral500)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = customUpiId,
                            onValueChange = { customUpiId = it },
                            label = { Text("Or verify custom UPI VPA ID", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { onProceedPayment(selectedUpiApp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("pay_upi_confirm_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Authorize & Pay ₹$amount via $selectedUpiApp",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        // COD UI
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = StreetoWarmBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StreetoRed.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "💵 Cash on Delivery",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StreetoRed
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "You can pay ₹$amount directly to the delivery partner when they arrive at your location. Please keep exact cash change ready.",
                                    fontSize = 12.sp,
                                    color = Neutral700,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { onProceedPayment("COD") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("place_cod_order_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Confirm & Place COD Order (₹$amount)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthModalSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onLogin: (email: String, pass: String) -> Unit,
    onSignUp: (name: String, email: String, pass: String, phone: String) -> Unit,
    errorMessage: String?
) {
    var isLoginTab by remember { mutableStateOf(true) }

    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
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
                        text = if (isLoginTab) "Welcome Back!" else "Create Account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Text(
                        text = if (isLoginTab) "Log in to track orders & manage addresses" else "Sign up to start ordering street favorites",
                        fontSize = 12.sp,
                        color = Neutral500
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Neutral700)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Neutral100)
                    .padding(4.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isLoginTab = true },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isLoginTab) Color.White else Color.Transparent,
                    shadowElevation = if (isLoginTab) 2.dp else 0.dp
                ) {
                    Text(
                        text = "Log In",
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        fontWeight = if (isLoginTab) FontWeight.Bold else FontWeight.Medium,
                        color = if (isLoginTab) StreetoRed else Neutral700
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isLoginTab = false },
                    shape = RoundedCornerShape(8.dp),
                    color = if (!isLoginTab) Color.White else Color.Transparent,
                    shadowElevation = if (!isLoginTab) 2.dp else 0.dp
                ) {
                    Text(
                        text = "Sign Up",
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        fontWeight = if (!isLoginTab) FontWeight.Bold else FontWeight.Medium,
                        color = if (!isLoginTab) StreetoRed else Neutral700
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isLoginTab) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Full Name", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("Phone Number (+91)", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text("Email Address", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Password", fontSize = 12.sp) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = errorMessage,
                    color = StreetoRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isLoginTab) {
                        onLogin(emailInput, passwordInput)
                    } else {
                        onSignUp(nameInput, emailInput, passwordInput, phoneInput)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("auth_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (isLoginTab) "Log In to Streeto" else "Create Streeto Account",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Demo Login Button
            OutlinedButton(
                onClick = {
                    onLogin("rahul@streeto.in", "password123")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Instant Demo Login (Rahul Sharma)", fontSize = 12.sp, color = Neutral700)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileModalSheet(
    user: UserEntity?,
    addresses: List<AddressEntity>,
    orders: List<PastOrderEntity>,
    favoriteVendors: List<Vendor> = emptyList(),
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    onAddAddress: (title: String, address: String) -> Unit,
    onDeleteAddress: (id: String) -> Unit,
    onViewOrderHistory: () -> Unit = {},
    onToggleFavorite: (String) -> Unit = {},
    onSelectVendor: (Vendor) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Orders, 1: Favorites, 2: Addresses
    var showNewAddressForm by remember { mutableStateOf(false) }
    var newAddressTitle by remember { mutableStateOf("Home") }
    var newAddressText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {
            // Profile Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(StreetoWarmBg, CircleShape)
                            .border(1.5.dp, StreetoRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = user?.name ?: "Guest User",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral900
                        )
                        Text(
                            text = user?.email ?: "guest@streeto.in",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                        Text(
                            text = user?.phone ?: "+91 98765 43210",
                            fontSize = 11.sp,
                            color = Neutral700
                        )
                    }
                }

                TextButton(onClick = onLogout) {
                    Text("Logout", color = StreetoRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs: Orders / Favorites / Addresses
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = StreetoRed,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = StreetoRed
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Orders (${orders.size})", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Favorites (${favoriteVendors.size})", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Addresses (${addresses.size})", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Content
            if (selectedTab == 0) {
                // Orders History
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Orders", fontSize = 12.sp, color = Neutral500, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onViewOrderHistory) {
                        Text("View Full History ➔", color = StreetoRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📦", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("No orders placed yet", fontSize = 14.sp, color = Neutral700)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        items(orders, key = { it.id }) { pastOrder ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Neutral100),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = pastOrder.vendorName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Neutral900
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFE8F5E9)
                                        ) {
                                            Text(
                                                text = pastOrder.status,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = RatingGreen,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = pastOrder.itemsSummary,
                                        fontSize = 12.sp,
                                        color = Neutral700,
                                        maxLines = 2
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${pastOrder.orderDate} • ${pastOrder.paymentMethod}",
                                            fontSize = 11.sp,
                                            color = Neutral500
                                        )
                                        Text(
                                            text = "₹${pastOrder.totalAmount}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = StreetoRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (selectedTab == 1) {
                // Favorites Tab
                if (favoriteVendors.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("❤️", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No Favorite Vendors Yet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Neutral900
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap the heart icon on any street food stall on the Home screen to save it here for quick access!",
                                fontSize = 12.sp,
                                color = Neutral700,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(favoriteVendors, key = { it.id }) { vendor ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onSelectVendor(vendor) }
                                    .testTag("favorite_vendor_${vendor.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Neutral100),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Image(
                                            painter = painterResource(id = vendor.imageDrawableRes),
                                            contentDescription = vendor.name,
                                            modifier = Modifier
                                                .size(54.dp)
                                                .clip(RoundedCornerShape(10.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = vendor.name,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Neutral900,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${vendor.rating} ★ • ${vendor.deliveryTimeMin} mins • ${vendor.distanceKm}",
                                                fontSize = 11.sp,
                                                color = RatingGreen,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = vendor.cuisines.joinToString(", "),
                                                fontSize = 11.sp,
                                                color = Neutral500,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = { onToggleFavorite(vendor.id) },
                                        modifier = Modifier.testTag("unfavorite_btn_${vendor.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Remove Favorite",
                                            tint = StreetoRed,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Saved Addresses Tab
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Manage Delivery Locations", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { showNewAddressForm = !showNewAddressForm }) {
                            Text(
                                text = if (showNewAddressForm) "Cancel" else "+ Add New",
                                color = StreetoRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (showNewAddressForm) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = Neutral100,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Home", "Work", "Other").forEach { tag ->
                                        val isTag = newAddressTitle == tag
                                        Surface(
                                            modifier = Modifier.clickable { newAddressTitle = tag },
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isTag) StreetoRed else Color.White,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isTag) StreetoRed else CardBorder)
                                        ) {
                                            Text(
                                                text = tag,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isTag) Color.White else Neutral700,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = newAddressText,
                                    onValueChange = { newAddressText = it },
                                    placeholder = { Text("House / Street, Landmark...", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Button(
                                    onClick = {
                                        if (newAddressText.isNotBlank()) {
                                            onAddAddress(newAddressTitle, newAddressText)
                                            newAddressText = ""
                                            showNewAddressForm = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Save Address", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(addresses, key = { it.id }) { addressItem ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFAFAFA),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = when (addressItem.title.lowercase()) {
                                                "home" -> Icons.Default.Home
                                                "work" -> Icons.Default.Work
                                                else -> Icons.Default.LocationOn
                                            },
                                            contentDescription = null,
                                            tint = StreetoRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = addressItem.title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Neutral900
                                            )
                                            Text(
                                                text = addressItem.address,
                                                fontSize = 11.sp,
                                                color = Neutral700
                                            )
                                        }
                                    }
                                    IconButton(onClick = { onDeleteAddress(addressItem.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Neutral500,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
