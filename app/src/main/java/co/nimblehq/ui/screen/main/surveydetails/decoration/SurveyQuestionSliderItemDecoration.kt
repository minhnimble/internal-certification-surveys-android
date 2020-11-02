package co.nimblehq.ui.screen.main.surveydetails.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R

class SurveyQuestionSliderItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {
    val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.fg_general_divider)!!

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.adapter!!.itemCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            if (child != null) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + drawable.intrinsicHeight
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(c)
            }
        }
    }
}
