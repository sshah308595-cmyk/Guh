package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderState
import com.example.ui.components.CallDriverDialog
import com.example.ui.components.HelpSupportDialog
import com.example.ui.theme.CardBorder
import com.example.ui.theme.Neutral100
import com.example.ui.theme.Neutral300
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral700
import com.example.ui.theme.Neutral900
import com.example.ui.theme.RatingGreen
import com.example.ui.theme.StreetoRed
import com.example.ui.theme.StreetoWarmBg
import com.example.ui.theme.VegGreen
import com.example.viewmodel.StreetoUiState
import com.example.viewmodel.StreetoViewModel

@Composable
fun OrderTrackingScreen(
    uiState: StreetoUiState,
    viewModel: StreetoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val order = uiState.activeOrder
    val currentStep = uiState.activeOrderStep

    val animatedProgress by animateFloatAsState(
        targetValue = uiState.trackingProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progressAnim"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    if (uiState.showCallDriverDialog && order != null) {
        CallDriverDialog(
            driver = order.driver,
            onDismiss = { viewModel.openCallDriverDialog(false) }
        )
    }

    if (uiState.showHelpDialog && order != null) {
        HelpSupportDialog(
            orderId = order.id,
            onDismiss = { viewModel.openHelpDialog(false) }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB)),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Top App Bar
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateBack() },
                            modifier = Modifier.testTag("tracking_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Neutral900
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Live Order Tracking",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Neutral900
                            )
                            Text(
                                text = "ID: ${order?.id ?: "STREETO-29381"}",
                                fontSize = 11.sp,
                                color = Neutral500
                            )
                        }
                    }

                    // Help button
                    OutlinedButton(
                        onClick = { viewModel.openHelpDialog(true) },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("help_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = StreetoRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Help",
                            color = StreetoRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // ETA Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(RatingGreen.copy(alpha = pulseAlpha), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = when (currentStep) {
                                        OrderState.CONFIRMED -> "ACCEPTED BY VENDOR"
                                        OrderState.PREPARING -> "COOKING IN PROGRESS"
                                        OrderState.OUT_FOR_DELIVERY -> "RIDER ON THE WAY"
                                        OrderState.ARRIVED -> "ORDER DELIVERED"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RatingGreen,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = when (currentStep) {
                                    OrderState.CONFIRMED -> "Arriving in ~25 mins"
                                    OrderState.PREPARING -> "Arriving in ~18 mins"
                                    OrderState.OUT_FOR_DELIVERY -> "Arriving in 6 mins"
                                    OrderState.ARRIVED -> "Arrived at your location!"
                                },
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Neutral900
                            )
                        }

                        Text(
                            text = when (currentStep) {
                                OrderState.CONFIRMED -> "📋"
                                OrderState.PREPARING -> "🍳"
                                OrderState.OUT_FOR_DELIVERY -> "🛵"
                                OrderState.ARRIVED -> "🎉"
                            },
                            fontSize = 36.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step Tracker Status Bar
                    StepProgressBar(activeStep = currentStep)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Simulation Advance Button (to test interactive tracking flow)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Auto-simulating live stages",
                            fontSize = 11.sp,
                            color = Neutral500
                        )
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.advanceTrackingStepManually() }
                                .testTag("simulate_next_step_button"),
                            color = StreetoWarmBg,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StreetoRed.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = StreetoRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Simulate Next Stage ➔",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StreetoRed
                                )
                            }
                        }
                    }
                }
            }
        }

        // Rating CTA when order has arrived
        if (currentStep == OrderState.ARRIVED) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { viewModel.openFeedbackModal() }
                        .testTag("rate_order_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StreetoWarmBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StreetoRed.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color.White, CircleShape)
                                    .border(1.dp, StreetoRed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("⭐", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Rate Food & Delivery",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = StreetoRed
                                )
                                Text(
                                    text = "Share feedback & tip ${order?.driver?.name ?: "rider"}",
                                    fontSize = 11.sp,
                                    color = Neutral700
                                )
                            }
                        }
                        Text(
                            text = "Rate Now ➔",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StreetoRed
                        )
                    }
                }
            }
        }

        // Mock Map Canvas with Animated Moving Delivery Partner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                            .background(Color(0xFFE9EEF4))
                            .testTag("map_view_container")
                    ) {
                        // Drawing Stylized Map with Roads & Animated Moving Partner
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Background blocks (parks, blocks)
                            drawRect(
                                color = Color(0xFFD4E7D3),
                                topLeft = Offset(w * 0.05f, h * 0.1f),
                                size = androidx.compose.ui.geometry.Size(w * 0.28f, h * 0.35f)
                            )
                            drawRect(
                                color = Color(0xFFE2E7EE),
                                topLeft = Offset(w * 0.65f, h * 0.5f),
                                size = androidx.compose.ui.geometry.Size(w * 0.3f, h * 0.4f)
                            )

                            // Main Roads
                            val roadPaint = Color.White
                            // Horizontal Road
                            drawLine(
                                color = roadPaint,
                                start = Offset(0f, h * 0.45f),
                                end = Offset(w, h * 0.45f),
                                strokeWidth = 28f,
                                cap = StrokeCap.Round
                            )
                            // Secondary Cross Roads
                            drawLine(
                                color = roadPaint,
                                start = Offset(w * 0.38f, 0f),
                                end = Offset(w * 0.38f, h),
                                strokeWidth = 22f,
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = roadPaint,
                                start = Offset(w * 0.8f, 0f),
                                end = Offset(w * 0.8f, h),
                                strokeWidth = 20f,
                                cap = StrokeCap.Round
                            )

                            // Route Path from Vendor (top left) to Customer (bottom right)
                            val routeStart = Offset(w * 0.15f, h * 0.3f)
                            val wayPoint1 = Offset(w * 0.38f, h * 0.3f)
                            val wayPoint2 = Offset(w * 0.38f, h * 0.72f)
                            val routeEnd = Offset(w * 0.85f, h * 0.72f)

                            val fullPath = Path().apply {
                                moveTo(routeStart.x, routeStart.y)
                                lineTo(wayPoint1.x, wayPoint1.y)
                                lineTo(wayPoint2.x, wayPoint2.y)
                                lineTo(routeEnd.x, routeEnd.y)
                            }

                            // Route line background
                            drawPath(
                                path = fullPath,
                                color = StreetoRed.copy(alpha = 0.3f),
                                style = Stroke(
                                    width = 12f,
                                    cap = StrokeCap.Round,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                                )
                            )

                            // Active traveled route line
                            drawPath(
                                path = fullPath,
                                color = StreetoRed,
                                style = Stroke(width = 8f, cap = StrokeCap.Round)
                            )

                            // Draw Vendor Pin
                            drawCircle(
                                color = Color.White,
                                radius = 22f,
                                center = routeStart
                            )
                            drawCircle(
                                color = Color(0xFFE23744),
                                radius = 16f,
                                center = routeStart
                            )

                            // Draw Destination Pin (Home)
                            drawCircle(
                                color = Color.White,
                                radius = 24f,
                                center = routeEnd
                            )
                            drawCircle(
                                color = RatingGreen,
                                radius = 18f,
                                center = routeEnd
                            )

                            // Interpolate Partner Position based on animatedProgress (0.0 to 1.0)
                            val partnerPos = when {
                                animatedProgress <= 0.33f -> {
                                    val t = animatedProgress / 0.33f
                                    Offset(
                                        routeStart.x + (wayPoint1.x - routeStart.x) * t,
                                        routeStart.y + (wayPoint1.y - routeStart.y) * t
                                    )
                                }
                                animatedProgress <= 0.66f -> {
                                    val t = (animatedProgress - 0.33f) / 0.33f
                                    Offset(
                                        wayPoint1.x + (wayPoint2.x - wayPoint1.x) * t,
                                        wayPoint1.y + (wayPoint2.y - wayPoint1.y) * t
                                    )
                                }
                                else -> {
                                    val t = ((animatedProgress - 0.66f) / 0.34f).coerceAtMost(1f)
                                    Offset(
                                        wayPoint2.x + (routeEnd.x - wayPoint2.x) * t,
                                        wayPoint2.y + (routeEnd.y - wayPoint2.y) * t
                                    )
                                }
                            }

                            // Glowing circle around partner
                            drawCircle(
                                color = StreetoRed.copy(alpha = 0.3f),
                                radius = 32f,
                                center = partnerPos
                            )
                            // Scooter badge
                            drawCircle(
                                color = Color.White,
                                radius = 22f,
                                center = partnerPos
                            )
                            drawCircle(
                                color = StreetoRed,
                                radius = 16f,
                                center = partnerPos
                            )
                        }

                        // Map labels overlay
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.95f),
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🏪 ${order?.vendor?.name ?: "Vendor"}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.95f),
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📍 Your Location", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RatingGreen)
                            }
                        }
                    }

                    // Route status bar under map
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.NearMe, contentDescription = null, tint = StreetoRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (currentStep) {
                                    OrderState.CONFIRMED -> "Vendor is preparing packaging"
                                    OrderState.PREPARING -> "Chef cooking items fresh"
                                    OrderState.OUT_FOR_DELIVERY -> "Rider near Balrampur Chowk (~1.2 km)"
                                    OrderState.ARRIVED -> "Delivered at doorstep!"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Neutral700
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Delivery Partner Card
        item {
            val driver = order?.driver ?: com.example.data.StreetoSampleData.defaultDriver
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(StreetoWarmBg, CircleShape)
                                .border(1.5.dp, StreetoRed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🛵", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = driver.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Neutral900
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Row(
                                    modifier = Modifier
                                        .background(RatingGreen, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${driver.rating}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(8.dp)
                                    )
                                }
                            }
                            Text(
                                text = driver.vehicle,
                                fontSize = 12.sp,
                                color = Neutral700
                            )
                            Text(
                                text = "Vaccinated • Contactless",
                                fontSize = 11.sp,
                                color = RatingGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Call Driver Button
                    IconButton(
                        onClick = { viewModel.openCallDriverDialog(true) },
                        modifier = Modifier
                            .size(44.dp)
                            .background(StreetoWarmBg, CircleShape)
                            .border(1.dp, StreetoRed, CircleShape)
                            .testTag("call_driver_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Driver",
                            tint = StreetoRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Order Summary Details
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Details",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    order?.items?.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.dish.name}",
                                fontSize = 13.sp,
                                color = Neutral900
                            )
                            Text(
                                text = "₹${item.dish.price * item.quantity}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Neutral900
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Neutral300.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Paid via ${order?.paymentMethod ?: "UPI"}", fontSize = 12.sp, color = Neutral500)
                        Text(
                            text = "Total: ₹${order?.finalTotal ?: 0}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StreetoRed
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = StreetoRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = order?.deliveryAddress ?: uiState.selectedAddress,
                            fontSize = 11.sp,
                            color = Neutral700
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepProgressBar(activeStep: OrderState) {
    val steps = OrderState.values()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, step ->
                val isCompleted = step.stepIndex <= activeStep.stepIndex
                val isCurrent = step == activeStep

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(72.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = if (isCompleted) StreetoRed else Neutral300,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                color = Neutral700,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (step) {
                            OrderState.CONFIRMED -> "Confirmed"
                            OrderState.PREPARING -> "Kitchen"
                            OrderState.OUT_FOR_DELIVERY -> "On Way"
                            OrderState.ARRIVED -> "Arrived"
                        },
                        fontSize = 10.sp,
                        fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Medium,
                        color = if (isCurrent) StreetoRed else Neutral700,
                        maxLines = 1
                    )
                }

                // Connecting line between steps
                if (index < steps.size - 1) {
                    val lineActive = activeStep.stepIndex > index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .padding(bottom = 14.dp)
                            .background(if (lineActive) StreetoRed else Neutral300)
                    )
                }
            }
        }
    }
}
