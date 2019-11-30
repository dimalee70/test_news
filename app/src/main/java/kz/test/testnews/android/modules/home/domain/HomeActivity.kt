package kz.test.testnews.android.modules.home.domain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kz.test.testnews.R
import kz.test.testnews.android.custom.BackButtonListener
import kz.test.testnews.android.modules.home.presentation.HomePresenter
import kz.test.testnews.android.modules.home.presentation.HomeView
import kz.test.testnews.android.utils.Screens
import kz.testwhether.android.modules.base.domain.BaseActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

class HomeActivity: BaseActivity(),HomeView{

    companion object{
        const val TAG = "HomeActivity"
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    private val router: Router by inject()

//    var weatherService: ApiManager = get()

    @InjectPresenter
    lateinit var mHomePresenter: HomePresenter

    @ProvidePresenter
    fun providePresenter(): HomePresenter{
        return HomePresenter(router)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if(savedInstanceState == null){
            navigator.applyCommands(arrayOf<Command>(Replace(Screens.HomeMainScreen())))
        }
    }


    var navigator: SupportAppNavigator = object : SupportAppNavigator(this, R.id.activity_home_frame_layout) {
        override fun setupFragmentTransaction(
            command: Command?,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction?
        ) {
            if (command is Forward
                && currentFragment is HomeMainFragment
                && nextFragment == null
            ) {
                setupSharedElement(
                    (currentFragment as HomeMainFragment?)!!,
                    nextFragment,
                    fragmentTransaction!!
                )
            }
        }
    }

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.activity_home_frame_layout)
        if (fragment != null && fragment is BackButtonListener
            && (fragment as BackButtonListener).onBackPressed()
        ) {
            return
        } else {
            super.onBackPressed()
        }
    }

    private fun setupSharedElement(
        showImageFragment: HomeMainFragment,
        nextFragment: Nothing? = null,
        fragmentTransaction: FragmentTransaction
    ) {

//        val changeBounds = ChangeBounds()//.apply { duration = 10000 }
//        productRegisterFragment.sharedElementEnterTransition = changeBounds
//        productRegisterFragment.sharedElementReturnTransition = changeBounds
////
//        val view = productRegisterFragment.binding.makePhotoBtn
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            view.transitionName = LoginInActivity.LOGIN_TRANSITION
//        }
//        fragmentTransaction.addSharedElement(view , HomeActivity.PRODUCT_TRANSITION)
    }

//    private inner class NewsPageAdapter: FragmentPagerAdapter(childFragmentManager)

}