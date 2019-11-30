package kz.testwhether.android.modules.base.domain

import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.load.engine.GlideException
import com.google.gson.Gson
import kz.smart.calendar.presentation.presenter.dialogs.DelayedProgressDialog
import kz.test.testnews.R
import kz.test.testnews.android.extensions.showErrorAlertDialog
import kz.test.testnews.android.moxy.MvpAppCompatFragment
import kz.test.testnews.android.modules.base.presentation.BaseView

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

open class BaseMvpFragment: MvpAppCompatFragment(), BaseView {

    val BASE_TAG: String = "BaseMvpFragment"

    private var progressDialog: DelayedProgressDialog? = null
    var errorDialog: AlertDialog? = null

    override fun showError(exception: Throwable) {
        if (errorDialog == null || (errorDialog != null && !errorDialog!!.isShowing))
        {
            //необходимо вытащить responseBody, т.к после сериализации(вызова) response(), он будет всегда возвращать null
            val responseBody = if (exception is HttpException){
                exception.response().errorBody()?.string()
            } else{
                null
            }
            errorDialog = showErrorAlertDialog({
                cancelable = true
                closeIconClickListener {
                    Log.d(BASE_TAG, "Error dialog close button clicked")
                }
            }, getNetworkErrorTitle(exception, responseBody), getErrorMessage(exception, responseBody))
            errorDialog?.show()
        }
    }

    override fun showError(message: String?, codeError: Int) {
        if (errorDialog == null) {
            var msg = message
            if (msg == null) {
                msg = getString(codeError)
            }

            errorDialog = showErrorAlertDialog({
                cancelable = true
                closeIconClickListener {
                }
            }, "Ошибка", msg)
            errorDialog?.show()
        }
    }

    override fun hideProgress() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        progressDialog?.cancel()
        progressDialog = null
    }

    override fun showProgress() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        if (progressDialog == null)
            progressDialog = DelayedProgressDialog(this.context!!)

        progressDialog?.show()
    }

    override fun showRequestSuccessfully(message: String) {
    }

    override fun hideKeyboard() {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity?.getCurrentFocus() != null && activity?.getCurrentFocus()?.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(activity?.getCurrentFocus()?.getWindowToken(), 0)
        }
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

    private fun getNetworkErrorTitle(error: Throwable, responseBody: String?): String
    {
        when (error) {
            is com.jakewharton.retrofit2.adapter.rxjava2.HttpException -> {
                when (error.code())
                {
                    404 -> return getString(R.string.bad_server_response)
                    500 -> return getString(R.string.default_unexpected_error_message)
                    502 -> return getString(R.string.default_error_message)
                }

                return getErrorTitle(responseBody)
            }
            is SocketTimeoutException -> return getString(R.string.timed_out)
            is IOException -> return getString(R.string.network_connection_lost)
            is GlideException -> return getString(R.string.bad_connection)

            else -> return if (error.localizedMessage != null) getString(R.string.unknown_error) else ""
        }
    }

    private fun getErrorMessage(exception: Throwable, responseBody: String?): String? {
        if (exception !is com.jakewharton.retrofit2.adapter.rxjava2.HttpException) {
            return null
        }

        try {
            val gson = Gson()
//            val errorRespType = object : TypeToken<ErrorResponse>() {}.type
//            val resp = gson.fromJson<ErrorResponse>(responseBody!!, errorRespType)
//
//            if (resp.messageList?.isNotEmpty() == true) {
//                return resp.messageList!!.joinToString(separator = "\n") {"\'${it.description}\'" }
//            }
            return null
        } catch (e: Exception) {
            return null
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