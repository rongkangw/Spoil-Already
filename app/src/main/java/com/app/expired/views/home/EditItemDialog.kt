package com.app.expired.views.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.expired.MainViewModel
import com.app.expired.R
import com.app.expired.database.Item
import com.app.expired.ui.theme.Black
import com.app.expired.ui.theme.Gray
import com.app.expired.ui.theme.White
import java.io.File

@Composable
fun EditItemDialog(item: Item, onEdit: () -> Unit, onDismiss: () -> Unit, viewModel: MainViewModel){
    var name by remember { mutableStateOf(item.name) }
    var expiry by remember { mutableStateOf(item.dateFormatted) }
    var desc by remember { mutableStateOf(item.description) }
    var newImageUri by remember { mutableStateOf(Uri.EMPTY) }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
                uri ->
            if (uri != null) {
                newImageUri = uri
            }
            else{
                println("FAIL image")
            }
        }
    )

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .fillMaxHeight(0.7f),
            shape = RoundedCornerShape(50.dp),
            colors = CardDefaults.cardColors(Gray))
        {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {

                Text(text = "Edit Item",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )

                Spacer(Modifier.height(5.dp))

                AsyncImage(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(White.copy(alpha = 0.3f))
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                    model = if (newImageUri != Uri.EMPTY) {
                        newImageUri
                    }
                    else {
                        ImageRequest.Builder(LocalContext.current)
                        .data(File(LocalContext.current.filesDir, item.imageLink))
                        .build()
                         },
                    placeholder = painterResource(id = R.drawable.addphoto),
                    error = painterResource(id = R.drawable.addphoto),
                    fallback = painterResource(id = R.drawable.addphoto),
                    contentScale = ContentScale.Inside,
                    contentDescription = "Add Photo"
                )

                Spacer(Modifier.height(5.dp))

                //Product Name
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Black,
                        unfocusedTextColor = Black,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    ),
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Name") })

                //Product Expiry
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Black,
                        unfocusedTextColor = Black,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    ),
                    value = expiry,
                    onValueChange = { expiry = it },
                    placeholder = { Text("Expiry") })

                //Product Desc
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .clip(RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Black,
                        unfocusedTextColor = Black,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    ),
                    value = desc,
                    onValueChange = { desc = it },
                    placeholder = { Text("Brief Description of Product") })

                Spacer(Modifier.height(10.dp))

                Row(modifier = Modifier.padding(horizontal = 50.dp)) {

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = Black
                        ),
                        onClick = { onDismiss() })
                    {
                        Text(text = "Cancel")
                    }

                    Spacer(Modifier.weight(1f))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = Black
                        ),
                        onClick = {
                            if (newImageUri != null){
                                viewModel.saveImage(context, newImageUri, "$name-$expiry")
                                viewModel.editItem(
                                    newName = name,
                                    newExpiry = expiry,
                                    desc = desc,
                                    imageLink = "$name-$expiry.jpg",
                                    name = item.name,
                                    expiry = item.expiryDate
                                )
                            }
                            else{
                                viewModel.editItem(
                                    newName = name,
                                    newExpiry = expiry,
                                    desc = desc,
                                    imageLink = item.imageLink,
                                    name = item.name,
                                    expiry = item.expiryDate
                                )
                            }

                            viewModel.getOutputList()
                            onEdit()
                        })
                    {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

