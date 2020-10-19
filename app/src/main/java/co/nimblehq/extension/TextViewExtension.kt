package co.nimblehq.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.widget.TextView
import co.nimblehq.data.lib.common.DEFAULT_DURATION

fun TextView.switchTextWithFadeAnimation(newText: String) {
    animate()
        .alpha(0f)
        .setDuration(DEFAULT_DURATION / 5)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                text = newText
                animate()
                    .alpha(1f)
                    .setDuration(DEFAULT_DURATION / 4)
                    .setListener(null)
            }
        })
}
