package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, AddressEntity::class, PastOrderEntity::class, FavoriteVendorEntity::class],
    version = 2,
    exportSchema = false
)
abstract class StreetoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun addressDao(): AddressDao
    abstract fun orderDao(): OrderDao
    abstract fun favoriteVendorDao(): FavoriteVendorDao

    companion object {
        @Volatile
        private var INSTANCE: StreetoDatabase? = null

        fun getDatabase(context: Context): StreetoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StreetoDatabase::class.java,
                    "streeto_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
