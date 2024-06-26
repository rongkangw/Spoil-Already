package com.app.expired.views.home

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.expired.R
import com.app.expired.defaultShape
import com.app.expired.ui.theme.Black
import com.app.expired.ui.theme.DarkGray
import com.app.expired.ui.theme.Gray
import com.app.expired.ui.theme.Green
import com.app.expired.ui.theme.Red
import com.app.expired.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherItemsCard(
    name: String,
    expiry: String,
    imageLink: String,
    desc: String,
    expiryColor: Color,
    scope: CoroutineScope,
    onRemove: () -> Unit,
    onEdit: () -> Unit
){

    val onClickExpand = remember { mutableStateOf(false) }
    val swipeState = rememberSwipeToDismissBoxState(
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
        backgroundContent = {
        val color by animateColorAsState(
            when(swipeState.targetValue){
                SwipeToDismissBoxValue.Settled -> Gray
                SwipeToDismissBoxValue.StartToEnd -> Green
                SwipeToDismissBoxValue.EndToStart -> Red
            }, label = "Change color"
        )
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(defaultShape)
                    .background(color)
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
            .fillMaxWidth(),
            shape = defaultShape,
            border = BorderStroke(1.dp, White),
            colors = CardDefaults.cardColors(
                containerColor = Black,
                contentColor = White),
            onClick = { onClickExpand.value = !onClickExpand.value }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp, horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = name)

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = expiry,
                        color = expiryColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    )

                    ExpandedOtherItemsCard(
                        isExpanded = onClickExpand.value,
                        desc = desc
                    )
                }

                Spacer(Modifier.weight(1f))

                println(name + "_" + expiry + ".jpg")
                AsyncImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DarkGray),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(File(LocalContext.current.filesDir, imageLink))
                        .build(),
                    contentDescription = "Product Image",
                    placeholder = painterResource(id = R.drawable.addphoto),
                    error = painterResource(id = R.drawable.addphoto),
                    fallback = painterResource(id = R.drawable.addphoto)
                )
            }
        }
    }
}

@Composable
fun ExpandedOtherItemsCard(isExpanded: Boolean, desc: String){
    val animateTime = 300

    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(animateTime)
        ) + fadeIn(initialAlpha = .3f, animationSpec = tween(animateTime))
    }

    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(animateTime)
        ) + fadeOut(targetAlpha = 0.3f, animationSpec = tween(animateTime))
    }

    AnimatedVisibility(
        modifier = Modifier.padding(top = 10.dp),
        visible = isExpanded,
        enter = expandTransition,
        exit = exitTransition)
    {
        Spacer(Modifier.height(15.dp))
        Text(text = desc, textAlign = TextAlign.Justify)
    }
}