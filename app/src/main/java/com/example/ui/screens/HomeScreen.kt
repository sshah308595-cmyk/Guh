package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.StreetoSampleData
import com.example.model.FoodCategory
import com.example.model.Vendor
import com.example.ui.components.RatingBadge
import com.example.ui.components.RecentSearchesDropdown
import com.example.ui.components.VegNonVegBadge
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
fun HomeScreen(
    uiState: StreetoUiState,
    viewModel: StreetoViewModel,
    modifier: Modifier = Modifier
) {
    val filteredVendors = viewModel.getFilteredVendors()
    val filterScrollState = rememberScrollState()
    val categoryScrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var isSearchFocused by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB)),
        contentPadding = PaddingValues(bottom = if (uiState.totalItemCount > 0) 90.dp else 24.dp)
    ) {
        // 1. Top Header: Location Selector & Avatar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Location pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.showAddressDialog(true) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(StreetoWarmBg, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = StreetoRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Streeto Delivery",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = StreetoRed,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = uiState.selectedAddress,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Neutral900,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(max = 220.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select address",
                                    tint = Neutral900,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Avatar & action icons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { viewModel.openOrderHistoryScreen() }
                                .testTag("order_history_nav_button"),
                            shape = CircleShape,
                            color = Neutral100
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = "Order History",
                                    tint = Neutral700,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { viewModel.openNotificationsSheet() }
                                .testTag("notifications_bell_button"),
                            shape = CircleShape,
                            color = if (uiState.inAppNotifications.isNotEmpty()) StreetoWarmBg else Neutral100
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsNone,
                                    contentDescription = "Notifications",
                                    tint = if (uiState.inAppNotifications.isNotEmpty()) StreetoRed else Neutral700,
                                    modifier = Modifier.size(20.dp)
                                )
                                if (uiState.inAppNotifications.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(top = 4.dp, end = 4.dp)
                                            .size(8.dp)
                                            .background(StreetoRed, CircleShape)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        // User/Brand Logo
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, StreetoRed, CircleShape)
                                .clickable { viewModel.openProfileModal() }
                                .testTag("user_profile_avatar"),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_streeto_logo_v2_1790929092412),
                                contentDescription = "Streeto App Logo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isSearchFocused = focusState.isFocused
                        }
                        .testTag("search_food_input"),
                    placeholder = {
                        Text(
                            text = "Search momos, rolls, chaat, chowmein...",
                            fontSize = 13.sp,
                            color = Neutral500
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = StreetoRed
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Neutral500
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (uiState.searchQuery.isNotBlank()) {
                                viewModel.addRecentSearch(uiState.searchQuery)
                            }
                            focusManager.clearFocus()
                            isSearchFocused = false
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Neutral100,
                        unfocusedContainerColor = Neutral100,
                        focusedBorderColor = StreetoRed,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )

                // Recent Searches Dropdown Panel (shown when search bar is focused)
                if (isSearchFocused) {
                    Spacer(modifier = Modifier.height(10.dp))
                    RecentSearchesDropdown(
                        recentSearches = uiState.recentSearches,
                        onSelectSearch = { query ->
                            viewModel.setSearchQuery(query)
                            viewModel.addRecentSearch(query)
                            focusManager.clearFocus()
                            isSearchFocused = false
                        },
                        onRemoveSearch = { query ->
                            viewModel.removeRecentSearch(query)
                        },
                        onClearAll = {
                            viewModel.clearRecentSearches()
                        },
                        onClose = {
                            focusManager.clearFocus()
                            isSearchFocused = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Filter Chips: [Pure Veg, Rating 4.0+, Under 30 Mins, Street Food Offers]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(filterScrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pure Veg Chip
                    FilterChip(
                        selected = uiState.isPureVegOnly,
                        onClick = { viewModel.togglePureVeg() },
                        label = { Text("Pure Veg", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        leadingIcon = {
                            VegNonVegBadge(isVeg = true)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFE8F5E9),
                            selectedLabelColor = VegGreen
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = uiState.isPureVegOnly,
                            selectedBorderColor = VegGreen,
                            borderColor = Neutral300
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Rating 4.0+ Chip
                    FilterChip(
                        selected = uiState.rating4PlusOnly,
                        onClick = { viewModel.toggleRating4Plus() },
                        label = { Text("Rating 4.0+", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (uiState.rating4PlusOnly) RatingGreen else Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StreetoWarmBg,
                            selectedLabelColor = StreetoRed
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = uiState.rating4PlusOnly,
                            selectedBorderColor = StreetoRed,
                            borderColor = Neutral300
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Under 30 Mins Chip
                    FilterChip(
                        selected = uiState.under30MinsOnly,
                        onClick = { viewModel.toggleUnder30Mins() },
                        label = { Text("Under 30 Mins", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (uiState.under30MinsOnly) StreetoRed else Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StreetoWarmBg,
                            selectedLabelColor = StreetoRed
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = uiState.under30MinsOnly,
                            selectedBorderColor = StreetoRed,
                            borderColor = Neutral300
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Street Food Offers Chip
                    FilterChip(
                        selected = uiState.hasOffersOnly,
                        onClick = { viewModel.toggleHasOffers() },
                        label = { Text("Offers %", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = if (uiState.hasOffersOnly) StreetoRed else Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StreetoWarmBg,
                            selectedLabelColor = StreetoRed
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = uiState.hasOffersOnly,
                            selectedBorderColor = StreetoRed,
                            borderColor = Neutral300
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }

        // 2. Horizontal Categories Circles
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "WHAT'S ON YOUR MIND?",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Neutral500,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(categoryScrollState)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    StreetoSampleData.categories.forEach { cat ->
                        val isSelected = uiState.selectedCategory == cat.id
                        CategoryCircleItem(
                            category = cat,
                            isSelected = isSelected,
                            onClick = { viewModel.selectCategory(cat.id) }
                        )
                    }
                }
            }
        }

        // 3. Featured Promotional Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { viewModel.applyCoupon("STREET50") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.street_food_banner_1790928504820),
                        contentDescription = "Street Food Festival",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.85f),
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = StreetoRed
                        ) {
                            Text(
                                text = "STREET FESTIVAL",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "50% OFF UP TO ₹100",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "On best local street chaat, momos & rolls • Code: STREET50",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // 4. Section Title: "Top Street Food Vendors"
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Top Street Food Vendors",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Neutral900
                    )
                    Text(
                        text = "${filteredVendors.size} vendors serving in Tulsipur / Balrampur",
                        fontSize = 12.sp,
                        color = Neutral700
                    )
                }
            }
        }

        // 5. Vendor Cards List
        if (filteredVendors.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🍽️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No street vendors found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900
                    )
                    Text(
                        text = "Try clearing filters or searching for different dishes.",
                        fontSize = 13.sp,
                        color = Neutral700,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            items(filteredVendors, key = { it.id }) { vendor ->
                val isFav = uiState.favoriteVendorIds.contains(vendor.id)
                VendorCard(
                    vendor = vendor,
                    isFavorite = isFav,
                    onToggleFavorite = { viewModel.toggleFavorite(vendor.id) },
                    onClick = { viewModel.openVendorMenu(vendor) }
                )
            }
        }
    }
}

@Composable
fun CategoryCircleItem(
    category: FoodCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("category_${category.id}")
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(if (isSelected) StreetoWarmBg else Color.White)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) StreetoRed else CardBorder,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.emoji,
                fontSize = 28.sp
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) StreetoRed else Neutral900
        )
    }
}

@Composable
fun VendorCard(
    vendor: Vendor,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("vendor_card_${vendor.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Vendor Image with Overlays
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = vendor.imageDrawableRes),
                    contentDescription = vendor.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top left veg indicator
                if (vendor.isPureVeg) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VegNonVegBadge(isVeg = true)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PURE VEG",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = VegGreen
                            )
                        }
                    }
                }

                // Top right Favorite toggle button
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onToggleFavorite)
                        .testTag("favorite_button_${vendor.id}"),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.92f),
                    shadowElevation = 3.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) StreetoRed else Neutral700,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Bottom offer badge overlay
                if (vendor.offerTag.isNotBlank()) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1B5E20)
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

            // Vendor Details
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vendor.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral900,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RatingBadge(rating = vendor.rating)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = vendor.cuisines.joinToString(" • "),
                    fontSize = 12.sp,
                    color = Neutral700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = Neutral500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = vendor.deliveryTimeMin,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Neutral700
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "•", color = Neutral500, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = vendor.distanceKm,
                        fontSize = 12.sp,
                        color = Neutral700
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "•", color = Neutral500, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = vendor.priceForTwo,
                        fontSize = 12.sp,
                        color = Neutral500
                    )
                }
            }
        }
    }
}
