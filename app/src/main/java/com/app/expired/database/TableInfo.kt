package com.app.expired.database

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import com.app.expired.dtf
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity(tableName = "ItemsTable", primaryKeys = ["name", "expiryDate"])
@Parcelize
data class Item(
    val name: String,
    val expiryDate: LocalDate,
    val description: String,
    val imageLink: String
) : Parcelable{

    val dateFormatted: String
        get() = expiryDate.format(dtf)
}