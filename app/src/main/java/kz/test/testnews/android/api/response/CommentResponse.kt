package kz.test.testnews.android.api.response

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kz.test.testnews.BR
import kz.test.testnews.R
import kz.testwhether.android.modules.base.domain.BaseItem

data class Comment(
    var by: String?,
    var id: Int?,
    var kids: ArrayList<Int>?,
    var pearent: Int?,
    var text:String?,
    var type: String?
): BaseObservable(), BaseItem{

    var _by: String?
    @Bindable get() = "By " + by
    set(value) {
        _by = value
        notifyPropertyChanged(BR._by)
    }
    var _text: String?
    @Bindable get() = text
    set(value) {
        _text = value
        notifyPropertyChanged(BR._text)
    }


    override fun getHolderLayout(): Int {
        return R.layout.item_comment
    }
}