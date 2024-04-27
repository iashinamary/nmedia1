package ru.netology.nmedia.api

import android.app.Application
import android.content.Context
import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GoogleApiAvailabilityModule {
    @Provides
    fun provideGoogleApiAvailability(

    ): GoogleApiAvailability =
        GoogleApiAvailability.getInstance()
}