package com.tfg.loginsignupfirebasecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.tfg.loginsignupfirebasecompose.interfaces.FirebaseComposeScreen
import com.tfg.loginsignupfirebasecompose.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           /* App*//*Theme {
                Column(modifier = Modifier.fillMaxSize()) {  ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(32.dp),
                        fontSize = 32
                    )
                    OutlinedButton (onClick = { throw RuntimeException("Test Crash") }) {
                        Text(text = "Test Crash")
                    }
                }
            }*/
            AppNavigation()
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, fontSize: Int) {
    Text(
        text = "Hello $name!",
        modifier = modifier.clickable { throw RuntimeException("Test Crash") },
        fontSize = fontSize.sp
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Gilipollas",modifier = Modifier.padding(32.dp), fontSize = 32)
    }
}