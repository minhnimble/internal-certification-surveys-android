package co.nimblehq.ui.screen.main.surveydetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R
import co.nimblehq.data.model.QuestionPickValue
import co.nimblehq.ui.common.adapter.DiffUpdateAdapter
import co.nimblehq.ui.screen.main.surveydetails.uimodel.AnswerItemUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_survey_questions_choice_answer.*
import timber.log.Timber
import kotlin.properties.Delegates

interface ViewHolderClickListener {
    fun onTap(position : Int)
}

internal class QuestionItemChoiceAdapter :
    RecyclerView.Adapter<QuestionItemChoiceAdapter.QuestionChoiceAnswerViewHolder>(),
    DiffUpdateAdapter,
    ViewHolderClickListener
{
    var uiModels: List<AnswerItemUiModel> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(
            old,
            new,
            { oldItem, newItem -> oldItem.id == newItem.id }
        )
    }

    var onItemsSelected: ((answerUiModels: List<AnswerItemUiModel>) -> Unit)? = null

    var pickValue: QuestionPickValue = QuestionPickValue.NONE

    private val selectedIds: MutableList<String> = mutableListOf()

    override fun getItemCount() = uiModels.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_survey_questions_choice_answer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionChoiceAnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return QuestionChoiceAnswerViewHolder(view, this)
    }

    override fun onTap(position: Int) {
        val answer = uiModels[position]
        when (pickValue) {
            QuestionPickValue.ANY -> {
                if (selectedIds.contains(answer.id)) {
                    selectedIds.remove(answer.id)
                } else {
                    selectedIds.add(answer.id)
                }
            }
            QuestionPickValue.ONE -> {
                selectedIds.clear()
                selectedIds.add(answer.id)
            }
            else -> Timber.d("No need to handle for this case")
        }
        onItemsSelected?.invoke(uiModels.filter { selectedIds.contains(it.id) })
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: QuestionChoiceAnswerViewHolder, position: Int) {
        holder.bind(uiModels[position], selectedIds)
    }

    internal inner class QuestionChoiceAnswerViewHolder(
        itemView: View,
        private val clickListener: ViewHolderClickListener
    ) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer
    {

        override val containerView: View
            get() = itemView

        init {
            clChoiceQuestionItemAnswerViewHolder.setOnClickListener {
                clickListener.onTap(adapterPosition)
            }
        }

        fun bind(uiModel: AnswerItemUiModel, selectedIds: List<String>) {
            with(uiModel) {
                tvChoiceQuestionItemAnswerTitle.text = text
                ivChoiceQuestionItemAnswerState.setImageResource(
                    if (selectedIds.contains(id)) {
                        R.drawable.bg_survey_questions_choice_answer_checked
                    } else {
                        R.drawable.bg_survey_questions_choice_answer_unchecked
                    }
                )
            }
        }
    }
}
