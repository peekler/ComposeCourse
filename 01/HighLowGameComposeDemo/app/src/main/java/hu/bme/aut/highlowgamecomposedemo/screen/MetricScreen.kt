package hu.bme.aut.highlowgamecomposedemo.screen

import android.widget.RadioGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.highlowgamecomposedemo.R
import java.util.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricScreen() {
    val context = LocalContext.current

    var numberText by rememberSaveable {
        mutableStateOf("1")
    }
    var textResult by rememberSaveable {
        mutableStateOf("0.6214")
    }
    var inputErrorState by rememberSaveable {
        mutableStateOf(false)
    }

    fun validate(text: String) {
        val allDigit = text.all { char -> char.isDigit() }

        textResult = context.getString(R.string.error_empty)
        inputErrorState = !allDigit
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Value") },
            value = "$numberText",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = {
                numberText = it
                validate(numberText)
            },
            isError = inputErrorState,
            trailingIcon = {
                if (inputErrorState)
                    Icon(
                        Icons.Filled.Warning,
                        "error", tint = MaterialTheme.colorScheme.error)
            }
        )
        Row {
            Button(
                onClick = {

                }
            ) {
                Text(text = "km -> miles")
            }
            Button(
                onClick = {

                }
            ) {
                Text(text = "km -> yard")
            }
            Button(
                onClick = {

                }
            ) {
                Text(text = "km -> foot")
            }
        }

        Text(
            text = textResult,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color.Blue
        )
    }
}


