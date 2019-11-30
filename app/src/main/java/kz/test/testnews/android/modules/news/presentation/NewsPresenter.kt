package kz.test.testnews.android.modules.news.presentation

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kz.test.testnews.android.api.ApiManager
import kz.test.testnews.android.api.response.New
import kz.test.testnews.android.custom.BackButtonListener
import kz.test.testnews.android.utils.Constants
import kz.test.testnews.android.utils.Screens
import kz.testwhether.android.modules.base.presentation.BasePresenter
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import java.util.concurrent.Executors

@InjectViewState
class NewsPresenter(private val router: Router, private val client: ApiManager): BasePresenter<NewsView>(), BackButtonListener {

    var isLoading = ObservableBoolean()

    var liveNewResponse = MutableLiveData<List<New>>()

    var listNew = mutableListOf<New>()

    fun getNews(){
        disposables.add(client.getNews()
            .subscribeOn(background)
            .observeOn(mainThread)
            .doFinally {
                viewState.hideProgress()
            }
            .subscribe({
                result ->
                    run {
                    liveNewResponse.postValue(null)
                    for (i in Constants.FROM_NEWS..Constants.TO_NEWS){
                        if (i == Constants.TO_NEWS) {
                            getNew(result.get(i), true)
                            return@subscribe
                        }
                        getNew(result.get(i), false)
                    } }

            },{
                error ->
                run {
                    viewState.showError(error)
                }
            }))
    }

    fun openWebView(url: String?){
        if (url != null)
            router.navigateTo(Screens.WebScreen(url))
    }

    @SuppressLint("CheckResult")
    fun getNew(id: Int?, islast: Boolean){
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
                        listNew.add(result)
                        if (islast){
                            liveNewResponse.value = listNew
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
        getNews()
//        productsDataSourceFactory.productsDataSourceLiveData.value?.invalidate()
//        sync()
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun observeNewResponseBoundary(): MutableLiveData<List<New>> {
        return liveNewResponse
    }
}