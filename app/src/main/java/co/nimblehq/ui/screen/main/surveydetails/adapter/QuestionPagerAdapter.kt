package co.nimblehq.ui.screen.main.surveydetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R
import co.nimblehq.data.model.QuestionDisplayType
import co.nimblehq.ui.common.adapter.DiffUpdateAdapter
import co.nimblehq.ui.screen.main.surveydetails.decoration.SurveyQuestionNpsItemDecoration
import co.nimblehq.ui.screen.main.surveydetails.decoration.SurveyQuestionSliderItemDecoration
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

    var uiModels: List<QuestionItemPagerUiModel> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(
            old,
            new,
            { oldItem, newItem -> oldItem.id == newItem.id }
        )
    }

    override fun getItemCount() = uiModels.size

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
        val question = uiModels[position]
        holder.bind(question)
        (holder as? ChoiceQuestionViewHolder)?.let {
            it.choiceAnswersAdapter.pickValue = question.pick
        }
    }

    // ================= View Holders ========================= \\
    internal open inner class QuestionViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View
            get() = itemView

        open fun bind(uiModel: QuestionItemPagerUiModel) {
            tvQuestionItemText.text = uiModel.text.trim()
        }
    }

    internal inner class ChoiceQuestionViewHolder(
        itemView: View,
        val choiceAnswersAdapter: QuestionItemChoiceAdapter
    ): QuestionViewHolder(itemView) {

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            choiceAnswersAdapter.uiModels = uiModel.answers
            rvChoiceQuestionAnswers.adapter = choiceAnswersAdapter
        }
    }

    internal inner class DropdownQuestionViewHolder(
        itemView: View,
        val dropdownAdapter: ArrayAdapter<String>
    ): QuestionViewHolder(itemView), AdapterView.OnItemSelectedListener {

        override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

        }

        override fun onNothingSelected(arg0: AdapterView<*>) {

        }

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            dropdownAdapter.setDropDownViewResource(R.layout.item_survey_questions_dropdown_answer)
            dropdownAdapter.clear()
            dropdownAdapter.addAll(uiModel.answers.map { answer -> answer.text })
            dropdownAdapter.notifyDataSetChanged()
            sDropdownQuestionAnswers.adapter = dropdownAdapter
            sDropdownQuestionAnswers.onItemSelectedListener = this
        }
    }

    internal inner class HeartQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            rbQuestionItemHeart.numStars = uiModel.answers.size
        }
    }

    internal inner class MoneyQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            rbQuestionItemMoney.numStars = uiModel.answers.size
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

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            npsAnswersAdapter.uiModels = uiModel.answers
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

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            sliderAnswersAdapter.uiModels = uiModel.answers
            rvSliderQuestionAnswers.adapter = sliderAnswersAdapter
        }
    }

    internal inner class SmileyQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            rbQuestionItemSmiley.numStars = uiModel.answers.size
        }
    }

    internal inner class StarQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            rbQuestionItemStar.numStars = uiModel.answers.size
        }
    }

    internal inner class TextAreaQuestionViewHolder(itemView: View): QuestionViewHolder(itemView) {

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            etQuestionItemTextArea.hint = uiModel.text
        }
    }

    internal inner class TextFieldQuestionViewHolder(
        itemView: View,
        val textFieldAnswersAdapter: QuestionItemTextFieldAdapter
    ): QuestionViewHolder(itemView) {

        override fun bind(uiModel: QuestionItemPagerUiModel) {
            super.bind(uiModel)
            textFieldAnswersAdapter.uiModels = uiModel.answers
            rvTextFieldQuestionAnswers.adapter = textFieldAnswersAdapter
        }
    }
}
