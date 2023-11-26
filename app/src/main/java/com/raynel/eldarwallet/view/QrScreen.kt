package com.raynel.eldarwallet.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.raynel.eldarwallet.viewmodel.QRViewModel

@Composable
fun QRScreen(
    navController: NavController,
    onBackPressed: () -> Unit,
    name: String,
    lastName: String
) {

    val viewModel: QRViewModel = viewModel(
        factory = QRViewModel.QrViewModelFactory(name, lastName)
    )

    val bit by viewModel.image.collectAsState(null)
    if(bit != null){
        QRScreen(
            bitmap = bit!!,
            onBackPressed = onBackPressed
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScreen(bitmap: Bitmap, onBackPressed: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary,),
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Botón de retroceso")
                    }

                }
            )
        },

        content = {

           Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center){
               Image(
                   modifier = Modifier.fillMaxWidth().height(300.dp),
                   bitmap = bitmap.asImageBitmap(),
                   contentDescription = "some useful description",
                   contentScale = ContentScale.Crop
               )
           }

        }
    )

}



