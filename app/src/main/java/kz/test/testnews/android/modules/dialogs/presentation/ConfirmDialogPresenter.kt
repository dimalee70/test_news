package kz.testwhether.android.modules.dialogs.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kz.test.testnews.R

class ConfirmDialogPresenter(private var context: Context, private val title: Int, private val message: Int): BaseDialogHelper() {

    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    override fun create(dialogSize: DialogSize?): AlertDialog {
        dialog = super.create(null)
        dialogView.findViewById<TextView>(R.id.titleTv)?.setText(title)
        dialogView.findViewById<TextView>(R.id.confirmTv)?.setText(message)
        dialog?.setCanceledOnTouchOutside(false)
        return dialog!!
    }

    private val yesBtn: Button by lazy {
        dialogView.findViewById<Button>(R.id.yesBtn)
    }

    private val noBtn: Button by lazy {
        dialogView.findViewById<Button>(R.id.noBtn)
    }

    fun noBtnClickListenerToDialogButton(func: (() -> Unit)? = null ) =
        with(noBtn){
            setClickListenerToDialogButton(func)
        }
}