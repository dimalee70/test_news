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
class BestPresenter(private val router: Router, private val client: ApiManager): BasePresenter<NewsView>(), BackButtonListener {

    var isLoading = ObservableBoolean()

    var liveBestResponse = MutableLiveData<List<New>>()

    var listBest = mutableListOf<New>()

    fun getBests(){
        disposables.add(client.getBests()
            .subscribeOn(background)
            .observeOn(mainThread)
            .doFinally {
                viewState.hideProgress()
            }
            .subscribe({
                    result ->
                run {
                    liveBestResponse.postValue(null)
                    for (i in Constants.FROM_NEWS..Constants.TO_NEWS){
                        if (i == Constants.TO_NEWS) {
                            getBest(result.get(i), true)
                            return@subscribe
                        }
                        getBest(result.get(i), false)
                    } }

            },{
                    error ->
                run {
                    viewState.showError(error)
                }
            }))
    }

    fun openWebView(url: String?){
        if (url != null) {
            router.navigateTo(Screens.WebScreen(url))
        }
    }

    @SuppressLint("CheckResult")
    fun getBest(id: Int?, islast: Boolean){
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
                            listBest.add(result)
                        if (islast){
                            liveBestResponse.value = listBest
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
        getBests()
//        productsDataSourceFactory.productsDataSourceLiveData.value?.invalidate()
//        sync()
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun observeNewResponseBoundary(): MutableLiveData<List<New>> {
        return liveBestResponse
    }
}