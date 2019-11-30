package kz.testwhether.android.modules.base.domain

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.load.engine.GlideException
import com.google.gson.Gson
import kz.smart.calendar.presentation.presenter.dialogs.DelayedProgressDialog
import kz.test.testnews.R
import kz.test.testnews.android.extensions.showErrorAlertDialog
import kz.test.testnews.android.moxy.MvpActivity
import kz.test.testnews.android.utils.Constants
import kz.test.testnews.android.modules.base.presentation.BaseView
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import ru.terrakok.cicerone.NavigatorHolder
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

open class BaseActivity: MvpActivity(), BaseView {

    val BASE_TAG: String = "BaseActivity"

    val navigatorHolder: NavigatorHolder by inject()

    private val sharedPref: SharedPreferences by inject()

    protected var isFullScreen: Boolean = false

    private var progressDialog: DelayedProgressDialog? = null

    private lateinit var currentTheme: String

    var errorDialog: AlertDialog? = null

    protected var isDynamicThemingOn: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isDynamicThemingOn && isFullScreen)
        {
            return
        }

        currentTheme = if (isDynamicThemingOn)
        {
            sharedPref.getString(Constants.themePrefsKey, Constants.lightTheme)!!
        } else {
            Constants.lightTheme
        }
        setAppTheme(currentTheme)

    }

    fun setAppTheme(currentTheme: String) {
        when (currentTheme)
        {
            Constants.lightTheme -> {
                setTheme(R.style.Theme_App_Light)
            }
            else -> {
                setTheme(R.style.Theme_App_Dark)
            }
        }
    }
//    private var loadingDialog: LoadingDialog? = null

    override fun showError(exception: Throwable) {
        runOnUiThread{
            if(errorDialog == null ||(errorDialog != null && !errorDialog!!.isShowing)) {
                val responseBody = if (exception is HttpException) {
                    exception.response()!!.errorBody()?.string()
                } else {
                    null
                }
                errorDialog = showErrorAlertDialog(
                    {
                        cancelable = true
                        closeIconClickListener {
                            Timber.d("Error dialog close button clicked")
                        }
                    },
                    getNetworkErrorTitle(exception, responseBody),
                    getErrorMessage(exception, responseBody)
                )
                errorDialog?.show()
            }
        }
    }

    fun isNetworkAvailable(): Boolean {
        val cm = applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI)
        }
        return false
    }

    fun getNetworkErrorTitle(error: Throwable, responseBody: String?): String
    {
        if (error is HttpException)
        {
            when (error.code())
            {
                404 -> return getString(R.string.bad_server_response)
                500 -> return getString(R.string.default_unexpected_error_message)
                502 -> return getString(R.string.default_error_message)
            }
            return getErrorTitle(responseBody)
        } else if (error is SocketTimeoutException)
        {
            return getString(R.string.timed_out)
        } else if (error is IOException)
        {
            return getString(R.string.network_connection_lost)
        }
        else if (error is GlideException)
        {
            return getString(R.string.bad_connection)
        }

        return if (error.localizedMessage != null) getString(R.string.unknown_error) else ""
    }

    private fun getErrorTitle(responseBody: String?): String {
        try {
            if (responseBody ==  null) {
                return getString(R.string.unknown_error_from_backend)
            }

            val gson = Gson()
//            val errorRespType = object : TypeToken<ErrorResponse>() {}.type
//            val resp = gson.fromJson<ErrorResponse>(responseBody, errorRespType)
//            if (resp.messageList?.isNotEmpty() == true) {
//                return resp.messageList!!.joinToString(separator = "\n") {"\'${it.message}\'" }
//            }

            return getString(R.string.unknown_error_from_backend)
        } catch (e: Exception) {
            return e.localizedMessage!!
        }
    }

    private fun getErrorMessage(exception: Throwable, responseBody: String?): String? {
        if (exception !is HttpException) {

            return null
        }
        try {

            val gson = Gson()
//            val errorRespType = object : TypeToken<ErrorResponse>() {}.type
//            val resp = gson.fromJson<ErrorResponse>(responseBody!!, errorRespType)
//
//
//            if (resp.messageList?.isNotEmpty() == true) {
//                return resp.messageList!!.joinToString(separator = "\n") {"\'${it.description}\'" }
//            }

            return null
        } catch (e: Exception) {
            return null
        }
    }

    override fun showError(message: String?, codeError: Int) {
        var msg = message
        if (msg == null)
        {
            msg = getString(codeError)
        }
        runOnUiThread {
            if (errorDialog == null) {
                errorDialog = showErrorAlertDialog({
                    cancelable = true
                    closeIconClickListener {
                        Timber.d("Error dialog close button clicked")
                    }
                }, "Ошибка", msg)
                errorDialog?.show()
            }
        }
    }

    override fun hideProgress() {
//        runOnUiThread {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            progressDialog?.cancel()
//            progressDialog = null
//        }
    }

    override fun showProgress() {
        runOnUiThread {
            if (progressDialog == null)
                progressDialog = DelayedProgressDialog(this)

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            progressDialog?.show()
        }
    }

    override fun showRequestSuccessfully(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideKeyboard() {

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (getCurrentFocus() != null && getCurrentFocus()?.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (progressDialog != null)
        {
            progressDialog?.dialog?.setOnDismissListener(null)
            progressDialog?.cancel()
        }

        if (errorDialog != null)
        {
            errorDialog?.setOnDismissListener(null)
            errorDialog?.dismiss()
        }
    }


}