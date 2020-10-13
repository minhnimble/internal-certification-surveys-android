package co.nimblehq.ui.screen.main.surveys

import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.data.lib.common.DATE_FORMAT_SHORT_DISPLAY
import co.nimblehq.extension.toDisplayFormat
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screen.main.LoaderAnimatable
import co.nimblehq.ui.screen.main.MainNavigator
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerAdapter
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerItemUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_surveys.*
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

    override fun initViewModel() {
        viewModel.getSurveysList()
    }

    override fun setupView() {
        tvSurveysDate.text = Date().toDisplayFormat(DATE_FORMAT_SHORT_DISPLAY).toUpperCase()

        vpSurveys.adapter = SurveysPagerAdapter().also {
            surveysPagerAdapter = it
            it.registerAdapterDataObserver(ciSurveysIndicator.adapterDataObserver)
        }
        ciSurveysIndicator.setViewPager(vpSurveys)
    }

    override fun bindViewEvents() { }

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

    private fun bindLoading(isLoading: Boolean) {
        if (!isLoading) loaderAnimator?.toggleShimmerLoader(isLoading)
        toggleLoading(isLoading)
    }

    private fun bindSurveysPagerItemUiModels(uiModels: List<SurveysPagerItemUiModel>) {
        if (uiModels.isNotEmpty()) {
            surveysPagerAdapter.uiModels = uiModels
        }
    }
}
