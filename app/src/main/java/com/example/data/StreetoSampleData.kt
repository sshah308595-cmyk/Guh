package com.example.data

import com.example.R
import com.example.model.Coupon
import com.example.model.Dish
import com.example.model.Driver
import com.example.model.FoodCategory
import com.example.model.Vendor

object StreetoSampleData {

    val categories = listOf(
        FoodCategory("all", "All Items", "🔥", "Street picks"),
        FoodCategory("momos", "Momos", "🥟", "Steamed & fried"),
        FoodCategory("chowmein", "Chowmein", "🍜", "Desi Hakka"),
        FoodCategory("rolls", "Rolls", "🌯", "Kathi & Frankie"),
        FoodCategory("burgers", "Burgers", "🍔", "Desi Bun Tikki"),
        FoodCategory("chaat", "Chaat", "🥘", "Spicy & Tangy"),
        FoodCategory("drinks", "Drinks", "🥤", "Lassi & Shakes"),
        FoodCategory("desserts", "Desserts", "🍨", "Gulab Jamun & Kulfi")
    )

    val coupons = listOf(
        Coupon(
            code = "STREET50",
            title = "50% OFF up to ₹100",
            description = "Valid on all street food orders above ₹149",
            discountPercent = 50,
            maxDiscount = 100,
            minOrder = 149
        ),
        Coupon(
            code = "FIRSTBITE",
            title = "Flat ₹40 OFF + Free Delivery",
            description = "Welcome treat on your first Streeto order",
            flatDiscount = 40,
            minOrder = 99
        ),
        Coupon(
            code = "TASTEOFUP",
            title = "Flat ₹60 OFF above ₹249",
            description = "Exclusive Balrampur & Tulsipur local treat",
            flatDiscount = 60,
            minOrder = 249
        ),
        Coupon(
            code = "CHAAT30",
            title = "Flat ₹30 OFF on Street Snacks",
            description = "Quick bite special on orders above ₹79",
            flatDiscount = 30,
            minOrder = 79
        ),
        Coupon(
            code = "STREETFEAST",
            title = "30% OFF up to ₹150",
            description = "Perfect for group feast orders above ₹299",
            discountPercent = 30,
            maxDiscount = 150,
            minOrder = 299
        )
    )

    val vendors = listOf(
        Vendor(
            id = "v1",
            name = "Sharma Ji Chaat & Golgappe",
            tagLine = "Balrampur's most loved tangy pani puri & dahi bhalla",
            rating = 4.7,
            reviewsCount = "2.4k+ reviews",
            deliveryTimeMin = "15-20 min",
            distanceKm = "0.8 km",
            offerTag = "50% OFF up to ₹100",
            imageDrawableRes = R.drawable.chaat_special_1790928575414,
            isPureVeg = true,
            address = "Shop 14, Main Chowk, Balrampur",
            cuisines = listOf("Chaat", "Golgappe", "Street Food", "North Indian"),
            priceForTwo = "₹120 for two"
        ),
        Vendor(
            id = "v2",
            name = "Tibetan Hills Momo Point",
            tagLine = "Authentic spicy red chutney, kurkure & steamed momos",
            rating = 4.6,
            reviewsCount = "1.8k+ reviews",
            deliveryTimeMin = "20-25 min",
            distanceKm = "1.2 km",
            offerTag = "Flat ₹40 OFF above ₹199",
            imageDrawableRes = R.drawable.momo_delight_1790928520035,
            isPureVeg = false,
            address = "Near Railway Crossing, Station Road, Balrampur",
            cuisines = listOf("Momos", "Tibetan", "Chinese", "Snacks"),
            priceForTwo = "₹160 for two"
        ),
        Vendor(
            id = "v3",
            name = "Kolkata Kathi Rolls & Chowmein",
            tagLine = "Flaky laccha parathas loaded with roasted paneer & spices",
            rating = 4.4,
            reviewsCount = "950+ reviews",
            deliveryTimeMin = "25-30 min",
            distanceKm = "1.8 km",
            offerTag = "20% OFF up to ₹50",
            imageDrawableRes = R.drawable.street_food_banner_1790928504820,
            isPureVeg = false,
            address = "Near Gandhi Park Market, Tulsipur Road",
            cuisines = listOf("Rolls", "Chowmein", "Fast Food", "Chinese"),
            priceForTwo = "₹180 for two"
        ),
        Vendor(
            id = "v4",
            name = "Kailash Desi Bun Tikki & Burgers",
            tagLine = "Old-school crisp butter-toasted aaloo tikki burgers",
            rating = 4.5,
            reviewsCount = "1.3k+ reviews",
            deliveryTimeMin = "15-20 min",
            distanceKm = "1.0 km",
            offerTag = "Buy 1 Get 1 Free on Combos",
            imageDrawableRes = R.drawable.street_food_banner_1790928504820,
            isPureVeg = true,
            address = "Purani Bazar, Balrampur",
            cuisines = listOf("Burgers", "Street Snacks", "Fries", "Beverages"),
            priceForTwo = "₹140 for two"
        ),
        Vendor(
            id = "v5",
            name = "Banarasi Lassi & Sweets Corner",
            tagLine = "Thick clay-kulhad malai rabri lassi & hot gulab jamun",
            rating = 4.8,
            reviewsCount = "3.1k+ reviews",
            deliveryTimeMin = "10-15 min",
            distanceKm = "0.5 km",
            offerTag = "Free delivery on ₹99+",
            imageDrawableRes = R.drawable.chaat_special_1790928575414,
            isPureVeg = true,
            address = "Shiv Mandir Marg, Tulsipur",
            cuisines = listOf("Drinks", "Desserts", "Mithai", "Lassi"),
            priceForTwo = "₹100 for two"
        ),
        Vendor(
            id = "v6",
            name = "Delhi 6 Zaika & Pav Bhaji",
            tagLine = "Amul butter drenched spicy tawa pav bhaji & tawa pulao",
            rating = 4.5,
            reviewsCount = "1.1k+ reviews",
            deliveryTimeMin = "20-25 min",
            distanceKm = "1.5 km",
            offerTag = "30% OFF up to ₹75",
            imageDrawableRes = R.drawable.street_food_banner_1790928504820,
            isPureVeg = true,
            address = "Civil Lines, Opposite Head Post Office",
            cuisines = listOf("Pav Bhaji", "Chowmein", "North Indian", "Combos"),
            priceForTwo = "₹150 for two"
        )
    )

    val dishesByVendor: Map<String, List<Dish>> = mapOf(
        "v1" to listOf(
            Dish(
                id = "d101",
                vendorId = "v1",
                name = "Special Dahi Puri Platter (6 pcs)",
                price = 60,
                description = "Crispy hollow puris filled with spiced potatoes, sweetened curd, tangy imli chutney and crunchy sev.",
                isVeg = true,
                imageDrawableRes = R.drawable.chaat_special_1790928575414,
                rating = 4.8,
                ratingCount = 340,
                isBestseller = true,
                category = "chaat"
            ),
            Dish(
                id = "d102",
                vendorId = "v1",
                name = "Crispy Kurkuri Aloo Tikki Chaat",
                price = 70,
                description = "Double-fried crunchy potato patties bathed in spiced chole, whipped yogurt, and roasted cumin.",
                isVeg = true,
                imageDrawableRes = R.drawable.chaat_special_1790928575414,
                rating = 4.7,
                ratingCount = 280,
                isBestseller = true,
                category = "chaat"
            ),
            Dish(
                id = "d103",
                vendorId = "v1",
                name = "Kolkata Style Puchka / Pani Puri (8 pcs)",
                price = 40,
                description = "Spicy khatta-meetha hing pani with black chickpea mashed potato stuffing.",
                isVeg = true,
                imageDrawableRes = R.drawable.chaat_special_1790928575414,
                rating = 4.9,
                ratingCount = 512,
                isBestseller = true,
                category = "chaat"
            ),
            Dish(
                id = "d104",
                vendorId = "v1",
                name = "Papdi Chaat with Pomegranate & Sev",
                price = 65,
                description = "Crispy flour wafers topped with fresh curd, mint chutney, tamarind dip and fresh anar pearls.",
                isVeg = true,
                imageDrawableRes = R.drawable.chaat_special_1790928575414,
                rating = 4.6,
                ratingCount = 145,
                isBestseller = false,
                category = "chaat"
            )
        ),
        "v2" to listOf(
            Dish(
                id = "d201",
                vendorId = "v2",
                name = "Steamed Veg Paneer Momos (8 pcs)",
                price = 90,
                description = "Handcrafted soft dumplings stuffed with grated paneer, cabbage, onions, served with signature red fiery dip.",
                isVeg = true,
                imageDrawableRes = R.drawable.momo_delight_1790928520035,
                rating = 4.8,
                ratingCount = 420,
                isBestseller = true,
                category = "momos"
            ),
            Dish(
                id = "d202",
                vendorId = "v2",
                name = "Kurkure Fried Chicken Momos (6 pcs)",
                price = 130,
                description = "Extra crunchy crumb-coated fried chicken momos served with creamy garlic mayo and spicy schezwan sauce.",
                isVeg = false,
                imageDrawableRes = R.drawable.momo_delight_1790928520035,
                rating = 4.7,
                ratingCount = 310,
                isBestseller = true,
                category = "momos"
            ),
            Dish(
                id = "d203",
                vendorId = "v2",
                name = "Tandoori Gravy Momos",
                price = 140,
                description = "Charcoal-smoked momos tossed in rich tandoori butter cream gravy with capsicum and sliced onions.",
                isVeg = true,
                imageDrawableRes = R.drawable.momo_delight_1790928520035,
                rating = 4.6,
                ratingCount = 190,
                isBestseller = false,
                category = "momos"
            ),
            Dish(
                id = "d204",
                vendorId = "v2",
                name = "Spicy Street Thukpa Soup",
                price = 95,
                description = "Warm soothing Tibetan noodle broth cooked with seasonal vegetables and aromatic mountain spices.",
                isVeg = true,
                imageDrawableRes = R.drawable.momo_delight_1790928520035,
                rating = 4.4,
                ratingCount = 85,
                isBestseller = false,
                category = "chowmein"
            )
        ),
        "v3" to listOf(
            Dish(
                id = "d301",
                vendorId = "v3",
                name = "Double Paneer Tikka Kathi Roll",
                price = 110,
                description = "Crispy layered laccha paratha wrapped around marinated spicy grilled paneer, onions and green chili mayo.",
                isVeg = true,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.7,
                ratingCount = 295,
                isBestseller = true,
                category = "rolls"
            ),
            Dish(
                id = "d302",
                vendorId = "v3",
                name = "Kolkata Double Egg Chicken Roll",
                price = 130,
                description = "Authentic street roll layered with 2 fluffy farm eggs, tender chicken chunks, lemon juice and chatpata masala.",
                isVeg = false,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.8,
                ratingCount = 412,
                isBestseller = true,
                category = "rolls"
            ),
            Dish(
                id = "d303",
                vendorId = "v3",
                name = "Desi Street Hakka Chowmein",
                price = 80,
                description = "Wok-tossed noodles with shredded cabbage, carrots, bell peppers, dark soya and garlic chili sauce.",
                isVeg = true,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.5,
                ratingCount = 210,
                isBestseller = true,
                category = "chowmein"
            ),
            Dish(
                id = "d304",
                vendorId = "v3",
                name = "Schezwan Egg Chowmein",
                price = 100,
                description = "Spicy fiery street noodles tossed with scrambled eggs, scallions and pungent schezwan peppers.",
                isVeg = false,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.4,
                ratingCount = 130,
                isBestseller = false,
                category = "chowmein"
            )
        ),
        "v4" to listOf(
            Dish(
                id = "d401",
                vendorId = "v4",
                name = "Maharaja Crispy Aloo Tikki Burger",
                price = 60,
                description = "Tawa-grilled butter buns loaded with golden potato patty, sliced tomatoes, onions and thousand island dressing.",
                isVeg = true,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.6,
                ratingCount = 380,
                isBestseller = true,
                category = "burgers"
            ),
            Dish(
                id = "d402",
                vendorId = "v4",
                name = "Cheese Burst Paneer Burger",
                price = 99,
                description = "Thick grilled paneer steak with melting cheddar slice, jalapenos and spicy peri-peri sauce.",
                isVeg = true,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.7,
                ratingCount = 220,
                isBestseller = true,
                category = "burgers"
            ),
            Dish(
                id = "d403",
                vendorId = "v4",
                name = "Peri-Peri Masala French Fries",
                price = 70,
                description = "Crispy crinkle-cut fries generously seasoned with spicy street peri-peri seasoning.",
                isVeg = true,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.4,
                ratingCount = 150,
                isBestseller = false,
                category = "burgers"
            )
        ),
        "v5" to listOf(
            Dish(
                id = "d501",
                vendorId = "v5",
                name = "Special Banarasi Kulhad Malai Lassi (350ml)",
                price = 70,
                description = "Slow-churned thick sweet yogurt served in earthen clay kulhad, crowned with thick rabri malai and pistachios.",
                isVeg = true,
                imageDrawableRes = R.drawable.chaat_special_1790928575414,
                rating = 4.9,
                ratingCount = 680,
                isBestseller = true,
                category = "drinks"
            ),
            Dish(
                id = "d502",
                vendorId = "v5",
                name = "Hot Desi Ghee Gulab Jamun (2 pcs)",
                price = 50,
                description = "Melt-in-mouth soft khoya dumplings soaked in fragrant cardamom & saffron sugar syrup.",
                isVeg = true,
                imageDrawableRes = R.drawable.chaat_special_1790928575414,
                rating = 4.8,
                ratingCount = 490,
                isBestseller = true,
                category = "desserts"
            ),
            Dish(
                id = "d503",
                vendorId = "v5",
                name = "Kesar Pista Matka Kulfi",
                price = 60,
                description = "Traditional slow-reduced milk kulfi infused with real saffron strands and crushed dry fruits.",
                isVeg = true,
                imageDrawableRes = R.drawable.chaat_special_1790928575414,
                rating = 4.7,
                ratingCount = 210,
                isBestseller = false,
                category = "desserts"
            )
        ),
        "v6" to listOf(
            Dish(
                id = "d601",
                vendorId = "v6",
                name = "Special Butter Pav Bhaji (2 Pav)",
                price = 110,
                description = "Slow-cooked mashed vegetable curry cooked with secret spices, topped with an entire slab of Amul butter and fluffy pav.",
                isVeg = true,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.7,
                ratingCount = 390,
                isBestseller = true,
                category = "chaat"
            ),
            Dish(
                id = "d602",
                vendorId = "v6",
                name = "Mumbai Style Tawa Masala Pulao",
                price = 90,
                description = "Fragrant basmati rice tossed on a smoking tawa with pav bhaji masala, paneer cubes, and fresh coriander.",
                isVeg = true,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.5,
                ratingCount = 180,
                isBestseller = false,
                category = "chaat"
            ),
            Dish(
                id = "d603",
                vendorId = "v6",
                name = "Extra Butter Toasted Pav (2 pcs)",
                price = 30,
                description = "Soft bakery pav roasted crisp on hot iron tawa with salted Amul butter.",
                isVeg = true,
                imageDrawableRes = R.drawable.street_food_banner_1790928504820,
                rating = 4.6,
                ratingCount = 120,
                isBestseller = false,
                category = "chaat"
            )
        )
    )

    val defaultDriver = Driver(
        name = "Ramesh Kumar",
        phone = "+91 98765 43210",
        rating = 4.9,
        vehicle = "TVS Jupiter • UP 47 AB 2341",
        trips = "1,850+ deliveries"
    )

    val sampleAddresses = listOf(
        "Near Shiv Mandir, Tulsipur, Balrampur",
        "Opposite Railway Station, Station Road, Balrampur",
        "Flat 302, Green Valley Apartments, Civil Lines",
        "Shop No. 5, Gandhi Market, Balrampur",
        "Pahalwara Chowk, Balrampur"
    )
}
