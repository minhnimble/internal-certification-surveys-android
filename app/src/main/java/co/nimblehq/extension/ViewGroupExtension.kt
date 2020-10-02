package co.nimblehq.extension

import android.view.ViewGroup
import co.nimblehq.data.lib.common.BLUR_RADIUS
import co.nimblehq.data.lib.common.BLUR_SAMPLING
import co.nimblehq.data.lib.common.DEFAULT_DURATION
import jp.wasabeef.blurry.Blurry

fun ViewGroup.blurView(shouldAnimate: Boolean = true, shouldPost: Boolean = false) {
    fun addBlur() {
        Blurry.with(context)
            .radius(BLUR_RADIUS)
            .sampling(BLUR_SAMPLING)
            .animate(if (shouldAnimate) DEFAULT_DURATION.toInt() else 0)
            .onto(this)
    }

    if (shouldPost) this.post { addBlur() } else addBlur()
}
