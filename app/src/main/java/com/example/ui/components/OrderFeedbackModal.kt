package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.ui.theme.CardBorder
import com.example.ui.theme.Neutral100
import com.example.ui.theme.Neutral300
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral700
import com.example.ui.theme.Neutral900
import com.example.ui.theme.RatingGreen
import com.example.ui.theme.StarYellow
import com.example.ui.theme.StreetoRed
import com.example.ui.theme.StreetoWarmBg

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OrderFeedbackModal(
    order: Order,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSubmitFeedback: (
        foodRating: Int,
        deliveryRating: Int,
        foodTags: List<String>,
        deliveryTags: List<String>,
        comment: String,
        tipAmount: Int
    ) -> Unit
) {
    var foodRating by remember { mutableIntStateOf(5) }
    var deliveryRating by remember { mutableIntStateOf(5) }
    var selectedFoodTags by remember { mutableStateOf(setOf("Hot & Fresh 🔥", "Authentic Taste 🤤")) }
    var selectedDeliveryTags by remember { mutableStateOf(setOf("Super Fast ⚡", "Polite & Helpful 😊")) }
    var userComment by remember { mutableStateOf("") }
    var selectedTip by remember { mutableIntStateOf(20) }
    var isSubmitted by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val availableFoodTags = listOf(
        "Hot & Fresh 🔥",
        "Super Crisp ✨",
        "Authentic Taste 🤤",
        "Generous Portion 🥣",
        "Spicy Chutney Loved 🌶️",
        "Spill-proof Packaging 📦"
    )

    val availableDeliveryTags = listOf(
        "Super Fast ⚡",
        "Polite & Helpful 😊",
        "Followed Instructions 📍",
        "Contactless Handover 🛡️"
    )

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
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isSubmitted) {
                // Post-Submission Thank You View
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFE8F5E9), CircleShape)
                        .border(2.dp, RatingGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = RatingGreen,
                        modifier = Modifier.size(52.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Thank You for the Review!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Neutral900
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your feedback helps ${order.vendor.name} and delivery partner ${order.driver.name} keep serving great street food in Balrampur!",
                    fontSize = 13.sp,
                    color = Neutral700,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
                if (selectedTip > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = StreetoWarmBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StreetoRed.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💖", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "₹$selectedTip tip sent to ${order.driver.name}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StreetoRed
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StreetoRed)
                ) {
                    Text("Done", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Rate Your Street Feast!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Neutral900
                        )
                        Text(
                            text = "Order #${order.id} from ${order.vendor.name}",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Neutral700)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SECTION 1: Food Quality Rating
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Neutral100),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "How was the food?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (foodRating) {
                                1 -> "Disappointing 😞"
                                2 -> "Below Average 😐"
                                3 -> "Good & Decent 🙂"
                                4 -> "Really Tasty! 😋"
                                else -> "Authentic Street Magic! 🔥🤤"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (foodRating >= 4) RatingGreen else StreetoRed
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Star selector
                        StarRatingBar(
                            rating = foodRating,
                            onRatingSelected = { foodRating = it }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Food tags chips
                        Text(
                            text = "What did you enjoy most?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Neutral700,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            availableFoodTags.forEach { tag ->
                                val isSelected = selectedFoodTags.contains(tag)
                                Surface(
                                    modifier = Modifier.clickable {
                                        selectedFoodTags = if (isSelected) {
                                            selectedFoodTags - tag
                                        } else {
                                            selectedFoodTags + tag
                                        }
                                    },
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (isSelected) StreetoWarmBg else Color.White,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) StreetoRed else CardBorder
                                    )
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) StreetoRed else Neutral700,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SECTION 2: Delivery Partner Rating
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Neutral100),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Rate Delivery by ${order.driver.name}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (deliveryRating) {
                                1 -> "Late or cold delivery"
                                2 -> "Average delivery"
                                3 -> "Good delivery"
                                4 -> "Prompt & courteous!"
                                else -> "Lightning fast & super polite! 🛵⚡"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (deliveryRating >= 4) RatingGreen else StreetoRed
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Star selector
                        StarRatingBar(
                            rating = deliveryRating,
                            onRatingSelected = { deliveryRating = it }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Delivery tags chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            availableDeliveryTags.forEach { tag ->
                                val isSelected = selectedDeliveryTags.contains(tag)
                                Surface(
                                    modifier = Modifier.clickable {
                                        selectedDeliveryTags = if (isSelected) {
                                            selectedDeliveryTags - tag
                                        } else {
                                            selectedDeliveryTags + tag
                                        }
                                    },
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (isSelected) StreetoWarmBg else Color.White,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) StreetoRed else CardBorder
                                    )
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) StreetoRed else Neutral700,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Neutral300.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Tip Delivery Partner
                        Text(
                            text = "Tip ${order.driver.name} (100% goes to rider)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral700,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(0 to "No Tip", 10 to "₹10", 20 to "₹20 💖", 30 to "₹30", 50 to "₹50").forEach { (tip, label) ->
                                val isSelected = selectedTip == tip
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedTip = tip },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) StreetoWarmBg else Color.White,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) StreetoRed else CardBorder
                                    )
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) StreetoRed else Neutral700,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Optional Written Review
                OutlinedTextField(
                    value = userComment,
                    onValueChange = { userComment = it },
                    placeholder = { Text("Write a message for the vendor or rider (optional)...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        isSubmitted = true
                        onSubmitFeedback(
                            foodRating,
                            deliveryRating,
                            selectedFoodTags.toList(),
                            selectedDeliveryTags.toList(),
                            userComment,
                            selectedTip
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_feedback_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StreetoRed)
                ) {
                    Text(
                        text = "Submit Review ⭐",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Skip for Now", color = Neutral500, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StarRatingBar(
    rating: Int,
    onRatingSelected: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            val isFilled = i <= rating
            IconButton(
                onClick = { onRatingSelected(i) },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "$i Star",
                    tint = if (isFilled) StarYellow else Neutral300,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
}
