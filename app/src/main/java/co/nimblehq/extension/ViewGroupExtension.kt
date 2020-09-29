package co.nimblehq.extension

import android.view.ViewGroup
import co.nimblehq.data.lib.common.Const
import jp.wasabeef.blurry.Blurry

fun ViewGroup.addBlurWithAnimation(shouldAnimate: Boolean = true, shouldPost: Boolean = false) {
    fun addBlur() {
        Blurry.with(context)
                .radius(25)
                .sampling(2)
                .animate(if (shouldAnimate) Const.Animation.DURATION.toInt() else 0)
                .onto(this)
    }

    if (shouldPost) this.post { addBlur() } else addBlur()
}
