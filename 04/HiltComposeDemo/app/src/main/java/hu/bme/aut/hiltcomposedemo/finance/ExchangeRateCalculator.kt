package hu.bme.aut.hiltcomposedemo.finance

import javax.inject.Inject

class ExchangeRateCalculator constructor(
    private var hufRates: HufRates = HufRates()
) {


    fun eurToHuf(amountOfEur: Double): Double {
        return amountOfEur * hufRates.EUR_TO_HUF
    }

    fun hufToEur(amountOfHuf: Double): Double {
        return amountOfHuf / hufRates.EUR_TO_HUF
    }

}

data class HufRates(
    var EUR_TO_HUF: Double = 389.5,
    var USD_TO_HUF: Double = 368.3,
    var GBP_TO_HUF: Double = 449.4
)