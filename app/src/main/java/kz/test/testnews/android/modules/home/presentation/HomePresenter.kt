package kz.test.testnews.android.modules.home.presentation

import io.reactivex.functions.Action
import kz.test.testnews.android.api.ApiManager
import kz.testwhether.android.modules.base.presentation.BasePresenter
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class HomePresenter(private var router: Router): BasePresenter<HomeView>(){
}