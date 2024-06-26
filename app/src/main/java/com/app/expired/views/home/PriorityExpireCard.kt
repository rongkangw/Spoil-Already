package com.app.expired.views.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.expired.R
import com.app.expired.currentDate
import com.app.expired.database.Item
import com.app.expired.daysFromCurrentDate
import com.app.expired.defaultShape
import com.app.expired.ui.theme.Gray
import com.app.expired.ui.theme.Green
import com.app.expired.ui.theme.Red
import com.app.expired.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityExpireCard(
    item: Item,
    scope: CoroutineScope,
    onRemove: () -> Unit,
    onEdit: () -> Unit
){
    val expiryColor = daysFromCurrentDate(item.expiryDate)
    val onClickExpand = remember { mutableStateOf(false) }
    val swipeState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        confirmValueChange = { state ->
            when (state) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    scope.launch { onEdit() }
                    false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    scope.launch { onRemove() }
                    true
                }
                else -> {
                    false
                }
            }
        },
        positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    )
    SwipeToDismissBox(
        state = swipeState,
        modifier = Modifier
            .fillMaxWidth(),
        backgroundContent = {
        val color by animateColorAsState(
            when(swipeState.targetValue){
                SwipeToDismissBoxValue.Settled -> Gray
                SwipeToDismissBoxValue.StartToEnd -> Green
                SwipeToDismissBoxValue.EndToStart -> Red
            }, label = "Change color"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(defaultShape)
                .background(color),
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Item"
                )

                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Item",
                    tint = White
                )
            }

        }
    }
    ) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
            shape = defaultShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary),
            onClick = { onClickExpand.value = !onClickExpand.value  }
        ) {
            Column (Modifier.padding(25.dp)) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Column {
                        Row {
                            val rowTextSize = 18.sp

                            Text(
                                text = "Expires in ",
                                fontSize = rowTextSize,
                            )
                            Text(
                                modifier = Modifier.offset(y = (-15).dp),
                                text = ChronoUnit.DAYS.between(currentDate, item.expiryDate).toString() ,
                                fontSize = 2*rowTextSize,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = " days",
                                fontSize = rowTextSize,
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(text = item.name,
                            fontSize = 25.sp
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(text = item.expiryDate.toString(),
                            color = expiryColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    AsyncImage(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Gray),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(File(LocalContext.current.filesDir, item.imageLink))
                            .build(),
                        placeholder = painterResource(R.drawable.addphoto),
                        fallback = painterResource(R.drawable.addphoto),
                        error = painterResource(R.drawable.addphoto)    ,
                        contentDescription = "Product Image",
                        contentScale = ContentScale.Fit
                    )
                }
                
                if (onClickExpand.value){
                    Text(text = item.description)
                }
            }
            
        }
    }
}

@Composable
fun EmptyPriorityExpireCard(showDialog : () -> Unit){
    Card(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.33f)
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        shape = defaultShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary),
        onClick = { /*TODO*/ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){

                Text(
                    text = "Theres nothing here...",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(5.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    onClick = { showDialog() }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add an item")
                    Text(text = "ADD ITEM")
                }
            }
        }
    }
}