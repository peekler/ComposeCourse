package hu.bme.aut.hiltcomposedemo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import hu.bme.aut.hiltcomposedemo.finance.ExchangeRateCalculator
import hu.bme.aut.hiltcomposedemo.finance.HufRates
import javax.inject.Qualifier
import javax.inject.Singleton

/*
@Module
@InstallIn(ActivityComponent::class)
object FinanceModule {

    @Provides
    fun provideExchangeRateCalculator() : ExchangeRateCalculator {
        return ExchangeRateCalculator(HufRates())
    }
}
*/

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Exchange2022Average

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Exchange2023Average

@Module
//@InstallIn(ActivityComponent::class)
@InstallIn(ViewModelComponent::class)
object FinanceModule {

    @Exchange2022Average
    @Provides
    fun provideExchangeRateCalculator2022() : ExchangeRateCalculator {
        return ExchangeRateCalculator(
            HufRates(EUR_TO_HUF = 360.2, USD_TO_HUF = 310.1, GBP_TO_HUF = 423.3)
        )
    }

    @Exchange2023Average
    @Provides
    fun provideExchangeRateCalculator2023() : ExchangeRateCalculator {
        return ExchangeRateCalculator(
            HufRates()
        )
    }
}