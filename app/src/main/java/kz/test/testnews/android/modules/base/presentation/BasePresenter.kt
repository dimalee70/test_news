package kz.testwhether.android.modules.base.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kz.test.testnews.android.modules.base.presentation.BaseView
import moxy.MvpPresenter

/**
 * Created by wokrey@gmail.com on 7/4/19.
 * It's not wokrey, if the code smells bad. Somebody set me up.
 */

abstract class BasePresenter<T: BaseView>(
    val disposables: CompositeDisposable = CompositeDisposable(),
    val background: Scheduler = Schedulers.io(),
    val mainThread: Scheduler = AndroidSchedulers.mainThread()
): MvpPresenter<T>(), LifecycleObserver {

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    fun attachLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    fun detachLifecycle(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun onPresenterDestroy() {
        disposables.clear()
    }
}