package co.nimblehq.ui.screen.main.surveys.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R
import com.swd.edeeremit.ui.onboarding.introduction.adapter.SurveysPagerItemUiModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_surveys_pager_content.*

internal class SurveysPagerAdapter(
    private val SurveysPagerItems: List<SurveysPagerItemUiModel>
) : RecyclerView.Adapter<SurveysPagerAdapter.SurveysViewHolder>() {

    override fun getItemCount() = SurveysPagerItems.size

    override fun getItemViewType(position: Int) = R.layout.item_surveys_pager_content

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveysViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return SurveysViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurveysViewHolder, position: Int) {
        holder.bind(SurveysPagerItems[position])
    }

    internal inner class SurveysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {

        override val containerView: View
            get() = itemView

        fun bind(uiModel: SurveysPagerItemUiModel) {
            with(uiModel) {
                ivSurveysItemBackground.setImageResource(imageDrawable)
                tvSurveysItemHeader.text = title
                tvSurveysItemDescription.text = subtitle
            }
        }
    }
}
