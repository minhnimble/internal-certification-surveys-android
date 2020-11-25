package co.nimblehq.extension

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import co.nimblehq.R
import co.nimblehq.di.modules.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Provide extension functions relates to ImageView and loading image mechanism.
 */

fun ImageView.loadImage(url: String, placeHolderDrawable: Drawable? = null) {
    GlideApp.with(context)
        .load(url)
        .placeholder(placeHolderDrawable ?: ColorDrawableCreator.default(context))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .into(this)
}

fun ImageView.loadImageWithFadeAnimation(url: String, placeHolderDrawable: Drawable? = null) {
    GlideApp.with(context)
        .load(url)
        .placeholder(placeHolderDrawable ?: ColorDrawableCreator.default(context))
        .transition(DrawableTransitionOptions.withCrossFade(1000))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .into(this)
}

object ColorDrawableCreator {
    fun default(context: Context): ColorDrawable =
        ColorDrawable(ContextCompat.getColor(context, R.color.black_20a))
}
