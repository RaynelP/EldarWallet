package com.raynel.eldarwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.raynel.eldarwallet.model.AutheticationRepoImpl
import com.raynel.eldarwallet.ui.theme.EldarWalletTheme
import com.raynel.eldarwallet.view.MyApp
import com.raynel.eldarwallet.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val emailUser = AutheticationRepoImpl.init(applicationContext).getString("email", null)

        setContent {
            EldarWalletTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   MyApp(emailUser)
                }
            }
        }
    }


}
