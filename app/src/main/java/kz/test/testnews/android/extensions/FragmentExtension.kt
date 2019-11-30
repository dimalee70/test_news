package kz.test.testnews.android.extensions

import androidx.appcompat.app.AlertDialog
import kz.test.testnews.android.moxy.MvpAppCompatFragment
import kz.testwhether.android.modules.dialogs.presentation.ConfirmDialogPresenter
import kz.testwhether.android.modules.dialogs.presentation.ErrorDialogPresenter

inline fun MvpAppCompatFragment.showErrorAlertDialog(func: ErrorDialogPresenter.() -> Unit, title: String?, message: String?): AlertDialog =
    ErrorDialogPresenter(this.context!!, title, message).apply {
        func()
    }.create(null)

inline fun MvpAppCompatFragment.showConfirmAlertDialog(func: ConfirmDialogPresenter.() -> Unit, title: Int, message: Int): AlertDialog =
    ConfirmDialogPresenter(this.context!!, title, message).apply{
        func()
    }.create(null)
