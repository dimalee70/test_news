package kz.test.testnews.android.extensions

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kz.testwhether.android.modules.dialogs.presentation.ConfirmDialogPresenter
import kz.testwhether.android.modules.dialogs.presentation.ErrorDialogPresenter

inline fun AppCompatActivity.showErrorAlertDialog(func: ErrorDialogPresenter.() -> Unit, title: String?, message: String?): AlertDialog =
    ErrorDialogPresenter(this, title, message).apply {
        func()
    }.create(null)