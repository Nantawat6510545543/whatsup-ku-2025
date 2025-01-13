package org.classapp.whatsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.classapp.whatsup.ui.theme.WhatsUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            WhatsUpTheme {
                Surface (modifier = Modifier.wrapContentSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Greeting(
                        name = "Android"
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Click me")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhatsUpTheme {
        Greeting("Android")
    }
}