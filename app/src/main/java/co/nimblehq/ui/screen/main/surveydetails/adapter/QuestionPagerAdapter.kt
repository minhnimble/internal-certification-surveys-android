package co.nimblehq.ui.screen.main.surveydetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R
import co.nimblehq.data.model.QuestionDisplayType
import co.nimblehq.ui.common.adapter.DiffUpdateAdapter
import co.nimblehq.ui.screen.main.surveydetails.decoration.SurveyQuestionNpsItemDecoration
import co.nimblehq.ui.screen.main.surveydetails.uimodel.QuestionItemPagerUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_survey_questions_choice.*
import kotlinx.android.synthetic.main.item_survey_questions_default.tvQuestionItemText
import kotlinx.android.synthetic.main.item_survey_questions_heart.*
import kotlinx.android.synthetic.main.item_survey_questions_nps.*
import kotlinx.android.synthetic.main.item_survey_questions_smiley.*
import kotlinx.android.synthetic.main.item_survey_questions_star.*
import kotlinx.android.synthetic.main.item_survey_questions_text_area.*
import kotlinx.android.synthetic.main.item_survey_questions_text_field.*
import kotlin.properties.Delegates

internal class QuestionPagerAdapter : RecyclerView.Adapter<QuestionPagerAdapter.QuestionViewHolder>(), DiffUpdateAdapter {

    var uiModels: List<QuestionItemPagerUiModel> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(
            old,
            new,
            { oldItem, newItem -> oldItem.id == newItem.id }
        )
    }

    private var choiceAnswersAdapters: MutableMap<String, QuestionItemChoiceAdapter> = mutableMapOf()
    private var npsAnswersAdapters: MutableMap<String, QuestionItemNpsAdapter> = mutableMapOf()
    private var textFieldAdapters: MutableMap<String, QuestionItemTextFieldAdapter> = mutableMapOf()

    override fun getItemCount() = uiModels.size

    override fun getItemViewType(position: Int): Int {
        return when (uiModels[position].displayType) {
            QuestionDisplayType.STAR -> R.layout.item_survey_questions_star
            QuestionDisplayType.HEART -> R.layout.item_survey_questions_heart
            QuestionDisplayType.SMILEY -> R.layout.item_survey_questions_smiley
            QuestionDisplayType.CHOICE -> R.layout.item_survey_questions_choice
            QuestionDisplayType.NPS -> R.layout.item_survey_questions_nps
            QuestionDisplayType.TEXTAREA -> R.layout.item_survey_questions_text_area
            QuestionDisplayType.TEXTFIELD -> R.layout.item_survey_questions_text_field
            else -> R.layout.item_survey_questions_default
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_survey_questions_choice -> QuestionViewHolder(view, choiceAnswersAdapter = QuestionItemChoiceAdapter())
            R.layout.item_survey_questions_nps -> QuestionViewHolder(view, context = parent.context, npsAnswersAdapter = QuestionItemNpsAdapter())
            R.layout.item_survey_questions_text_field -> QuestionViewHolder(view, textFieldAnswersAdapter = QuestionItemTextFieldAdapter())
            else -> QuestionViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = uiModels[position]
        holder.bind(question)
        holder.choiceAnswersAdapter?.let {
            choiceAnswersAdapters[question.id] = it
        }
        holder.npsAnswersAdapter?.let {
            npsAnswersAdapters[question.id] = it
        }
        holder.textFieldAnswersAdapter?.let {
            textFieldAdapters[question.id] = it
        }
    }

    internal inner class QuestionViewHolder(
        itemView: View,
        context: Context? = null,
        val choiceAnswersAdapter: QuestionItemChoiceAdapter? = null,
        val npsAnswersAdapter: QuestionItemNpsAdapter? = null,
        val textFieldAnswersAdapter: QuestionItemTextFieldAdapter? = null,
    ) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer
    {

        override val containerView: View
            get() = itemView

        init {
            if (npsAnswersAdapter != null && context != null) {
                rvNpsQuestionAnswers.addItemDecoration(SurveyQuestionNpsItemDecoration(context))
            }
        }

        fun bind(uiModel: QuestionItemPagerUiModel) {
            with(uiModel) {
                tvQuestionItemText.text = text.trim()
                when (displayType) {
                    QuestionDisplayType.STAR -> {
                        rbQuestionItemStar.numStars = answers.size
                    }
                    QuestionDisplayType.HEART -> {
                        rbQuestionItemHeart.numStars = answers.size
                    }
                    QuestionDisplayType.SMILEY -> {
                        rbQuestionItemSmiley.numStars = answers.size
                    }
                    QuestionDisplayType.CHOICE -> {
                        choiceAnswersAdapter?.let {
                            it.uiModels = answers
                            rvChoiceQuestionAnswers.adapter = it
                        }
                    }
                    QuestionDisplayType.NPS -> {
                        npsAnswersAdapter?.let {
                            it.uiModels = answers
                            rvNpsQuestionAnswers.adapter = it
                        }
                    }
                    QuestionDisplayType.TEXTAREA -> {
                        etQuestionItemTextArea.hint = text
                    }
                    QuestionDisplayType.TEXTFIELD -> {
                        textFieldAnswersAdapter?.let {
                            it.uiModels = answers
                            rvTextFieldQuestionAnswers.adapter = it
                        }
                    }
                    else -> { }
                }
            }
        }
    }
}
