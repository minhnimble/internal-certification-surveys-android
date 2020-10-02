package co.nimblehq.extension

import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import co.nimblehq.data.lib.common.DEFAULT_DURATION

fun ConstraintLayout.animateResource(resId: Int, toTopOfResId: Int, shouldAnimate: Boolean = true) {
    val constraintSet = ConstraintSet()
    constraintSet.clone(this)
    constraintSet.connect(resId, ConstraintSet.BOTTOM, toTopOfResId, ConstraintSet.TOP)
    val transition = AutoTransition()
    transition.duration = if (shouldAnimate) DEFAULT_DURATION else 0
    transition.interpolator = FastOutSlowInInterpolator()
    TransitionManager.beginDelayedTransition(this, transition)
    constraintSet.applyTo(this)
}
