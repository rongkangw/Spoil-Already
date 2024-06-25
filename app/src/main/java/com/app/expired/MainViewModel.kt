package com.app.expired

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.expired.database.Item
import com.app.expired.database.ItemRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel(private val repository: ItemRepository): ViewModel() {

    private val imageThing = Uri.EMPTY
    private var filePath = ""

    private var _outputList = MutableStateFlow(emptyList<Item>())
    var outputList = _outputList.asStateFlow()

    fun saveImage(context: Context, imageUri: Uri) {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val timestamp = currentDate.format(DateTimeFormatter.ofPattern("dd_MM_yyyy"))
        val outputStream = context.openFileOutput("$timestamp.jpg", MODE_PRIVATE)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        filePath = "$timestamp.jpg"
    }

    /*fun getImage(context: Context){
        val outputStream = context.openFileInput(filePath)
        outputStream.use { image ->
            imageThing = image

        }
    }*/

    fun getOutputList(){
        viewModelScope.launch(IO) { repository.getOrderedItems().collectLatest{ _outputList.tryEmit(it) } }
    }

    fun addItem(name: String, expiry: String, desc: String, link: Uri){
        viewModelScope.launch {
            repository.addItem(
                Item(
                    name = name,
                    expiryDate = LocalDate.parse(expiry, dtf),
                    description = desc,
                    imageLink = link
                )
            )
        }
        //println("Item : $name added") (setup a logging process eventually)
    }

    fun editItem(
        newName: String,
        newExpiry: String,
        desc: String,
        imageLink: Uri,
        name: String,
        expiry: LocalDate
    ){
        viewModelScope.launch {
            repository.editItem(
                newName = newName,
                newExpiry = LocalDate.parse(newExpiry, dtf),
                desc = desc,
                imageLink = imageLink,
                name = name,
                expiry = expiry
            )
        }
    }

    fun deleteItem(name: String, expiry: LocalDate, desc: String, link: Uri){
        viewModelScope.launch {
            repository.deleteItem(
                Item(
                    name = name,
                    expiryDate = expiry,
                    description = desc,
                    imageLink = link
                )
            )
        }
        //println("Item : $name deleted") (setup a logging process eventually)
    }


    //SettingsScreen Functions
    
    fun switchTheme(){
        /*TODO*/
    }

    fun switchNotification(){
        /*TODO*/
    }
}