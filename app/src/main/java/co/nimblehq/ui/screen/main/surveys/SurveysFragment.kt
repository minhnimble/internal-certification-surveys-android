package co.nimblehq.ui.screen.main.surveys

import android.os.Handler
import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screen.main.LoaderAnimatable
import co.nimblehq.ui.screen.main.MainNavigator
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerAdapter
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerItemUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_surveys.*
import javax.inject.Inject

@AndroidEntryPoint
class SurveysFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: MainNavigator

    private val viewModel by viewModels<SurveysViewModel>()

    private val loaderAnimator: LoaderAnimatable? by lazy {
        requireActivity() as? LoaderAnimatable
    }

    override val layoutRes = R.layout.fragment_surveys

    override fun initViewModel() { }

    override fun setupView() {
        // TODO: Will remove in logic PR
        Handler().postDelayed({
            loaderAnimator?.toggleShimmerLoader(false)
        }, 2000)

        // TODO: update this to real data in logic PR
        tvSurveysDate.text = getString(R.string.surveys_sample_date_desc)
        val testData = listOf(
            SurveysPagerItemUiModel(R.drawable.bg_surveys_sample_item, getString(R.string.surveys_sample_item_header), getString(R.string.surveys_sample_item_description)),
            SurveysPagerItemUiModel(R.drawable.bg_surveys_sample_item, getString(R.string.surveys_sample_item_header), getString(R.string.surveys_sample_item_description)),
            SurveysPagerItemUiModel(R.drawable.bg_surveys_sample_item, getString(R.string.surveys_sample_item_header), getString(R.string.surveys_sample_item_description)),
            SurveysPagerItemUiModel(R.drawable.bg_surveys_sample_item, getString(R.string.surveys_sample_item_header), getString(R.string.surveys_sample_item_description))
        )

        vpSurveys.adapter = SurveysPagerAdapter(testData).also {
            it.registerAdapterDataObserver(ciSurveysIndicator.adapterDataObserver)
        }
        ciSurveysIndicator.setViewPager(vpSurveys)
    }

    override fun bindViewEvents() { }

    override fun bindViewModel() { }
}
