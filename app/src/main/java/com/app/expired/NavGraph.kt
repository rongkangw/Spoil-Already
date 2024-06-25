package com.app.expired

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.expired.views.home.HomeScreen
import com.app.expired.views.settings.SettingsScreen

@Composable
fun Navigation(viewmodel: MainViewModel){
    val navController = rememberNavController()
    viewmodel.getOutputList()
    NavHost(navController = navController, startDestination = NavRoute.Main.rout){

        composable(NavRoute.Main.rout){
            viewmodel.getOutputList()
            HomeScreen(navController, viewmodel)
        }

        composable(NavRoute.Settings.rout){
            SettingsScreen(navController, viewmodel)
        }
    }
}