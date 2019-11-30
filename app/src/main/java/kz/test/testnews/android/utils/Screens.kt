package kz.test.testnews.android.utils

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import kz.test.testnews.android.modules.comment.domain.CommentFragment
import kz.test.testnews.android.modules.home.domain.HomeActivity
import kz.test.testnews.android.modules.home.domain.HomeMainFragment
import kz.test.testnews.android.modules.news.domain.BestFragment
import kz.test.testnews.android.modules.news.domain.NewsFragment
import kz.test.testnews.android.modules.news.domain.TopFragment
import kz.test.testnews.android.modules.news.domain.WebViewFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class HomeScreen: SupportAppScreen(){
        override fun getActivityIntent(context: Context?): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
//
    class HomeMainScreen: SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return HomeMainFragment.newInstance()
        }
    }

    class NewsScreen: SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return NewsFragment.newInstance()
        }
    }

    class TopScreen: SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return TopFragment.newInstance()
        }
    }

    class BestScreen: SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return BestFragment.newInstance()
        }
    }

    class WebScreen(private var url: String): SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return  WebViewFragment.newInstance(url)
        }
    }

    class CommentScreen(private var kids: List<Int>): SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return CommentFragment.newInstance(ArrayList<Int>(kids))
        }
    }
}