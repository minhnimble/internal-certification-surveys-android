package co.nimblehq.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import co.nimblehq.data.lib.common.DEFAULT_DURATION

fun View.startFadeInAnimation(duration: Long = DEFAULT_DURATION, executeOnAnimationEnd: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                executeOnAnimationEnd?.invoke()
            }
        })
}

fun View.startFadeOutAnimation(duration: Long = DEFAULT_DURATION, executeOnAnimationEnd: (() -> Unit)? = null) {
    alpha = 1f
    visibility = View.VISIBLE
    animate()
        .alpha(0f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
                executeOnAnimationEnd?.invoke()
            }
        })
}

fun View.isShowing(): Boolean = visibility == View.VISIBLE && alpha == 1f

fun View.isNotShowing(): Boolean = !isShowing()

