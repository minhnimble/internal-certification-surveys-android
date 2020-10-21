package co.nimblehq.ui.screen.main.surveydetails

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.nimblehq.R
import co.nimblehq.data.error.AppError
import co.nimblehq.data.lib.extension.subscribeOnClick
import co.nimblehq.extension.loadImage
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screen.main.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_survey_details.*
import javax.inject.Inject

@AndroidEntryPoint
class SurveyDetailsFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: MainNavigator

    private val args: SurveyDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<SurveyDetailsViewModel>()

    override val layoutRes = R.layout.fragment_survey_details

    override fun initViewModel() { }

    override fun setupView() {
        tvSurveyDetailsHeader.text = args.surveyHeader
        tvSurveyDetailsDescription.text = args.surveyDescription
        ivSurveyDetailsBackground.loadImage(args.surveyImageUrl)
    }

    override fun bindViewEvents() {

        btSurveyDetailsStartSurvey.subscribeOnClick {
            // TODO: Start showing questions list logic
            displayError(AppError(null, "Start Survey button clicked"))
        }

        ivSurveyDetailsBack.subscribeOnClick {
            navigator.navigateBack()
        }.bindForDisposable()
    }

    override fun bindViewModel() { }
}
