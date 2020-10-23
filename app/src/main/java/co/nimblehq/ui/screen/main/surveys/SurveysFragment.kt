package co.nimblehq.ui.screen.main.surveys

import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.common.DATE_FORMAT_SHORT_DISPLAY
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.data.lib.extension.subscribeOnClick
import co.nimblehq.extension.loadImageWithFadeAnimation
import co.nimblehq.extension.switchTextWithFadeAnimation
import co.nimblehq.extension.toDisplayFormat
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.common.listener.OnSwipeTouchListener
import co.nimblehq.ui.screen.main.LoaderAnimatable
import co.nimblehq.ui.screen.main.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_surveys.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SurveysFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: MainNavigator

    private val viewModel by viewModels<SurveysViewModel>()

    private val loaderAnimator: LoaderAnimatable? by lazy {
        requireActivity() as? LoaderAnimatable
    }

    override val layoutRes = R.layout.fragment_surveys

    override fun initViewModel() {
        viewModel.checkAndRefreshInitialSurveys()
    }

    override fun setupView() {
        tvSurveysDate.text = Date().toDisplayFormat(DATE_FORMAT_SHORT_DISPLAY).toUpperCase()

        srlSurveys.setOnRefreshListener { viewModel.refreshSurveysList() }
    }

    override fun bindViewEvents() {
        btSurveysItemNext.subscribeOnClick {
            // TODO: Handle navigate to survey details screen
        }.bindForDisposable()

        clSurveys.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                viewModel.inputs.nextIndex()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                viewModel.inputs.previousIndex()
            }
        })
    }

    override fun bindViewModel() {
        viewModel.error
            .subscribe(::bindError)
            .bindForDisposable()

        viewModel.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()

        viewModel.showRefreshing
            .subscribe(::bindRefreshing)
            .bindForDisposable()

        viewModel.selectedSurveyIndex
            .subscribe(::bindSelectedSurveyIndex)
            .bindForDisposable()

        viewModel.surveyItemUiModels
            .subscribe(::bindSurveyItemUiModels)
            .bindForDisposable()
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
        val surveyUiModel = viewModel.getSelectedSurveyUiModel()
        if (surveyUiModel != null) {
            ciSurveysIndicator.animatePageSelected(index)
            tvSurveysItemHeader.switchTextWithFadeAnimation(surveyUiModel.header)
            tvSurveysItemDescription.switchTextWithFadeAnimation(surveyUiModel.description)
            ivSurveysItemBackground.loadImageWithFadeAnimation(surveyUiModel.imageUrl)
        }
    }

    private fun bindSurveyItemUiModels(uiModels: List<SurveyItemUiModel>) {
        if (uiModels.isEmpty()) return
        ciSurveysIndicator.createIndicators(uiModels.size, viewModel.selectedSurveyIndexValue)
    }
}
