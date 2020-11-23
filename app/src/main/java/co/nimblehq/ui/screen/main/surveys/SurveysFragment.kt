package co.nimblehq.ui.screen.main.surveys

import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.common.DATE_FORMAT_SHORT_DISPLAY
import co.nimblehq.data.lib.extension.subscribeOnClick
import co.nimblehq.extension.loadImageWithFadeAnimation
import co.nimblehq.extension.switchTextWithFadeAnimation
import co.nimblehq.extension.toDisplayFormat
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.common.listener.OnSwipeTouchListener
import co.nimblehq.ui.screen.main.LoaderAnimatable
import co.nimblehq.ui.screen.main.MainNavigator
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_fragment_surveys.*
import kotlinx.android.synthetic.main.fragment_surveys.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SurveysFragment: BaseFragment(), BaseFragmentCallbacks, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var navigator: MainNavigator

    private val viewModel by viewModels<SurveysViewModel>()

    private val loaderAnimator: LoaderAnimatable? by lazy {
        requireActivity() as? LoaderAnimatable
    }

    override val layoutRes = R.layout.fragment_surveys

    override fun initViewModel() {
        viewModel.checkAndRefreshInitialSurveys()
    }

    override fun setupView() {
        tvSurveysDate.text = Date().toDisplayFormat(DATE_FORMAT_SHORT_DISPLAY).toUpperCase(Locale.ROOT)

        srlSurveys.setOnRefreshListener { viewModel.refreshSurveysList() }
    }

    override fun bindViewEvents() {
        btSurveysItemNext.subscribeOnClick {
            viewModel.output.selectedSurveyUiModel?.let {
                navigator.navigateToSurveyDetails(it)
            }
        }.bindForDisposable()

        clSurveys.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                viewModel.input.nextIndex()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                viewModel.input.previousIndex()
            }
        })

        ivSurveysUserAvatar.subscribeOnClick {
            toggleDrawer(!dlSurveys.isDrawerOpen(GravityCompat.END))
        }.bindForDisposable()

        nvDrawerContainer.setNavigationItemSelectedListener(this)
    }

    override fun bindViewModel() {
        viewModel.output.error
            .subscribe(::bindError)
            .bindForDisposable()

        viewModel.output.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()

        viewModel.output.showRefreshing
            .subscribe(::bindRefreshing)
            .bindForDisposable()

        viewModel.output.selectedSurveyIndex
            .subscribe(::bindSelectedSurveyIndex)
            .bindForDisposable()

        viewModel.output.surveyItemUiModels
            .subscribe(::bindSurveyItemUiModels)
            .bindForDisposable()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_logout -> {
                //TODO: Implement logout logic
                displayError(Throwable("Logout pressed", null))
            }
        }
        toggleDrawer(false)
        return false
    }

    private fun bindError(throwable: Throwable) {
        if (throwable !is Ignored) displayError(throwable)
    }

    private fun bindLoading(isLoading: Boolean) {
        if (!isLoading) loaderAnimator?.toggleShimmerLoader(isLoading)
        toggleLoading(isLoading)
    }

    private fun bindRefreshing(isRefreshing: Boolean) {
        srlSurveys.isRefreshing = isRefreshing
    }

    private fun bindSelectedSurveyIndex(index: Int) {
        viewModel.output.selectedSurveyUiModel?.let {
            ciSurveysIndicator.animatePageSelected(index)
            tvSurveysItemHeader.switchTextWithFadeAnimation(it.header)
            tvSurveysItemDescription.switchTextWithFadeAnimation(it.description)
            ivSurveysItemBackground.loadImageWithFadeAnimation(it.imageUrl)
        }
    }

    private fun bindSurveyItemUiModels(uiModels: List<SurveyItemUiModel>) {
        if (uiModels.isEmpty()) return
        ciSurveysIndicator.createIndicators(uiModels.size, viewModel.output.selectedSurveyIndexValue)
    }

    private fun toggleDrawer(shouldShow: Boolean) {
        if (shouldShow) dlSurveys.openDrawer(GravityCompat.END) else dlSurveys.closeDrawer(GravityCompat.END)
    }
}
