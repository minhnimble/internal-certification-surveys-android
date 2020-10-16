package co.nimblehq.ui.screen.main.surveys

import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.data.lib.common.DATE_FORMAT_SHORT_DISPLAY
import co.nimblehq.data.lib.extension.subscribeOnClick
import co.nimblehq.extension.toDisplayFormat
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screen.main.LoaderAnimatable
import co.nimblehq.ui.screen.main.MainNavigator
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerAdapter
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerItemUiModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_fragment_surveys.*
import kotlinx.android.synthetic.main.fragment_surveys.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SurveysFragment: BaseFragment(), BaseFragmentCallbacks, NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var navigator: MainNavigator

    private lateinit var surveysPagerAdapter: SurveysPagerAdapter

    private val viewModel by viewModels<SurveysViewModel>()

    private val loaderAnimator: LoaderAnimatable? by lazy {
        requireActivity() as? LoaderAnimatable
    }

    override val layoutRes = R.layout.fragment_surveys

    override fun initViewModel() {
        viewModel.checkAndLoadSurveysListIfNeeded()
    }

    override fun setupView() {
        tvSurveysDate.text = Date().toDisplayFormat(DATE_FORMAT_SHORT_DISPLAY).toUpperCase()

        vpSurveys.adapter = SurveysPagerAdapter().also {
            surveysPagerAdapter = it
            it.registerAdapterDataObserver(ciSurveysIndicator.adapterDataObserver)
        }
        ciSurveysIndicator.setViewPager(vpSurveys)
    }

    override fun bindViewEvents() {
        nvDrawerContainer.setNavigationItemSelectedListener(this)
        ivSurveysUserAvatar.subscribeOnClick {
            toggleDrawer(!dlSurveys.isDrawerOpen(GravityCompat.END))
        }.bindForDisposable()
    }

    override fun bindViewModel() {
        viewModel.error
            .subscribe(::displayError)
            .bindForDisposable()

        viewModel.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()

        viewModel.surveysPagerItemUiModels
            .subscribe(::bindSurveysPagerItemUiModels)
            .bindForDisposable()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_logout -> {
                //TODO: Implement logout logic
            }
        }
        toggleDrawer(false)
        return true
    }

    private fun bindLoading(isLoading: Boolean) {
        if (!isLoading) loaderAnimator?.toggleShimmerLoader(isLoading)
        toggleLoading(isLoading)
    }

    private fun bindSurveysPagerItemUiModels(uiModels: List<SurveysPagerItemUiModel>) {
        if (uiModels.isNotEmpty()) {
            surveysPagerAdapter.uiModels = uiModels
        }
    }

    private fun toggleDrawer(shouldShow: Boolean) {
        if (shouldShow) dlSurveys.openDrawer(GravityCompat.END) else dlSurveys.closeDrawer(GravityCompat.END)
    }
}
