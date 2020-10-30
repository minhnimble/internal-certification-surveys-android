package co.nimblehq.ui.screen.main.surveydetails

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import co.nimblehq.R
import co.nimblehq.data.error.AppError
import co.nimblehq.data.lib.common.DEFAULT_DURATION
import co.nimblehq.data.lib.common.FIRST_INDEX
import co.nimblehq.data.lib.extension.subscribeOnClick
import co.nimblehq.extension.*
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screen.main.MainNavigator
import co.nimblehq.ui.screen.main.surveydetails.adapter.QuestionPagerAdapter
import co.nimblehq.ui.screen.main.surveydetails.uimodel.QuestionItemPagerUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_survey_questions.*
import kotlinx.android.synthetic.main.fragment_survey_details.*
import javax.inject.Inject

@AndroidEntryPoint
class SurveyDetailsFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: MainNavigator

    private lateinit var questionsPagerAdapter: QuestionPagerAdapter

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

        vpSurveyQuestions.adapter = QuestionPagerAdapter().also {
            questionsPagerAdapter = it
        }
    }

    override fun bindViewEvents() {
        btSurveyDetailsStartSurvey.subscribeOnClick {
            viewModel.loadSurveyDetails(args.survey.id)
        }

        btSurveyQuestionsClose.subscribeOnClick {
            // TODO: Show confirm close survey questions popup
            navigator.navigateBack()
        }

        btSurveyQuestionsItemNext.subscribeOnClick {
            // TODO: Handle check to show next page or not
            viewModel.inputs.triggerNextQuestion()
        }

        btSurveyQuestionsSubmit.subscribeOnClick {
            // TODO: Handle logic to submit answers to server
            displayError(AppError(null, "Submit button clicked",))
        }

        ivSurveyDetailsBack.subscribeOnClick {
            navigator.navigateBack()
        }.bindForDisposable()

        vpSurveyQuestions.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.inputs.currentQuestionIndex(position)
            }
        })
    }

    override fun bindViewModel() {
        viewModel.currentQuestionIndex
            .subscribe(::bindCurrentQuestionIndex)
            .bindForDisposable()

        viewModel.showError
            .subscribe(::displayError)
            .bindForDisposable()

        viewModel.questionIndicator
            .subscribe(::bindQuestionIndicator)
            .bindForDisposable()

        viewModel.questionItemPagerUiModels
            .subscribe(::bindQuestionItemPagerUiModels)
            .bindForDisposable()

        viewModel.reachedLastQuestion
            .subscribe(::bindReachedLastQuestion)
            .bindForDisposable()

        viewModel.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()
    }

    private fun bindCurrentQuestionIndex(index: Int) {
        if (vpSurveyQuestions.currentItem != index) vpSurveyQuestions.setCurrentItem(index, true)
    }

    private fun bindLoading(isLoading: Boolean) {
        toggleLoading(isLoading)
    }

    private fun bindQuestionIndicator(questionIndicator: String) {
        tvSurveyQuestionsIndex.text = questionIndicator
    }

    private fun bindQuestionItemPagerUiModels(uiModels: List<QuestionItemPagerUiModel>) {
        if (uiModels.isNotEmpty()) {
            hideSurveyDetailsUI()
            showSurveyQuestionsPagerUI()
            questionsPagerAdapter.uiModels = uiModels
            viewModel.inputs.currentQuestionIndex(FIRST_INDEX)
        } else {
            displayError(AppError(null, null, R.string.general_no_question_error))
        }
    }

    private fun bindReachedLastQuestion(isReachedLastQuestion: Boolean) {
        val animatingDuration = DEFAULT_DURATION / 4
        if(isReachedLastQuestion) {
            if (btSurveyQuestionsItemNext.isShowing()) btSurveyQuestionsItemNext.startFadeOutAnimation(animatingDuration)
            if (btSurveyQuestionsSubmit.isNotShowing()) btSurveyQuestionsSubmit.startFadeInAnimation(animatingDuration)
        }
        else {
            if (btSurveyQuestionsItemNext.isNotShowing()) btSurveyQuestionsItemNext.startFadeInAnimation(animatingDuration)
            if (btSurveyQuestionsSubmit.isShowing()) btSurveyQuestionsSubmit.startFadeOutAnimation(animatingDuration)
        }
    }

    private fun hideSurveyDetailsUI() {
        val animatingDuration = DEFAULT_DURATION / 2
        vSurveyDetailsTransparent.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black_40a))
        ivSurveyDetailsBack.startFadeOutAnimation(animatingDuration)
        btSurveyDetailsStartSurvey.startFadeOutAnimation(animatingDuration)
        tvSurveyDetailsHeader.startFadeOutAnimation(animatingDuration)
        tvSurveyDetailsDescription.startFadeOutAnimation(animatingDuration)
    }

    private fun showSurveyQuestionsPagerUI() {
        iclSurveyQuestions.visibility = View.VISIBLE
        iclSurveyQuestions.startFadeInAnimation()
    }
}