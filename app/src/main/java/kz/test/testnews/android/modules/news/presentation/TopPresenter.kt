package kz.test.testnews.android.modules.news.presentation

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import kz.test.testnews.android.api.ApiManager
import kz.test.testnews.android.api.response.New
import kz.test.testnews.android.custom.BackButtonListener
import kz.test.testnews.android.utils.Constants
import kz.test.testnews.android.utils.Screens
import kz.testwhether.android.modules.base.presentation.BasePresenter
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class TopPresenter(private val router: Router, private val client: ApiManager): BasePresenter<NewsView>(),
    BackButtonListener {

    var isLoading = ObservableBoolean()

    var liveTopResponse = MutableLiveData<List<New>>()

    var listTop = mutableListOf<New>()

    fun getTops(){
        disposables.add(client.getTops()
            .subscribeOn(background)
            .observeOn(mainThread)
            .doFinally {
                viewState.hideProgress()
            }
            .subscribe({
                    result ->
                run {
                    liveTopResponse.postValue(null)
                    for (i in Constants.FROM_NEWS..Constants.TO_NEWS){
                        if (i == Constants.TO_NEWS) {
                            getTop(result.get(i), true)
                            return@subscribe
                        }
                        getTop(result.get(i), false)
                    } }

            },{
                    error ->
                run {
                    viewState.showError(error)
                }
            }))
    }

    @SuppressLint("CheckResult")
    fun getTop(id: Int?, islast: Boolean){
        if (id != null) {
            client.getNewById(id)
                .subscribeOn(background)
                .observeOn(mainThread)
                .doFinally {
                    viewState.hideProgress()
                }
                .subscribe({ result ->
                    run {
                        if(result != null)
                            listTop.add(result)
                        if (islast){
                            liveTopResponse.value = listTop
                            isLoading.set(false)

                        }

                    }
                }, { error ->
                    run {
                        if(!(error is NullPointerException))
                            viewState.showError(error)
                        isLoading.set(false)
                    }
                })
//                )
//            )
        }

    }

    fun reloadData() = reloadData(showRefresh = true)

    fun reloadData(showRefresh: Boolean) {
        isLoading.set(showRefresh)
        getTops()
//        productsDataSourceFactory.productsDataSourceLiveData.value?.invalidate()
//        sync()
    }

    fun openWebView(url: String?){
        if (url != null)
            router.navigateTo(Screens.WebScreen(url))
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun observeNewResponseBoundary(): MutableLiveData<List<New>> {
        return liveTopResponse
    }

}