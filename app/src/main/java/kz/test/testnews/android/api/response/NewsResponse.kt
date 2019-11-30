package kz.test.testnews.android.api.response

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kz.test.testnews.BR
import kz.test.testnews.R
import kz.test.testnews.android.modules.news.presentation.NewItemPresenter
import kz.testwhether.android.modules.base.domain.BaseItem

data class New(
    var by: String?,
    var descendants: Int?,
    var id: Int?,
    var kids: ArrayList<Int>?,
    var score: Int?,
    var time: Long?,
    var title: String?,
    var type: String?,
    var url: String?
): BaseObservable(), BaseItem{

    var presener: NewItemPresenter? = null

    override fun getHolderLayout(): Int {
        presener = NewItemPresenter(this)
        return R.layout.item_new
    }

    var _by: String?
    @Bindable get() = "By " + by
    set(value) {
        _by = value
        notifyPropertyChanged(BR._by)
    }

    var _score: String?
    @Bindable get() = "Score " + score
    set(value) {
        _score = value
        notifyPropertyChanged(BR._score)
    }

    var _title: String?
    @Bindable get() = "Title " + title
    set(value) {
        _title = value
        notifyPropertyChanged(BR._title)
    }
}