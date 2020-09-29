package co.nimblehq.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import co.nimblehq.data.lib.common.Const

fun View.startFadeInAnimation(shouldAnimate: Boolean = true, executeOnAnimationEnd: (() -> Unit)? = null) {
    alpha = 0f
    animate()
            .alpha(1f)
            .setDuration(if (shouldAnimate) Const.Animation.DURATION else 0)
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
            .setDuration(Const.Animation.DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = View.GONE
                }
            })
}
