package kz.test.testnews.android.modules.news.presentation

import kz.test.testnews.android.api.response.New
import kz.test.testnews.android.events.CommentClickEvent
import kz.testwhether.android.modules.base.presentation.BasePresenter
import moxy.InjectViewState
import org.greenrobot.eventbus.EventBus

@InjectViewState
class NewItemPresenter(private var new: New): BasePresenter<NewItemView>() {

    fun getComment(){
        EventBus.getDefault().post(CommentClickEvent(new))
    }
}