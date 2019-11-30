package kz.test.testnews.android.modules.news.domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kz.test.testnews.R
import kz.test.testnews.android.custom.BackButtonListener
import kz.test.testnews.android.modules.news.presentation.WebView
import kz.test.testnews.databinding.FragmentNewsBinding
import kz.test.testnews.databinding.FragmentWebviewBinding
import kz.testwhether.android.modules.base.domain.BaseMvpFragment
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Router

class WebViewFragment: BaseMvpFragment(), WebView, BackButtonListener {

    lateinit var binding: FragmentWebviewBinding

    private val router: Router by inject()

    lateinit var url: String

    companion object {
        const val TAG = "WebViewFragment"

        fun newInstance(url: String): WebViewFragment {
            val fragment: WebViewFragment = WebViewFragment()
            val args: Bundle = Bundle()
            args.putString("url", url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString("url", "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false)
        binding.data = url
        binding.toolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
        }
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}