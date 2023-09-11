package hu.bme.aut.comosedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.comosedemo.ui.theme.ComoseDemoTheme
import java.util.Date
import kotlin.math.pow

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            ComoseDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    BodyWeightView()

                }
            }
        }
    }
}


@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var currentTime by rememberSaveable {
        mutableStateOf("-")
    }

    Column {
        Text(
            text = "Time: $currentTime"
        )

        Button(
            onClick = {
                currentTime = Date(System.currentTimeMillis()).toString()
            }
        ) {
            Text(text = "Show time")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComoseDemoTheme {
        Greeting()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyWeightView() {
    var BMIindex by remember { mutableStateOf(0.0) }
    var height by remember { mutableStateOf(0.0) }
    var weight by remember { mutableStateOf(0.0) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            label = {Text("Height in meters (e.g. 1.8):")},
            value = height.toString(),
            onValueChange = { newValue ->
                height = newValue.toDoubleOrNull() ?: 0.0
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            label = {Text("Weight in kg:")},
            value = weight.toString(),
            onValueChange = { newValue ->
                weight = newValue.toDoubleOrNull() ?: 0.0
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                if (height <= 0 || weight <= 0) {
                    showError = true
                } else {
                    showError = false
                    BMIindex = weight / (height.pow(2))
                }
            }
        ) {
            Text("Show ideal weight")
        }

        Text("BMI index: $BMIindex")
        if (showError) {
            Text(
                text = "Error in the inputs",
                color = Color.Red
            )
        } else {
            Text(
                """
                <18.5: underweight
                18.5 – 24.9: normal
                25<: overweight
                """
            )
        }
    }
}

@Composable
fun PreviewBodyWeightView() {
    BodyWeightView()
}






