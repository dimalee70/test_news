package kz.test.testnews.android.modules.home.domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import kz.test.testnews.R
import kz.test.testnews.android.custom.BackButtonListener
import kz.test.testnews.android.modules.home.presentation.HomeMainPresenter
import kz.test.testnews.android.modules.home.presentation.HomeMainView
import kz.test.testnews.android.utils.Screens
import kz.test.testnews.databinding.FragmentHomeMainBinding
import kz.testwhether.android.modules.base.domain.BaseMvpFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class HomeMainFragment: BaseMvpFragment(), HomeMainView {

    private val router: Router by inject()

    lateinit var binding: FragmentHomeMainBinding

    private val adapter by lazy { NewsPagesAdapter() }

    @InjectPresenter
    lateinit var mHomeMainPresenter: HomeMainPresenter

    @ProvidePresenter
    fun providePresenter(): HomeMainPresenter {
        return HomeMainPresenter(router)
    }

    companion object {
        const val TAG = "HomeMainFragment"

        fun newInstance(): HomeMainFragment {
            val fragment: HomeMainFragment = HomeMainFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }

        private const val TAB_NEW = 0
        private const val TAB_TOP = 1
        private const val TAB_BEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        binding.presenter = mHomeMainPresenter
        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.newsVp.offscreenPageLimit = 3
        binding.newsVp.adapter = adapter
    }

    private inner class NewsPagesAdapter : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = when (position) {
            TAB_NEW -> Screens.NewsScreen().fragment
            TAB_TOP -> Screens.TopScreen().fragment
            else -> Screens.BestScreen().fragment
        }

        override fun getCount() = 3

        override fun getPageTitle(position: Int) = when (position) {
            TAB_NEW -> getString(R.string.news_tab)
            TAB_TOP -> getString(R.string.top_tab)
            TAB_BEST -> getString(R.string.best_tap)
            else -> null
        }
    }
}