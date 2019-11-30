package kz.test.testnews.android.modules.home.presentation

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import kz.test.testnews.android.custom.BackButtonListener
import kz.testwhether.android.modules.base.domain.BaseItem
import kz.testwhether.android.modules.base.presentation.BasePresenter
import moxy.InjectViewState
import ru.terrakok.cicerone.Router

@InjectViewState
class HomeMainPresenter(private var router: Router): BasePresenter<HomeMainView>(),
    BackButtonListener {

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
//    var liveTemperatureResponse = MutableLiveData<WeatherResponse>()
//
//    var isLoading = ObservableBoolean()
//
//    fun getWhether(latitude: Double?, longitude: Double?){
//        disposables.add(client.getWheatherByCoord(latitude, longitude, Constants.WEATHER_API_KEY)
//            .subscribeOn(background)
//            .observeOn(mainThread)
//            .doFinally {
//                viewState.hideProgress()
//            }
//            .subscribe({
//                    result ->
//                run {
//                    liveTemperatureResponse.postValue(null)
//                    liveTemperatureResponse.value = result
//                    isLoading.set(false)
//                }
//            }, {
//                    error ->
//                run {
//                    viewState.showError(error)
//                    isLoading.set(false)
//                }
//            })
//        )
//    }
//
//    fun reloadData() = reloadData(showRefresh = true)
//
//    fun reloadData(showRefresh: Boolean) {
//        isLoading.set(showRefresh)
//        viewState.initView()
////        productsDataSourceFactory.productsDataSourceLiveData.value?.invalidate()
////        sync()
//    }
//
//
//    fun observeTemperatureResponseBoundary(): MutableLiveData<WeatherResponse> {
//        return liveTemperatureResponse
//    }
}