package co.nimblehq.ui.screen.main.surveydetails.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.ui.common.adapter.DiffUpdateAdapter
import co.nimblehq.ui.screen.main.surveydetails.uimodel.AnswerItemUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_survey_questions_choice_answer.clChoiceQuestionItemAnswerViewHolder
import kotlinx.android.synthetic.main.item_survey_questions_nps_answer.*
import kotlin.properties.Delegates

internal class QuestionItemNpsAdapter :
    RecyclerView.Adapter<QuestionItemNpsAdapter.QuestionNpsAnswerViewHolder>(),
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

    private var selectedIndex: Int = DEFAULT_UNSELECTED_INDEX

    override fun getItemCount() = uiModels.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_survey_questions_nps_answer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionNpsAnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return QuestionNpsAnswerViewHolder(view, this)
    }

    override fun onTap(position: Int) {
        if (selectedIndex != position) {
            selectedIndex = position
            notifyDataSetChanged()
            onItemsSelected?.invoke(listOf(uiModels[selectedIndex]))
        }
    }

    override fun onBindViewHolder(holder: QuestionNpsAnswerViewHolder, position: Int) {
        holder.bind(uiModels[position], position, selectedIndex)
    }

    internal inner class QuestionNpsAnswerViewHolder(
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

        fun bind(uiModel: AnswerItemUiModel, currentIndex: Int, selectedIndex: Int) {
            with(uiModel) {
                tvNpsQuestionItemAnswerTitle.text = text
                if (currentIndex > selectedIndex) {
                    tvNpsQuestionItemAnswerTitle.typeface = Typeface.DEFAULT
                } else {
                    tvNpsQuestionItemAnswerTitle.typeface = Typeface.DEFAULT_BOLD
                }
            }
        }
    }
}
