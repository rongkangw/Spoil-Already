package com.app.expired

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.expired.database.Item
import com.app.expired.database.ItemRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.time.LocalDate

class MainViewModel(private val repository: ItemRepository): ViewModel() {

    private var _outputList = MutableStateFlow(emptyList<Item>())
    var outputList = _outputList.asStateFlow()

    //saved to context.filesDir() in the naming format <name>-<dd-MM-yyyy>.jpg
    fun saveImage(context: Context, imageUri: Uri, fileName: String) {
        if (File(context.filesDir, "$fileName.jpg").exists()){
            try {
                File(context.filesDir, "$fileName.jpg").delete()
            }
            catch (e: IOException){
                Log.d("Viewmodel", "failed to delete")
            }
            catch (e: SecurityException){
                Log.d("Viewmodel", "unable to access image directory")
            }
        }
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val outputStream = context.openFileOutput("$fileName.jpg", MODE_PRIVATE)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    fun getOutputList(){
        viewModelScope.launch(IO) { repository.getOrderedItems().collectLatest{ _outputList.tryEmit(it) } }
    }

    fun addItem(name: String, expiry: String, desc: String, link: String){
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
        imageLink: String,
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

    fun deleteItem(name: String, expiry: LocalDate, desc: String, link: String){
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