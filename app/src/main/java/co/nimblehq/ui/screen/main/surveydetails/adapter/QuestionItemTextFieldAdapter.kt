package co.nimblehq.ui.screen.main.surveydetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R
import co.nimblehq.ui.common.adapter.DiffUpdateAdapter
import co.nimblehq.ui.screen.main.surveydetails.uimodel.AnswerItemUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_survey_questions_text_field_answer.*
import kotlin.properties.Delegates

internal class QuestionItemTextFieldAdapter :
    RecyclerView.Adapter<QuestionItemTextFieldAdapter.QuestionTextFieldAnswerViewHolder>(),
    DiffUpdateAdapter
{
    var uiModels: List<AnswerItemUiModel> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(
            old,
            new,
            { oldItem, newItem -> oldItem.id == newItem.id }
        )
    }

    var onItemsTextChanged: ((answerUiModels: List<AnswerItemUiModel>) -> Unit)? = null

    private val answers: MutableList<AnswerItemUiModel> = mutableListOf()

    override fun getItemCount() = uiModels.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_survey_questions_text_field_answer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionTextFieldAnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return QuestionTextFieldAnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionTextFieldAnswerViewHolder, position: Int) {
        holder.bind(uiModels[position])
    }

    internal inner class QuestionTextFieldAnswerViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer
    {

        override val containerView: View
            get() = itemView

        fun bind(uiModel: AnswerItemUiModel) {
            etTextFieldQuestionItemAnswer.hint = uiModel.text
            etTextFieldQuestionItemAnswer.doAfterTextChanged { editable ->
                val newAnswerText = editable?.toString().orEmpty()
                answers.firstOrNull { it.id == uiModel.id }?.let {
                    it.text = newAnswerText
                } ?: run {
                    answers.add(AnswerItemUiModel(uiModel.id, newAnswerText))
                }
                onItemsTextChanged?.invoke(answers)
            }
        }
    }
}
