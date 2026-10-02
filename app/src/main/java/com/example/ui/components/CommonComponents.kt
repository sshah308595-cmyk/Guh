package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreetoSampleData
import com.example.model.Driver
import com.example.ui.theme.Neutral300
import com.example.ui.theme.Neutral700
import com.example.ui.theme.Neutral900
import com.example.ui.theme.NonVegRed
import com.example.ui.theme.RatingGreen
import com.example.ui.theme.StreetoRed
import com.example.ui.theme.StreetoWarmBg
import com.example.ui.theme.VegGreen

@Composable
fun VegNonVegBadge(
    isVeg: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isVeg) VegGreen else NonVegRed
    Box(
        modifier = modifier
            .size(16.dp)
            .border(1.5.dp, color, RoundedCornerShape(3.dp))
            .padding(2.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(color, CircleShape)
        )
    }
}

@Composable
fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    Row(
        modifier = modifier
            .background(RatingGreen, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = String.format("%.1f", rating),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(10.dp)
        )
    }
}

@Composable
fun AddItemButton(
    count: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (count == 0) {
        Surface(
            modifier = modifier
                .shadow(2.dp, RoundedCornerShape(8.dp))
                .clickable(onClick = onAdd)
                .testTag("add_item_button"),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, StreetoRed)
        ) {
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .height(36.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ADD +",
                    color = StreetoRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    } else {
        Surface(
            modifier = modifier
                .shadow(3.dp, RoundedCornerShape(8.dp))
                .testTag("counter_item_button"),
            shape = RoundedCornerShape(8.dp),
            color = StreetoRed
        ) {
            Row(
                modifier = Modifier
                    .width(96.dp)
                    .height(36.dp)
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "−",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable(onClick = onRemove)
                        .padding(horizontal = 4.dp)
                        .testTag("decrease_item_button")
                )
                Text(
                    text = "$count",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable(onClick = onAdd)
                        .padding(horizontal = 4.dp)
                        .testTag("increase_item_button")
                )
            }
        }
    }
}

@Composable
fun StickyCartBar(
    itemCount: Int,
    totalAmount: Int,
    onViewCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = itemCount > 0,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .shadow(12.dp, RoundedCornerShape(16.dp))
                .navigationBarsPadding(),
            shape = RoundedCornerShape(16.dp),
            color = StreetoRed
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onViewCartClick)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("sticky_cart_bar"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Cart",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "$itemCount ${if (itemCount == 1) "ITEM" else "ITEMS"}",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹$totalAmount",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "View Cart →",
                        color = StreetoRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AddressPickerDialog(
    currentAddress: String,
    onSelectAddress: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var customAddress by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = StreetoRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Delivery Location",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutral900
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Delivering in Tulsipur / Balrampur Region",
                    fontSize = 13.sp,
                    color = Neutral700,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                StreetoSampleData.sampleAddresses.forEach { addr ->
                    val isSelected = addr == currentAddress
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectAddress(addr) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) StreetoWarmBg else Color(0xFFF7F7F7),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, StreetoRed) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) StreetoRed else Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = addr,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = Neutral900,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = customAddress,
                    onValueChange = { customAddress = it },
                    placeholder = { Text("Or type new street address...", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (customAddress.isNotBlank()) {
                        onSelectAddress(customAddress)
                    } else {
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = StreetoRed)
            ) {
                Text("Confirm Location", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Neutral700)
            }
        }
    )
}

@Composable
fun CallDriverDialog(
    driver: Driver,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Contact Delivery Partner",
                fontWeight = FontWeight.Bold,
                color = Neutral900,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(StreetoWarmBg, CircleShape)
                        .border(2.dp, StreetoRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🛵",
                        fontSize = 32.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = driver.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Neutral900
                )
                Text(
                    text = "${driver.rating} ★ • ${driver.vehicle}",
                    fontSize = 12.sp,
                    color = Neutral700
                )
                Text(
                    text = driver.phone,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = StreetoRed,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Delivery partner is vaccinated and practicing contactless safety.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 14.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${driver.phone.replace(" ", "")}")
                    }
                    try {
                        context.startActivity(intent)
                    } catch (_: Exception) { }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = StreetoRed)
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Call Now", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = Neutral700)
            }
        }
    )
}

@Composable
fun HelpSupportDialog(
    orderId: String,
    onDismiss: () -> Unit
) {
    var querySubmitted by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = null,
                    tint = StreetoRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Streeto Care & Help",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutral900
                )
            }
        },
        text = {
            if (querySubmitted) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "✅", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Support Ticket Created!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Neutral900
                    )
                    Text(
                        text = "Our Balrampur support desk is actively reviewing order $orderId. A support executive will reach out to you within 2 minutes.",
                        fontSize = 13.sp,
                        color = Neutral700,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            } else {
                Column {
                    Text(
                        text = "Order ID: $orderId",
                        fontSize = 12.sp,
                        color = StreetoRed,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Select your issue:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    listOf(
                        "Where is my delivery partner?",
                        "Change delivery address or phone",
                        "Special cooking instructions for chef",
                        "Cancel this order"
                    ).forEach { issue ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { querySubmitted = true },
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF7F7F7)
                        ) {
                            Text(
                                text = "• $issue",
                                fontSize = 13.sp,
                                color = Neutral900,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = StreetoRed)
            ) {
                Text(if (querySubmitted) "Done" else "Close")
            }
        }
    )
}
