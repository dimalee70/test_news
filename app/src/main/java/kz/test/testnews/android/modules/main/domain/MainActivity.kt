package kz.test.testnews.android.modules.main.domain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import kz.test.testnews.R
import kz.test.testnews.android.extensions.showErrorAlertDialog

import kz.testwhether.android.modules.base.domain.BaseActivity
import kz.test.testnews.android.modules.main.presentation.MainAppPresenter
import kz.testwhether.android.modules.main.presentation.MainAppView
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity: BaseActivity(), MainAppView{

    private val SPLASH_DELAY: Long = 1000

    private val mHandler = Handler()

    private val mLauncher = Launcher()

    private val router: Router by inject()

    private val navigator = SupportAppNavigator(this, -1)

    companion object {
        const val TAG = "MainAppActivity"
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
        var PACKAGE_NAME: String? = null
    }

    @InjectPresenter
    lateinit var mMainAppPresenter: MainAppPresenter


    @ProvidePresenter
    fun providePresenter(): MainAppPresenter
    {
        return MainAppPresenter(router)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        isDynamicThemingOn = false
        isFullScreen = true

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PACKAGE_NAME = applicationContext.packageName
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun showError(exception: Throwable) {
        if (errorDialog == null || (errorDialog != null && !errorDialog!!.isShowing))
        {
            val responseBody = if (exception is HttpException) {
                exception.response().errorBody()?.string()
            } else {
                null
            }
            errorDialog = showErrorAlertDialog({
                cancelable = false
                closeIconClickListener {
                    mMainAppPresenter.auth()
                }
            }, getNetworkErrorTitle(exception, responseBody), null)
            errorDialog?.show()
        }
    }

    override fun onStart() {
        super.onStart()

        mHandler.postDelayed(mLauncher, SPLASH_DELAY)
    }

    override fun onStop() {
        mHandler.removeCallbacks(mLauncher)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun launch()
    {
        if (!isFinishing)
        {
            mMainAppPresenter.gotoHome()
        }
    }

    private fun unbindDrawables(view: View) {
        if (view.background != null) {
            view.background.callback = null
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                unbindDrawables(view.getChildAt(i))
            }
            view.removeAllViews()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindDrawables(findViewById(R.id.root))
        System.gc()
    }

    private inner class Launcher : Runnable {
        override fun run() {
            launch()
        }
    }
}