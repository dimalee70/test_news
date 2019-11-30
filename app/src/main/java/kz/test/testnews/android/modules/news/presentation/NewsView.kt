package kz.test.testnews.android.modules.news.presentation

import kz.test.testnews.android.api.response.New
import kz.test.testnews.android.modules.base.presentation.BaseView

interface NewsView: BaseView {
    fun setNews(news: List<New>)
    fun initView()
}