package kz.test.testnews.android.modules.news.domain

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import kz.test.testnews.R
import kz.test.testnews.android.api.ApiManager
import kz.test.testnews.android.api.response.New
import kz.test.testnews.android.custom.BackButtonListener
import kz.test.testnews.android.custom.RecyclerBindingAdapter
import kz.test.testnews.android.events.CommentClickEvent
import kz.test.testnews.android.modules.home.domain.HomeMainFragment
import kz.test.testnews.android.modules.home.presentation.HomeMainPresenter
import kz.test.testnews.android.modules.news.presentation.NewsPresenter
import kz.test.testnews.android.modules.news.presentation.NewsView
import kz.test.testnews.android.utils.Screens
import kz.test.testnews.databinding.FragmentHomeMainBinding
import kz.test.testnews.databinding.FragmentNewsBinding
import kz.testwhether.android.modules.base.domain.BaseItem
import kz.testwhether.android.modules.base.domain.BaseMvpFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Router

class NewsFragment: BaseMvpFragment(), NewsView, RecyclerBindingAdapter.OnItemClickListener<BaseItem>, BackButtonListener {

    private val router: Router by inject()

    var client: ApiManager = get()


    lateinit var binding: FragmentNewsBinding

    val news: ObservableArrayList<BaseItem> = ObservableArrayList()

    lateinit var recyclerNewAdapter: RecyclerBindingAdapter<BaseItem>

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var onNewClickListenerRecycler: RecyclerBindingAdapter.OnItemClickListener<BaseItem>? = this

    @InjectPresenter
    lateinit var mNewsPresenter: NewsPresenter

    @ProvidePresenter
    fun providePresenter(): NewsPresenter {
        return NewsPresenter(router, client)
    }

    companion object {
        const val TAG = "NewsFragment"

        fun newInstance(): NewsFragment {
            val fragment: NewsFragment = NewsFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNewsPresenter.attachLifecycle(lifecycleRegistry)

        mNewsPresenter.observeNewResponseBoundary()
            .observe(this, Observer {
                    response ->
                if (response != null)
                    setNews(response)
            })

        recyclerNewAdapter = RecyclerBindingAdapter(BR.data, context!!)
        recyclerNewAdapter.setOnItemClickListener(onNewClickListenerRecycler!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
        binding.presenter = mNewsPresenter
        initView()
        binding.newsRv.adapter = recyclerNewAdapter
        return  binding.root
    }

    override fun setNews(news: List<New>) {
        if (news != null) {
            this.news.clear()
            this.news.addAll(news)
            recyclerNewAdapter.setItems(news)
            recyclerNewAdapter.notifyDataSetChanged()
        }
    }

    override fun initView() {
        mNewsPresenter.reloadData()
    }

    override fun onItemClick(position: Int, item: BaseItem) {
        if (item is New)
            mNewsPresenter.openWebView(item.url)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CommentClickEvent) {
        if (event.new.kids.isNullOrEmpty())
            Toast.makeText(context, "No Comments", Toast.LENGTH_SHORT).show()
        else{
            router.navigateTo(Screens.CommentScreen(ArrayList<Int>(event.new.kids!!)))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            onNewClickListenerRecycler = this
        }catch (e: Throwable){
            throw ClassCastException(context.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
//        binding.newsRv.adapter!!.notifyDataSetChanged()
    }

    override fun onBackPressed(): Boolean {
        mNewsPresenter.onBackPressed()
        return true
    }

    override fun onDetach() {
        super.onDetach()
        onNewClickListenerRecycler = null
    }


}