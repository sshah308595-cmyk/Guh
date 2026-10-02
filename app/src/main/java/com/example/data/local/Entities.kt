package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val passwordHash: String,
    val phone: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_addresses")
data class AddressEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val address: String,
    val isDefault: Boolean = false
)

@Entity(tableName = "orders_history")
data class PastOrderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val vendorName: String,
    val itemsSummary: String,
    val totalAmount: Int,
    val paymentMethod: String,
    val deliveryAddress: String,
    val status: String,
    val orderDate: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_vendors")
data class FavoriteVendorEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val vendorId: String,
    val addedAt: Long = System.currentTimeMillis()
)
