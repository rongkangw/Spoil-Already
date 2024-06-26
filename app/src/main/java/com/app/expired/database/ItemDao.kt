package com.app.expired.database

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ItemDao {

    @Upsert
    suspend fun addItem(item: Item)

    @Query("UPDATE itemstable" +
            " SET name = :newName, expiryDate = :newExpiry, description = :desc, imageLink = :imageLink " +
            "WHERE name = :name and expiryDate = :expiry")
    suspend fun editItem(
        newName: String,
        newExpiry: LocalDate,
        desc: String,
        imageLink: String,
        name: String,
        expiry: LocalDate
    )

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("SELECT * FROM itemstable")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM itemstable ORDER BY expiryDate asc")
    fun getOrderedItems(): Flow<List<Item>>
}