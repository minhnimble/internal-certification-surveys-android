package co.nimblehq.ui.screen.main.surveydetails.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.nimblehq.R

class SurveyQuestionNpsItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {
    val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.fg_general_vertical_divider)!!

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.adapter!!.itemCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            if (child != null) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                val left = child.right + params.rightMargin
                val right = left + drawable.intrinsicWidth
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(c)
            }
        }
    }
}
