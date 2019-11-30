package kz.testwhether.android.modules.main.presentation

import kz.test.testnews.android.modules.base.presentation.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainAppView: BaseView {
}