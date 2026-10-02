package com.example.data.repository

import com.example.data.local.AddressEntity
import com.example.data.local.FavoriteVendorEntity
import com.example.data.local.PastOrderEntity
import com.example.data.local.StreetoDatabase
import com.example.data.local.UserEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class StreetoRepository(private val database: StreetoDatabase) {

    private val userDao = database.userDao()
    private val addressDao = database.addressDao()
    private val orderDao = database.orderDao()
    private val favoriteDao = database.favoriteVendorDao()

    suspend fun initializeDefaultData() {
        val defaultUser = userDao.getUserByEmail("rahul@streeto.in")
        if (defaultUser == null) {
            val user = UserEntity(
                id = "usr_default_01",
                name = "Rahul Sharma",
                email = "rahul@streeto.in",
                passwordHash = "password123",
                phone = "+91 98765 43210"
            )
            userDao.insertUser(user)

            // Seed default addresses
            addressDao.insertAddress(
                AddressEntity(
                    id = "addr_01",
                    userId = user.id,
                    title = "Home",
                    address = "Near Shiv Mandir, Tulsipur, Balrampur",
                    isDefault = true
                )
            )
            addressDao.insertAddress(
                AddressEntity(
                    id = "addr_02",
                    userId = user.id,
                    title = "Work",
                    address = "Shop 12, Main Bazar, Balrampur",
                    isDefault = false
                )
            )

            // Seed past orders
            orderDao.insertOrder(
                PastOrderEntity(
                    id = "STREETO-882193",
                    userId = user.id,
                    vendorName = "Sharma Ji Chaat & Golgappe",
                    itemsSummary = "1x Special Dahi Puri Platter, 1x Crispy Kurkuri Aloo Tikki Chaat",
                    totalAmount = 145,
                    paymentMethod = "UPI",
                    deliveryAddress = "Near Shiv Mandir, Tulsipur, Balrampur",
                    status = "Delivered",
                    orderDate = "Yesterday, 7:30 PM",
                    timestamp = System.currentTimeMillis() - 86400000L
                )
            )
            orderDao.insertOrder(
                PastOrderEntity(
                    id = "STREETO-432190",
                    userId = user.id,
                    vendorName = "Tibetan Hills Momo Point",
                    itemsSummary = "2x Steamed Veg Paneer Momos",
                    totalAmount = 199,
                    paymentMethod = "COD",
                    deliveryAddress = "Near Shiv Mandir, Tulsipur, Balrampur",
                    status = "Delivered",
                    orderDate = "3 days ago, 8:15 PM",
                    timestamp = System.currentTimeMillis() - 259200000L
                )
            )

            // Seed initial favorites
            favoriteDao.insertFavorite(
                FavoriteVendorEntity(
                    id = "${user.id}_v1",
                    userId = user.id,
                    vendorId = "v1"
                )
            )
            favoriteDao.insertFavorite(
                FavoriteVendorEntity(
                    id = "${user.id}_v2",
                    userId = user.id,
                    vendorId = "v2"
                )
            )
        }
    }

    suspend fun signUp(name: String, email: String, password: String, phone: String): Result<UserEntity> {
        val trimmedEmail = email.trim().lowercase()
        val existing = userDao.getUserByEmail(trimmedEmail)
        if (existing != null) {
            return Result.failure(Exception("An account with this email already exists"))
        }

        val newUser = UserEntity(
            id = "usr_" + UUID.randomUUID().toString().take(8),
            name = name.trim(),
            email = trimmedEmail,
            passwordHash = password,
            phone = phone.trim()
        )
        userDao.insertUser(newUser)

        // Add default home address for new user
        addressDao.insertAddress(
            AddressEntity(
                id = "addr_" + UUID.randomUUID().toString().take(8),
                userId = newUser.id,
                title = "Home",
                address = "Tulsipur / Balrampur",
                isDefault = true
            )
        )

        return Result.success(newUser)
    }

    suspend fun logIn(email: String, password: String): Result<UserEntity> {
        val trimmedEmail = email.trim().lowercase()
        val user = userDao.getUserByEmail(trimmedEmail)
            ?: return Result.failure(Exception("Account not found with this email"))

        if (user.passwordHash != password) {
            return Result.failure(Exception("Incorrect password. Please try again."))
        }

        return Result.success(user)
    }

    suspend fun getUserById(userId: String): UserEntity? {
        return userDao.getUserById(userId)
    }

    fun getSavedAddresses(userId: String): Flow<List<AddressEntity>> {
        return addressDao.getAddressesForUser(userId)
    }

    suspend fun addAddress(userId: String, title: String, address: String, isDefault: Boolean = false) {
        val newAddress = AddressEntity(
            id = "addr_" + UUID.randomUUID().toString().take(8),
            userId = userId,
            title = title,
            address = address,
            isDefault = isDefault
        )
        addressDao.insertAddress(newAddress)
    }

    suspend fun deleteAddress(addressId: String) {
        addressDao.deleteAddress(addressId)
    }

    fun getOrdersHistory(userId: String): Flow<List<PastOrderEntity>> {
        return orderDao.getOrdersForUser(userId)
    }

    suspend fun recordCompletedOrder(
        orderId: String,
        userId: String,
        vendorName: String,
        itemsSummary: String,
        totalAmount: Int,
        paymentMethod: String,
        deliveryAddress: String
    ) {
        val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        val dateStr = "Today, " + dateFormat.format(Date()).substringAfter(", ")

        val pastOrder = PastOrderEntity(
            id = orderId,
            userId = userId,
            vendorName = vendorName,
            itemsSummary = itemsSummary,
            totalAmount = totalAmount,
            paymentMethod = paymentMethod,
            deliveryAddress = deliveryAddress,
            status = "Processing",
            orderDate = dateStr,
            timestamp = System.currentTimeMillis()
        )
        orderDao.insertOrder(pastOrder)
    }

    fun getFavoriteVendors(userId: String): Flow<List<FavoriteVendorEntity>> {
        return favoriteDao.getFavoritesForUser(userId)
    }

    suspend fun addFavorite(userId: String, vendorId: String) {
        favoriteDao.insertFavorite(
            FavoriteVendorEntity(
                id = "${userId}_$vendorId",
                userId = userId,
                vendorId = vendorId
            )
        )
    }

    suspend fun removeFavorite(userId: String, vendorId: String) {
        favoriteDao.deleteFavorite(userId, vendorId)
    }
}
