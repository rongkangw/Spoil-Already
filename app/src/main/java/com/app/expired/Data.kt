package com.app.expired

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import com.app.expired.ui.theme.Green
import com.app.expired.ui.theme.Red
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

//Defaults
val defaultShape = RoundedCornerShape(15)

//Items object
data class Items(
    var name: String = "",
    var expiry: String = "",
    var desc: String = "",
    val imageLink: String = "",
)

val test = listOf(
    Items("Milk", "05-05-2024"),
    Items("Cherries", "06-05-2024"),
    Items("Crackers", "08-01-2025"),
)

//Navigation Routes
sealed class NavRoute(val rout: String) {
    data object Main : NavRoute("Main")
    data object Settings : NavRoute("Settings")
}

//Date Methods and Variables
var currentDate: LocalDate = LocalDate.now()
val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
var currentDateDisplay: String = currentDate.format(DateTimeFormatter.ofPattern("d MMM uuuu"))


fun daysFromCurrentDate(date: LocalDate): Color {
    return if (ChronoUnit.DAYS.between(currentDate, date) >= 30){
        Green
    } else{
        Red
    }
}