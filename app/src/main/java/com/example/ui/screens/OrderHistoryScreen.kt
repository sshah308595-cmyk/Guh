package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PastOrderEntity
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
import com.example.viewmodel.StreetoUiState
import com.example.viewmodel.StreetoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    uiState: StreetoUiState,
    viewModel: StreetoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    var selectedOrderForDetail by remember { mutableStateOf<PastOrderEntity?>(null) }
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val orders = uiState.pastOrders

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        // Top App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("order_history_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Neutral900
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "My Order History",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Text(
                        text = "${orders.size} ${if (orders.size == 1) "past order" else "past orders"} in Tulsipur / Balrampur",
                        fontSize = 11.sp,
                        color = Neutral500
                    )
                }
            }
        }

        // Orders List
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛍️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Past Orders Found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Your completed street food orders and invoices will appear here.",
                        fontSize = 13.sp,
                        color = Neutral700,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.navigateBack() },
                        colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Explore Local Street Food")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders, key = { it.id }) { pastOrder ->
                    OrderHistoryCard(
                        order = pastOrder,
                        onClick = {
                            selectedOrderForDetail = pastOrder
                        },
                        onReorder = {
                            viewModel.reorderPastItems(pastOrder)
                        }
                    )
                }
            }
        }
    }

    // Clickable Order Detail Modal Bottom Sheet
    if (selectedOrderForDetail != null) {
        OrderDetailModalSheet(
            order = selectedOrderForDetail!!,
            sheetState = detailSheetState,
            onDismiss = {
                coroutineScope.launch {
                    detailSheetState.hide()
                    selectedOrderForDetail = null
                }
            },
            onReorder = {
                coroutineScope.launch {
                    detailSheetState.hide()
                    val orderToReorder = selectedOrderForDetail
                    selectedOrderForDetail = null
                    if (orderToReorder != null) {
                        viewModel.reorderPastItems(orderToReorder)
                    }
                }
            },
            onRateOrder = {
                coroutineScope.launch {
                    detailSheetState.hide()
                    selectedOrderForDetail = null
                    viewModel.openFeedbackModal()
                }
            }
        )
    }
}

@Composable
fun OrderHistoryCard(
    order: PastOrderEntity,
    onClick: () -> Unit,
    onReorder: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("order_card_${order.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Vendor & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(StreetoWarmBg, CircleShape)
                            .border(1.dp, StreetoRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = StreetoRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = order.vendorName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = order.orderDate,
                            fontSize = 11.sp,
                            color = Neutral500
                        )
                    }
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (order.status == "Delivered") Color(0xFFE8F5E9) else StreetoWarmBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (order.status == "Delivered") Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (order.status == "Delivered") RatingGreen else StreetoRed,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = order.status,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (order.status == "Delivered") RatingGreen else StreetoRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Neutral300.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Items Summary
            Text(
                text = order.itemsSummary,
                fontSize = 13.sp,
                color = Neutral700,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Address Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Neutral500,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = order.deliveryAddress,
                    fontSize = 11.sp,
                    color = Neutral500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Neutral300.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Footer: Amount, Payment & Reorder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Paid",
                        fontSize = 10.sp,
                        color = Neutral500,
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${order.totalAmount}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Neutral900
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Neutral100
                        ) {
                            Text(
                                text = order.paymentMethod,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Neutral700,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onClick) {
                        Text(
                            text = "Details ➔",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StreetoRed
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = onReorder,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reorder", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailModalSheet(
    order: PastOrderEntity,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onReorder: () -> Unit,
    onRateOrder: () -> Unit
) {
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 8.dp)
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
                        text = "Order Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Text(
                        text = "Order ID: #${order.id}",
                        fontSize = 12.sp,
                        color = StreetoRed,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Neutral700)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vendor & Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Neutral100),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = order.vendorName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral900
                        )
                        Text(
                            text = "Placed on ${order.orderDate}",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (order.status == "Delivered") Color(0xFFE8F5E9) else StreetoWarmBg
                    ) {
                        Text(
                            text = order.status,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (order.status == "Delivered") RatingGreen else StreetoRed,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Itemized Items
            Text(
                text = "Items Ordered",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Split items
            val itemsList = order.itemsSummary.split(",")
            itemsList.forEach { rawItem ->
                val clean = rawItem.trim()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("•", color = StreetoRed, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = clean,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Neutral900
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Neutral300.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(14.dp))

            // Bill Breakdown
            Text(
                text = "Bill Summary",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900
            )
            Spacer(modifier = Modifier.height(8.dp))

            DetailBillRow(label = "Items Subtotal", value = "₹${order.totalAmount - 30}")
            DetailBillRow(label = "Delivery Partner Fee", value = "₹25")
            DetailBillRow(label = "Platform Fee", value = "₹5")
            DetailBillRow(label = "Taxes & Packaging", value = "Included")

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Neutral300)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Paid",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Neutral900
                )
                Text(
                    text = "₹${order.totalAmount}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = StreetoRed
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Neutral300.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(14.dp))

            // Delivery Details
            Text(
                text = "Delivery Location",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = StreetoRed, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = order.deliveryAddress,
                    fontSize = 12.sp,
                    color = Neutral700
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Payments, contentDescription = null, tint = Neutral500, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Payment Method: ${order.paymentMethod}",
                    fontSize = 12.sp,
                    color = Neutral700
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onRateOrder,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StreetoRed)
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = StreetoRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Rate Food", color = StreetoRed, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onReorder,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StreetoRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reorder Meal", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailBillRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Neutral700)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Neutral900)
    }
}
