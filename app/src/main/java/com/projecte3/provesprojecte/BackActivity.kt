package com.projecte3.provesprojecte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme

class BackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvesProjecte3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2("User")
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    var calGoBack by remember { mutableStateOf(0) }
    if(calGoBack == 1){
        calGoBack = 0

        val activity = (LocalLifecycleOwner.current as ComponentActivity)
        activity.finish()
        return
    }
    Column{
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Button(onClick = {calGoBack = 1}
        ) {
            Text(text = "go back", fontSize = 24.sp)


        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ProvesProjecte3Theme {
        Greeting2("Android")
    }
}