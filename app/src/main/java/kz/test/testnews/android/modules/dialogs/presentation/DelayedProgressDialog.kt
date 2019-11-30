package kz.smart.calendar.presentation.presenter.dialogs

import android.content.Context
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import kz.test.testnews.android.utils.Constants

class DelayedProgressDialog(val context: Context)
{
    private var runner: Runnable? = null
    var dialog: AlertDialog? = null

    fun show(afterDelayMilliSec: Long = Constants.PROGRESS_DELAY) {

        this.runner = Runnable {
            try {
                if (dialog == null || (dialog != null && !dialog!!.isShowing)) {
                    dialog = ProgressDialogPresenter(context).apply {}.create(null)
                    dialog?.setCancelable(false)
                    dialog?.show()
                }
            } catch (e: Exception)
            {
                //Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

        DialogHandler.dialogHandler.postDelayed(this.runner!!, afterDelayMilliSec)
    }

    fun cancel() {
        DialogHandler.dialogHandler.removeCallbacks(runner!!)
        dialog?.cancel()
        dialog = null
    }
}

object DialogHandler
{
    val dialogHandler = Handler()
}

