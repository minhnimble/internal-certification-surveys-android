package co.nimblehq.ui.screen.main.surveydetails.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RatingBar
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R
import co.nimblehq.data.api.request.AnswerRequest
import co.nimblehq.data.api.request.QuestionRequest
import co.nimblehq.data.model.QuestionDisplayType
import co.nimblehq.extension.isValidIndex
import co.nimblehq.extension.refreshWithData
import co.nimblehq.ui.common.adapter.DiffUpdateAdapter
import co.nimblehq.ui.screen.main.surveydetails.decoration.SurveyQuestionNpsItemDecoration
import co.nimblehq.ui.screen.main.surveydetails.decoration.SurveyQuestionSliderItemDecoration
import co.nimblehq.ui.screen.main.surveydetails.uimodel.AnswerItemUiModel
import co.nimblehq.ui.screen.main.surveydetails.uimodel.QuestionItemPagerUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_survey_questions_choice.*
import kotlinx.android.synthetic.main.item_survey_questions_default.tvQuestionItemText
import kotlinx.android.synthetic.main.item_survey_questions_dropdown.*
import kotlinx.android.synthetic.main.item_survey_questions_heart.*
import kotlinx.android.synthetic.main.item_survey_questions_money.*
import kotlinx.android.synthetic.main.item_survey_questions_nps.*
import kotlinx.android.synthetic.main.item_survey_questions_slider.*
import kotlinx.android.synthetic.main.item_survey_questions_smiley.*
import kotlinx.android.synthetic.main.item_survey_questions_star.*
import kotlinx.android.synthetic.main.item_survey_questions_text_area.etQuestionItemTextArea
import kotlinx.android.synthetic.main.item_survey_questions_text_field.*
import kotlin.properties.Delegates

internal class QuestionPagerAdapter : RecyclerView.Adapter<QuestionPagerAdapter.QuestionViewHolder>(), DiffUpdateAdapter {

    val answeredQuestions: MutableList<QuestionRequest> = mutableListOf()

    var uiModels: List<QuestionItemPagerUiModel> by Delegates.observable(emptyList()) { _, _, new ->
        answeredQuestions.clear()
        updateQuestionsContent(new)
    }

    private var currentQuestionUiModels: List<QuestionItemPagerUiModel> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(
            old,
            new,
            { oldItem, newItem -> oldItem.id == newItem.id }
        )
    }

    var questionRequests: List<QuestionRequest> = listOf()

    override fun getItemCount() = currentQuestionUiModels.size

    override fun getItemViewType(position: Int): Int {
        return when (uiModels[position].displayType) {
            QuestionDisplayType.CHOICE -> R.layout.item_survey_questions_choice
            QuestionDisplayType.HEART -> R.layout.item_survey_questions_heart
            QuestionDisplayType.DROPDOWN -> R.layout.item_survey_questions_dropdown
            QuestionDisplayType.MONEY -> R.layout.item_survey_questions_money
            QuestionDisplayType.NPS -> R.layout.item_survey_questions_nps
            QuestionDisplayType.SMILEY -> R.layout.item_survey_questions_smiley
            QuestionDisplayType.STAR -> R.layout.item_survey_questions_star
            QuestionDisplayType.TEXTAREA -> R.layout.item_survey_questions_text_area
            QuestionDisplayType.TEXTFIELD -> R.layout.item_survey_questions_text_field
            QuestionDisplayType.SLIDER -> R.layout.item_survey_questions_slider
            else -> R.layout.item_survey_questions_default
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_survey_questions_choice -> ChoiceQuestionViewHolder(view, choiceAnswersAdapter = QuestionItemChoiceAdapter())
            R.layout.item_survey_questions_dropdown -> DropdownQuestionViewHolder(view, ArrayAdapter<String>(parent.context, R.layout.item_survey_questions_dropdown_answer_selected, R.id.tvDropdownQuestionItemAnswerTitle))
            R.layout.item_survey_questions_heart -> HeartQuestionViewHolder(view)
            R.layout.item_survey_questions_money -> MoneyQuestionViewHolder(view)
            R.layout.item_survey_questions_nps -> NpsQuestionViewHolder(view, context = parent.context, npsAnswersAdapter = QuestionItemNpsAdapter())
            R.layout.item_survey_questions_slider -> SliderQuestionViewHolder(view, context = parent.context, sliderAnswersAdapter = QuestionItemSliderAdapter())
            R.layout.item_survey_questions_smiley -> SmileyQuestionViewHolder(view)
            R.layout.item_survey_questions_star -> StarQuestionViewHolder(view)
            R.layout.item_survey_questions_text_area -> TextAreaQuestionViewHolder(view)
            R.layout.item_survey_questions_text_field -> TextFieldQuestionViewHolder(view, textFieldAnswersAdapter = QuestionItemTextFieldAdapter())
            else -> QuestionViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = currentQuestionUiModels[position]
        holder.bind(question) { questionId, newAnswers: List<AnswerRequest> ->
            answeredQuestions.firstOrNull { it.id == questionId }?.let {
                it.answers = newAnswers
            } ?: run {
                answeredQuestions.add(QuestionRequest(questionId, newAnswers))
                updateQuestionsContent(uiModels)
            }
        }
        (holder as? ChoiceQuestionViewHolder)?.let {
            it.choiceAnswersAdapter.pickValue = question.pick
        }
    }

    private fun updateQuestionsContent(totalQuestionUiModels: List<QuestionItemPagerUiModel>) {
        val questionsContent = mutableListOf<QuestionItemPagerUiModel>()
        for (question in totalQuestionUiModels) {
            questionsContent.add(question)
            if (question.shouldAnswer && answeredQuestions.firstOrNull {it.id == question.id } == null) {
                break
            }
        }
        currentQuestionUiModels = questionsContent
    }

    // ================= View Holders ========================= \\
    internal open inner class QuestionViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View
            get() = itemView

        open fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            tvQuestionItemText.text = questionUiModel.text.trim()
        }

        internal fun handleRating(
            rating: Float,
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            val selectedIndex = rating.toInt() - 1
            if (questionUiModel.answers.isValidIndex(selectedIndex)) {
                val answer = questionUiModel.answers[selectedIndex]
                onItemSelected(questionUiModel.id, listOf(AnswerRequest(answer.id)))
            }
        }

        internal fun handleDropdown(
            position: Int,
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            if (questionUiModel.answers.isValidIndex(position)) {
                val answer = questionUiModel.answers[position]
                onItemSelected(questionUiModel.id, listOf(AnswerRequest(answer.id)))
            }
        }

        internal fun handleAdapter(
            questionId: String,
            selectedAnswerUiModels: List<AnswerItemUiModel>,
            shouldAddText: Boolean,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            onItemSelected(
                questionId,
                selectedAnswerUiModels.map {
                    AnswerRequest(it.id, if (shouldAddText) it.text else null)
                }
            )
        }
    }

    internal inner class ChoiceQuestionViewHolder(
        itemView: View,
        val choiceAnswersAdapter: QuestionItemChoiceAdapter
    ): QuestionViewHolder(itemView) {

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            choiceAnswersAdapter.uiModels = questionUiModel.answers
            choiceAnswersAdapter.onItemsSelected = { selectedAnswers ->
                handleAdapter(questionUiModel.id, selectedAnswers,false, onItemSelected)
            }
            rvChoiceQuestionAnswers.adapter = choiceAnswersAdapter
        }
    }

    internal inner class DropdownQuestionViewHolder(
        itemView: View,
        private val dropdownAdapter: ArrayAdapter<String>
    ): QuestionViewHolder(itemView) {

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            with(dropdownAdapter) {
                setDropDownViewResource(R.layout.item_survey_questions_dropdown_answer)
                refreshWithData(questionUiModel.answers.map { answer -> answer.text })
            }
            sDropdownQuestionAnswers.adapter = dropdownAdapter
            sDropdownQuestionAnswers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) { }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    handleDropdown(position, questionUiModel, onItemSelected)
                }
            }
        }
    }

    internal inner class HeartQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            rbQuestionItemHeart.numStars = questionUiModel.answers.size
            rbQuestionItemHeart.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                handleRating(rating, questionUiModel, onItemSelected)
            }
        }
    }

    internal inner class MoneyQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            rbQuestionItemMoney.numStars = questionUiModel.answers.size
            rbQuestionItemMoney.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                handleRating(rating, questionUiModel, onItemSelected)
            }
        }
    }

    internal inner class NpsQuestionViewHolder(
        itemView: View,
        context: Context,
        val npsAnswersAdapter: QuestionItemNpsAdapter
    ): QuestionViewHolder(itemView) {

        init {
            rvNpsQuestionAnswers.addItemDecoration(SurveyQuestionNpsItemDecoration(context))
        }

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            npsAnswersAdapter.uiModels = questionUiModel.answers
            npsAnswersAdapter.onItemsSelected = { selectedAnswers ->
                handleAdapter(questionUiModel.id, selectedAnswers,false, onItemSelected)
            }
            rvNpsQuestionAnswers.adapter = npsAnswersAdapter
        }
    }

    internal inner class SliderQuestionViewHolder(
        itemView: View,
        context: Context,
        val sliderAnswersAdapter: QuestionItemSliderAdapter
    ): QuestionViewHolder(itemView) {

        init {
            rvSliderQuestionAnswers.addItemDecoration(SurveyQuestionSliderItemDecoration(context))
        }

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            sliderAnswersAdapter.uiModels = questionUiModel.answers
            sliderAnswersAdapter.onItemsSelected = { selectedAnswers ->
                handleAdapter(questionUiModel.id, selectedAnswers,false, onItemSelected)
            }
            rvSliderQuestionAnswers.adapter = sliderAnswersAdapter
        }
    }

    internal inner class SmileyQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            rbQuestionItemSmiley.numStars = questionUiModel.answers.size
            rbQuestionItemSmiley.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                handleRating(rating, questionUiModel, onItemSelected)
            }
        }
    }

    internal inner class StarQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            rbQuestionItemStar.numStars = questionUiModel.answers.size
            rbQuestionItemStar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                handleRating(rating, questionUiModel, onItemSelected)
            }
        }
    }

    internal inner class TextAreaQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            etQuestionItemTextArea.hint = questionUiModel.text
            etQuestionItemTextArea.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    questionUiModel.answers.firstOrNull()?.let {
                        val newAnswer = AnswerItemUiModel(it.id, s?.toString() ?: "")
                        handleAdapter(questionUiModel.id, listOf(newAnswer), true, onItemSelected)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            })
        }
    }

    internal inner class TextFieldQuestionViewHolder(
        itemView: View,
        val textFieldAnswersAdapter: QuestionItemTextFieldAdapter
    ): QuestionViewHolder(itemView) {

        override fun bind(
            questionUiModel: QuestionItemPagerUiModel,
            onItemSelected: (questionId: String, answers: List<AnswerRequest>) -> Unit
        ) {
            super.bind(questionUiModel, onItemSelected)
            textFieldAnswersAdapter.uiModels = questionUiModel.answers
            textFieldAnswersAdapter.onItemsTextChanged = { selectedAnswers ->
                handleAdapter(questionUiModel.id, selectedAnswers,true, onItemSelected)
            }
            rvTextFieldQuestionAnswers.adapter = textFieldAnswersAdapter
        }
    }
}
