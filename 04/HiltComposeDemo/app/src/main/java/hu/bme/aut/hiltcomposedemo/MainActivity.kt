package hu.bme.aut.hiltcomposedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.hiltcomposedemo.di.Exchange2022Average
import hu.bme.aut.hiltcomposedemo.di.Exchange2023Average
import hu.bme.aut.hiltcomposedemo.finance.ExchangeRateCalculator
import hu.bme.aut.hiltcomposedemo.ui.screen.ExchangeRateScreen
import hu.bme.aut.hiltcomposedemo.ui.theme.HiltComposeDemoTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /*
    @Exchange2023Average
    @Inject
    lateinit var exchangeRateCalculator: ExchangeRateCalculator
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiltComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExchangeRateScreen()
                }
            }
        }
    }
}
