package co.nimblehq.ui.screen.main.surveys

import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.data.lib.common.DATE_FORMAT_SHORT_DISPLAY
import co.nimblehq.extension.loadImageWithFadeAnimation
import co.nimblehq.extension.switchTextWithFadeAnimation
import co.nimblehq.extension.toDisplayFormat
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.common.listener.OnSwipeTouchListener
import co.nimblehq.ui.screen.main.LoaderAnimatable
import co.nimblehq.ui.screen.main.MainNavigator
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerAdapter
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerItemUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_surveys.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SurveysFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: MainNavigator

    private lateinit var surveysPagerAdapter: SurveysPagerAdapter

    private val viewModel by viewModels<SurveysViewModel>()

    private val loaderAnimator: LoaderAnimatable? by lazy {
        requireActivity() as? LoaderAnimatable
    }

    override val layoutRes = R.layout.fragment_surveys

    private var currentSelectedSurveyItemIndex = -1

    override fun initViewModel() {
        viewModel.checkAndLoadInitialSurveysListIfNeeded()
    }

    override fun setupView() {
        tvSurveysDate.text = Date().toDisplayFormat(DATE_FORMAT_SHORT_DISPLAY).toUpperCase()
    }

    override fun bindViewEvents() {
        vSurveysItemTransparent.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {

            override fun onSwipeTop() {
                super.onSwipeTop()
                Timber.d("Swiped top")
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                viewModel.refreshSurveysList()
            }

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
            .subscribe(::displayError)
            .bindForDisposable()

        viewModel.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()

        viewModel.selectedSurveyIndex
            .subscribe(::bindSelectedSurveyIndex)
            .bindForDisposable()

        viewModel.selectedSurveyItem
            .subscribe(::bindSelectedSurveyItem)
            .bindForDisposable()

        viewModel.surveysPagerItemUiModels
            .subscribe(::bindSurveysPagerItemUiModels)
            .bindForDisposable()
    }

    private fun bindLoading(isLoading: Boolean) {
        if (!isLoading) loaderAnimator?.toggleShimmerLoader(isLoading)
        toggleLoading(isLoading)
    }

    private fun bindSelectedSurveyIndex(index: Int) {
        if (index != -1) {
            currentSelectedSurveyItemIndex = index
            ciSurveysIndicator.animatePageSelected(index)
        }
    }

    private fun bindSelectedSurveyItem(item: SurveysPagerItemUiModel?) {
         item?.let {
            tvSurveysItemHeader.switchTextWithFadeAnimation(it.header)
            tvSurveysItemDescription.switchTextWithFadeAnimation(it.description)
            ivSurveysItemBackground.loadImageWithFadeAnimation(it.imageUrl)
        }
    }

    private fun bindSurveysPagerItemUiModels(uiModels: List<SurveysPagerItemUiModel>) {
        if (uiModels.isNotEmpty()) {
            ciSurveysIndicator.createIndicators(uiModels.size, currentSelectedSurveyItemIndex)
        }

    }
}
