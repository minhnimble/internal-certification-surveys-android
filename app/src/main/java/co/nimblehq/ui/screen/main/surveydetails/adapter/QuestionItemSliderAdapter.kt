package co.nimblehq.ui.screen.main.surveydetails.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R
import co.nimblehq.ui.common.adapter.DiffUpdateAdapter
import co.nimblehq.ui.screen.main.surveydetails.uimodel.AnswerItemUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_survey_questions_slider_answer.*
import kotlin.properties.Delegates

internal class QuestionItemSliderAdapter :
    RecyclerView.Adapter<QuestionItemSliderAdapter.QuestionSliderAnswerViewHolder>(),
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

    private var selectedId: String = ""

    override fun getItemCount() = uiModels.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_survey_questions_slider_answer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionSliderAnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return QuestionSliderAnswerViewHolder(view, this)
    }

    override fun onTap(position: Int) {
        val answer = uiModels[position]
         if (selectedId != answer.id) {
             selectedId = answer.id
             notifyDataSetChanged()
             onItemsSelected?.invoke(listOf(answer))
        }
    }

    override fun onBindViewHolder(holder: QuestionSliderAnswerViewHolder, position: Int) {
        holder.bind(uiModels[position], selectedId)
    }

    internal inner class QuestionSliderAnswerViewHolder(
        itemView: View,
        private val clickListener: ViewHolderClickListener
    ) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer
    {

        override val containerView: View
            get() = itemView

        init {
            clSliderQuestionItemAnswerViewHolder.setOnClickListener {
                clickListener.onTap(adapterPosition)
            }
        }

        fun bind(uiModel: AnswerItemUiModel, selectedId: String) {
            with(uiModel) {
                tvSliderQuestionItemAnswerTitle.text = text
                tvSliderQuestionItemAnswerTitle.typeface = if (selectedId == id) {
                    Typeface.DEFAULT_BOLD
                } else {
                    Typeface.DEFAULT
                }
            }
        }
    }
}
