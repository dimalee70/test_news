package kz.smart.calendar.presentation.presenter.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import kz.test.testnews.R
import kz.testwhether.android.modules.dialogs.presentation.BaseDialogHelper

class ProgressDialogPresenter(private val context: Context) : BaseDialogHelper() {
    //  dialog view

    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.progress, null)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)
}