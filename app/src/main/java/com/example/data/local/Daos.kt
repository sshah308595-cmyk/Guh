package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
}

@Dao
interface AddressDao {
    @Query("SELECT * FROM saved_addresses WHERE userId = :userId")
    fun getAddressesForUser(userId: String): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Query("DELETE FROM saved_addresses WHERE id = :id")
    suspend fun deleteAddress(id: String)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders_history WHERE userId = :userId ORDER BY timestamp DESC")
    fun getOrdersForUser(userId: String): Flow<List<PastOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: PastOrderEntity)
}

@Dao
interface FavoriteVendorDao {
    @Query("SELECT * FROM favorite_vendors WHERE userId = :userId ORDER BY addedAt DESC")
    fun getFavoritesForUser(userId: String): Flow<List<FavoriteVendorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteVendorEntity)

    @Query("DELETE FROM favorite_vendors WHERE userId = :userId AND vendorId = :vendorId")
    suspend fun deleteFavorite(userId: String, vendorId: String)
}
