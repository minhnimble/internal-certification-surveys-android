package co.nimblehq.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import co.nimblehq.data.lib.common.DEFAULT_DURATION

fun View.startFadeInAnimation(shouldAnimate: Boolean = true, executeOnAnimationEnd: (() -> Unit)? = null) {
    alpha = 0f
    animate()
        .alpha(1f)
        .setDuration(if (shouldAnimate) DEFAULT_DURATION else 0)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                executeOnAnimationEnd?.invoke()
            }
        })
}

fun View.startFadeOutAnimation(executeOnAnimationEnd: (() -> Unit)? = null) {
    alpha = 0f
    animate()
        .alpha(1f)
        .setDuration(DEFAULT_DURATION)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
}
