package kz.test.testnews.android.modules.comment.presentation

import androidx.lifecycle.MutableLiveData
import kz.test.testnews.android.api.ApiManager
import kz.test.testnews.android.api.response.Comment
import kz.test.testnews.android.api.response.New
import kz.test.testnews.android.modules.news.presentation.NewsView
import kz.testwhether.android.modules.base.presentation.BasePresenter
import moxy.InjectViewState
import ru.terrakok.cicerone.Router

@InjectViewState
class CommentPresenter(private var router: Router, private var client: ApiManager): BasePresenter<CommentView>() {

    var liveCommentResponse = MutableLiveData<List<Comment>>()

    var listComment = mutableListOf<Comment>()

    fun setComments(kids: List<Int>){
//        for(item in kids.indices){
//            if (item == kids.lastIndex){
//                getComments(kids.get(item), true)
//                return
//            }
//            getComments(kids.get(item), false)
//        }
        kids.forEach {
            if (it == kids.last()) {
                getComments(it, true)
                return
            }
            getComments(it, false)
        }

    }

    fun getComments(newId: Int, isLast:Boolean){

//        disposables.add(
            client.getComments(newId)
            .subscribeOn(background)
            .observeOn(mainThread)
//            .doFinally{
//                viewState.hideProgress()
//            }
            .subscribe({
                result ->
                run {
                    if (result != null)
                        listComment.add(result)
                    if (isLast)
                        liveCommentResponse.value = listComment
                }

            },{
                error ->
                viewState.showError(error)
            })
//        )
    }

    fun observeCommentResponseBoundary(): MutableLiveData<List<Comment>> {
        return liveCommentResponse
    }

    fun onBackPressed(){
        router.exit()
    }
}