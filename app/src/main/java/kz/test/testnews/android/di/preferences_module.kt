package kz.test.testnews.android.di

import android.content.Context
import android.content.SharedPreferences
import kz.test.testnews.android.Application
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val prefererncesModule = module{
    single { provideSettingsPreferences(androidApplication() as Application) }
}

private const val  PREFERENCES_FILE_KEY = "kz.test.testnews.settings_preferences"

private  fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)