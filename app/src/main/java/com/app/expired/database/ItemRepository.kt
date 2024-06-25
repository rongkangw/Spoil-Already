package com.app.expired.database

import android.net.Uri
import java.time.LocalDate

class ItemRepository(private val db : ItemDatabase) {

    suspend fun addItem(item: Item){
        db.dao.addItem(item)
    }

    suspend fun deleteItem(item: Item){
        db.dao.deleteItem(item)
    }

    suspend fun editItem(
        newName: String,
        newExpiry: LocalDate,
        desc: String,
        imageLink: Uri,
        name: String,
        expiry: LocalDate
    ){
        db.dao.editItem(
            newName,
            newExpiry,
            desc,
            imageLink,
            name,
            expiry
        )
    }

    fun getOrderedItems() = db.dao.getOrderedItems()
}