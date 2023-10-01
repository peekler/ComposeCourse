package hu.bme.aut.hiltcomposedemo.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.hiltcomposedemo.di.Exchange2023Average
import hu.bme.aut.hiltcomposedemo.finance.ExchangeRateCalculator
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel
@Inject constructor(
    @Exchange2023Average var exchangeRateCalculator: ExchangeRateCalculator
) : ViewModel() {

    fun hufToEur(hufAmount: Double) : Double {
        return exchangeRateCalculator.hufToEur(hufAmount)
    }

}