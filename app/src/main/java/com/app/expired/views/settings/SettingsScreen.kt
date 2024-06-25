package com.app.expired.views.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.expired.MainViewModel
import com.app.expired.NavRoute
import com.app.expired.ui.theme.Black
import com.app.expired.ui.theme.Green
import com.app.expired.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: MainViewModel){
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(NavRoute.Main.rout) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(
                        text = "Settings"
                    )
                },
                actions = { 
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp))
        {
            ThemeToggle(
                modifier = Modifier,
                toggle = { /*TODO*/ }
            )

            SettingRowToggle(
                modifier = Modifier,
                settingName = "Notifications",
                toggle = { /*TODO*/ }
            )
        }

    }
}

@Composable
fun SettingRowToggle(modifier: Modifier, settingName: String, toggle: () -> Unit){
        Row(modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.07f)
            .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = settingName,
                fontSize = 27.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.weight(0.1f))

            var checked by remember { mutableStateOf(true) }
            Switch(
                checked = checked ,
                onCheckedChange = {
                    checked = it
                    toggle()
                                  },
                thumbContent = if (checked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
                colors = SwitchDefaults.colors(
                    checkedIconColor = Green,
                    checkedThumbColor = MaterialTheme.colorScheme.background,
                    checkedTrackColor = Green,
                    uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.background,
                    uncheckedBorderColor = MaterialTheme.colorScheme.primary) )

        }
    }

@Composable
fun ThemeToggle(modifier: Modifier, toggle: () -> Unit){

    var checked by remember { mutableStateOf(true) }
    val colorList = listOf(White, Black)

    Row(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(0.07f)
        .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Text(
            text = "Theme",
            fontSize = 27.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.weight(0.1f))

        Button(modifier = Modifier
            .size(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorList[checked.compareTo(false)]),
            onClick = {
                checked = !checked
                toggle()
                      },
            shape = CircleShape,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)){}
    }
}