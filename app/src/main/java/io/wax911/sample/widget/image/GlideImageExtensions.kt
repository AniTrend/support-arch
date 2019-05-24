package io.wax911.sample.widget.image

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun AppCompatImageView.setImageUrl(url: String?) = url?.also {
    Glide.with(context).load(url)
        .transition(DrawableTransitionOptions.withCrossFade(350))
        .apply(RequestOptions.centerCropTransform())
        .into(this)
}
