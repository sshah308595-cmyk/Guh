package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.StreetoSampleData
import com.example.model.Dish
import com.example.model.Vendor
import com.example.ui.components.AddItemButton
import com.example.ui.components.RatingBadge
import com.example.ui.components.VegNonVegBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.GoldOffer
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
fun VendorMenuScreen(
    vendor: Vendor,
    uiState: StreetoUiState,
    viewModel: StreetoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    var menuSearchQuery by remember { mutableStateOf("") }
    var vegOnlyFilter by remember { mutableStateOf(false) }

    val allDishes = StreetoSampleData.dishesByVendor[vendor.id]
        ?: StreetoSampleData.dishesByVendor["v1"]
        ?: emptyList()

    val filteredDishes = allDishes.filter { dish ->
        val matchesVeg = !vegOnlyFilter || dish.isVeg
        val matchesSearch = menuSearchQuery.isBlank() ||
            dish.name.lowercase().contains(menuSearchQuery.lowercase()) ||
            dish.description.lowercase().contains(menuSearchQuery.lowercase())
        matchesVeg && matchesSearch
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF9FAFB))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = if (uiState.totalItemCount > 0) 100.dp else 32.dp)
        ) {
            // Header Image & Overlay
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = vendor.imageDrawableRes),
                        contentDescription = vendor.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                )
                            )
                    )

                    // Navigation bar with Back button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.navigateBack() },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                                .testTag("menu_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Row {
                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Offer tag at bottom of image
                    if (vendor.offerTag.isNotBlank()) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = StreetoRed
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = vendor.offerTag,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Vendor Info Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-16).dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = vendor.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Neutral900
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = vendor.tagLine,
                                    fontSize = 12.sp,
                                    color = Neutral700
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(horizontalAlignment = Alignment.End) {
                                RatingBadge(rating = vendor.rating)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = vendor.reviewsCount,
                                    fontSize = 10.sp,
                                    color = Neutral500,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = vendor.cuisines.joinToString(" • "),
                            fontSize = 12.sp,
                            color = Neutral500
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = Neutral300.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = StreetoRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = vendor.address,
                                fontSize = 11.sp,
                                color = Neutral700,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = Neutral500,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = vendor.deliveryTimeMin,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Neutral900
                            )
                        }
                    }
                }
            }

            // Menu Filter Controls (Veg toggle)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "RECOMMENDED STREET FOOD",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Neutral900,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${filteredDishes.size})",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (vegOnlyFilter) Color(0xFFE8F5E9) else Neutral100)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        VegNonVegBadge(isVeg = true)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Veg Only",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (vegOnlyFilter) VegGreen else Neutral700
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = vegOnlyFilter,
                            onCheckedChange = { vegOnlyFilter = it },
                            modifier = Modifier.size(32.dp),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = VegGreen,
                                checkedTrackColor = Color(0xFFA5D6A7)
                            )
                        )
                    }
                }
            }

            // Dish Cards
            items(filteredDishes, key = { it.id }) { dish ->
                val quantityInCart = uiState.cartItems[dish.id]?.quantity ?: 0
                DishCardItem(
                    dish = dish,
                    quantity = quantityInCart,
                    onAdd = { viewModel.addToCart(dish) },
                    onRemove = { viewModel.removeFromCart(dish) }
                )
            }
        }
    }
}

@Composable
fun DishCardItem(
    dish: Dish,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("dish_item_${dish.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column: Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VegNonVegBadge(isVeg = dish.isVeg)
                    if (dish.isBestseller) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFF3E0)
                        ) {
                            Text(
                                text = "⭐ BESTSELLER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = dish.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutral900
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${dish.price}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Neutral900
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingBadge(rating = dish.rating)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${dish.ratingCount})",
                        fontSize = 11.sp,
                        color = Neutral500
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = dish.description,
                    fontSize = 12.sp,
                    color = Neutral700,
                    lineHeight = 16.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Right Column: Image & Add Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    val fallbackRes = when (dish.category) {
                        "momos" -> R.drawable.momo_delight_1790928520035
                        "chaat" -> R.drawable.chaat_special_1790928575414
                        else -> R.drawable.street_food_banner_1790928504820
                    }
                    Image(
                        painter = painterResource(id = if (dish.imageDrawableRes != 0) dish.imageDrawableRes else fallbackRes),
                        contentDescription = dish.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                AddItemButton(
                    count = quantity,
                    onAdd = onAdd,
                    onRemove = onRemove
                )
            }
        }
    }
}
