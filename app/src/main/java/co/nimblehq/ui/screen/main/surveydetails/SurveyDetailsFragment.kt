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
        with(args.survey) {
            tvSurveyDetailsHeader.text = header
            tvSurveyDetailsDescription.text = description
            ivSurveyDetailsBackground.loadImage(imageUrl)
        }
    }

    override fun bindViewEvents() {
        btSurveyDetailsStartSurvey.subscribeOnClick {
            viewModel.loadSurveyDetails(args.survey.id)
        }.bindForDisposable()

        ivSurveyDetailsBack.subscribeOnClick {
            navigator.navigateBack()
        }.bindForDisposable()
    }

    override fun bindViewModel() {
        viewModel.showError
            .subscribe(::bindShowError)
            .bindForDisposable()

        viewModel.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()


        viewModel.questionItemPagerUiModels
            .subscribe(::bindQuestionItemPagerUiModels)
            .bindForDisposable()
    }

    private fun bindShowError(throwable: Throwable) {
        displayError(throwable)
    }

    private fun bindLoading(isLoading: Boolean) {
        toggleLoading(isLoading)
    }

    private fun bindQuestionItemPagerUiModels(uiModels: List<QuestionItemPagerUiModel>) {
        // TODO: Add logic to show questions list UI after get latest questions from start survey button
        displayError(AppError(null,"Questions data is ready"))
    }
}
