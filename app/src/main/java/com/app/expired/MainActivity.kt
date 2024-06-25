package com.app.expired

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.expired.database.ItemDatabase
import com.app.expired.database.ItemRepository
import com.app.expired.ui.theme.ExpiredTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        ItemDatabase.getDatabase(context = applicationContext)
    }

    //Init viewModel
    @Suppress("UNCHECKED_CAST")
    private val mainViewModel by viewModels<MainViewModel>( factoryProducer = {object: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(ItemRepository(db)) as T
        }
    }})





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpiredTheme {
                //initialisations(mainViewModel)
                Navigation(mainViewModel)

            }
        }
    }
}

fun initialisations(viewModel: MainViewModel){
    //for(i in test.indices){
    //    viewModel.addItem(test[i].name, test[i].expiry, test[i].desc, test[i].imageLink)
    //}
}
