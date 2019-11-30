package kz.test.testnews.android.modules.main.presentation

import kz.test.testnews.android.utils.Screens
import kz.testwhether.android.modules.main.presentation.MainAppView
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class MainAppPresenter(private  val router: Router): MvpPresenter<MainAppView>(){

    init {
        println("Hello")
    }

    fun auth(){
        println("auth")
    }

    fun gotoHome(){
        router.newRootScreen(Screens.HomeScreen())
    }
}