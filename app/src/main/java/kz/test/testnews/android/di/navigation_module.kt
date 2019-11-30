package kz.test.testnews.android.di

import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

val navigationModule = module{
    single { provideRouter() }
    single { provideNavigationHolder() }
}
private val cicerone: Cicerone<Router> = Cicerone.create()

private fun provideRouter(): Router =
    cicerone.router

private fun provideNavigationHolder(): NavigatorHolder =
    cicerone.navigatorHolder

//private val cicerone: Cicerone<Router> = Cicerone.create()
//
//@Provides
//@Singleton
//internal fun provideRouter(): Router {
//    return cicerone.router
//}
//
//@Provides
//@Singleton
//internal fun provideNavigatorHolder(): NavigatorHolder {
//    return cicerone.navigatorHolder
//}