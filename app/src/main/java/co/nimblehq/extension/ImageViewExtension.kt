package co.nimblehq.extension

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import co.nimblehq.R
import co.nimblehq.di.modules.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Provide extension functions relates to ImageView and loading image mechanism.
 */

fun ImageView.loadImage(url: String) {
    GlideApp.with(context)
        .load(url)
        .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.black_20a)))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .into(this)
}

fun ImageView.loadImageWithFadeAnimation(url: String) {
    GlideApp.with(context)
        .load(url)
        .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.black_20a)))
        .transition(DrawableTransitionOptions.withCrossFade(1000))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .into(this)

}
