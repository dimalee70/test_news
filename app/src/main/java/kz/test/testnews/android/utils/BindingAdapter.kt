package kz.test.testnews.android.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kz.test.testnews.R
import kz.test.testnews.android.modules.news.presentation.WebView

@BindingAdapter("setImage")
fun ImageView.setImage(url: String?){
    if(url != null) {
        com.bumptech.glide.Glide.with(context)
            .load(url)
            .apply(createOptionForGlide())
            .into(this)
    }
}

@BindingAdapter("visibility")
fun TextView.setVisibility(visibility: Boolean){
    if(visibility) {
        this.visibility = View.VISIBLE
        return
    }
    this.visibility = View.GONE
}

fun createOptionForGlide(): RequestOptions {
    return RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_animation)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()
}

@BindingAdapter("loadUrl")
fun android.webkit.WebView.loadUrl(url: String){
    settings.javaScriptEnabled = true
    loadUrl(url)
}