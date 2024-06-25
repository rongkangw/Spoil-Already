package com.app.expired.views.home

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.app.expired.MainViewModel
import com.app.expired.NavRoute
import com.app.expired.currentDateDisplay
import com.app.expired.database.Item
import com.app.expired.daysFromCurrentDate
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel){

    LaunchedEffect(true, block = { viewModel.getOutputList() })

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val showAddDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }

    var deletedItem: Item
    val editItem = remember { mutableStateOf(Item("", LocalDate.now(), "", Uri.EMPTY)) }
    var expiryColor: Color
    val output by viewModel.outputList.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog.value = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary
                ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp),
        ) {
            Row {
                Column {
                    Text(
                        text = "Welcome, User!",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(text = "It is $currentDateDisplay",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.tertiary)
                }

                Spacer(Modifier.weight(1f))

                IconButton(onClick = { navController.navigate(NavRoute.Settings.rout) }) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Additional Options"
                    )
                }
            }

            Spacer(Modifier.height(15.dp))

            //if no items in database
            if (output.isEmpty()){
                EmptyPriorityExpireCard(showDialog = { showAddDialog.value = true })
            }
            //else show priority item
            else{
                PriorityExpireCard(
                    item = output.first(),
                    scope = scope,
                    onRemove = {
                        //temporarily copy item in event of undo on deletion
                        deletedItem = Item(
                            output.first().name,
                            output.first().expiryDate,
                            output.first().description,
                            output.first().imageLink)

                        viewModel.deleteItem(
                            output.first().name,
                            output.first().expiryDate,
                            output.first().description,
                            output.first().imageLink
                        )

                        scope.launch{
                            val result = snackBarHostState.showSnackbar(
                                message = "Deleted",
                                actionLabel = "Undo",
                                withDismissAction = true,
                                duration = SnackbarDuration.Long
                            )
                            //add back deleted item
                            if (result == SnackbarResult.ActionPerformed){
                                viewModel.addItem(
                                    deletedItem.name,
                                    deletedItem.dateFormatted,
                                    deletedItem.description,
                                    deletedItem.imageLink
                                )
                            }
                        }
                        viewModel.getOutputList()
                    },
                    onEdit = {
                        editItem.value = output.first()
                        showEditDialog.value = true

                    }
                )
            }

            Spacer(Modifier.height(15.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                items(
                    if (output.isEmpty()){
                        output
                    } else {
                        output.subList(1,output.size)
                    }
                ){ item ->
                    expiryColor = daysFromCurrentDate(item.expiryDate)
                    OtherItemsCard(
                        item.name,
                        item.dateFormatted,
                        item.imageLink,
                        item.description,
                        expiryColor = expiryColor,
                        scope = scope,
                        onRemove = {
                            viewModel.deleteItem(
                                item.name,
                                item.expiryDate,
                                item.description,
                                item.imageLink
                            )
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = "Deleted",
                                    actionLabel = "Undo",
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Long
                                )

                                //add back deleted item on Undo
                                if (result == SnackbarResult.ActionPerformed){
                                    viewModel.addItem(
                                        item.name,
                                        item.dateFormatted,
                                        item.description,
                                        item.imageLink
                                    )
                                }
                            }
                            viewModel.getOutputList()
                        },
                        onEdit = {
                            editItem.value = item
                            showEditDialog.value = true
                        }
                    )

                }
            }
        }
    }

    if (showAddDialog.value){
        AddItemDialog(
            onDismiss = {
                showAddDialog.value = false
            },
            onAdd = {
                scope.launch {
                    snackBarHostState.showSnackbar(
                    message = "Item added",
                    duration = SnackbarDuration.Short
                )
                }
                showAddDialog.value = false
                    },
            viewModel = viewModel
        )
    }
    if (showEditDialog.value){
        EditItemDialog(
            item = editItem.value,
            onDismiss = {
                showEditDialog.value = false
            },
            onEdit = {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = "Item edited",
                        duration = SnackbarDuration.Short
                    )
                }
                showEditDialog.value = false
            },
            viewModel = viewModel
        )
    }
}

