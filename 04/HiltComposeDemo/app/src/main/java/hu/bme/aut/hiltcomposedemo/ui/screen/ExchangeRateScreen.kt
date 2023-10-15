package hu.bme.aut.hiltcomposedemo.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.hiltcomposedemo.finance.ExchangeRateCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateScreen(
    exchangeRateViewModel: ExchangeRateViewModel = hiltViewModel()
) {

    var hufText by rememberSaveable {
        mutableStateOf("400")
    }
    var textResult by rememberSaveable {
        mutableStateOf("-")
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Enter HUF amont" ) },
            value = "$hufText",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = {
                hufText = it
            },
        )
        Button(
            onClick = {
                // set textResult here...
                textResult = exchangeRateViewModel.hufToEur(hufText.toDouble()).toString()
                //textResult = exchangeRateCalculator.hufToEur(hufText.toDouble()).toString()
            }

        ) {
            Text(text = "HufToEur")
        }
        Text(
            text = textResult,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic)

    }

}