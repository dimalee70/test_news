package kz.test.testnews.android.modules.comment.domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import kz.test.testnews.R
import kz.test.testnews.android.api.ApiManager
import kz.test.testnews.android.api.response.Comment
import kz.test.testnews.android.custom.BackButtonListener
import kz.test.testnews.android.custom.RecyclerBindingAdapter
import kz.test.testnews.android.modules.comment.presentation.CommentPresenter
import kz.test.testnews.android.modules.comment.presentation.CommentView
import kz.test.testnews.databinding.FragmentCommentBinding
import kz.testwhether.android.modules.base.domain.BaseItem
import kz.testwhether.android.modules.base.domain.BaseMvpFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Router

class CommentFragment: BaseMvpFragment(), CommentView, BackButtonListener {

    private  val router: Router by inject()

    val client: ApiManager = get()

    lateinit var binding: FragmentCommentBinding

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var kids: List<Int> = ArrayList<Int>()

    lateinit var recyclerCommentAdapter: RecyclerBindingAdapter<BaseItem>

    private val comments: ObservableArrayList<BaseItem> = ObservableArrayList()

    @InjectPresenter
    lateinit var mCommentPresenter: CommentPresenter

    @ProvidePresenter
    fun providePresenter(): CommentPresenter{
        return CommentPresenter(router, client)
    }

    companion object {
        const val TAG = "ComentFragment"

        fun newInstance(kids: List<Int>): CommentFragment {
            val fragment: CommentFragment = CommentFragment()
            val args: Bundle = Bundle()
            args.putIntegerArrayList("kids", ArrayList(kids))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCommentPresenter.attachLifecycle(lifecycleRegistry)
        kids = arguments!!.getIntegerArrayList("kids")!!
        mCommentPresenter.observeCommentResponseBoundary()
            .observe(this, Observer {
                response -> response.let {
                    setComment(response)
                }
            })
        recyclerCommentAdapter = RecyclerBindingAdapter(BR.data, context!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false)
        binding.presenter = mCommentPresenter
        binding.toolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
        }
        mCommentPresenter.setComments(kids)
        binding.commentRv.adapter = recyclerCommentAdapter
        return binding.root
    }

    fun setComment(comments: List<Comment>) {
        this.comments.addAll(comments)
        recyclerCommentAdapter.setItems(this.comments)
        recyclerCommentAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        binding.commentRv.adapter!!.notifyDataSetChanged()
    }

    override fun onBackPressed(): Boolean {
        mCommentPresenter.onBackPressed()
        return true
    }

}