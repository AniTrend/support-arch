package io.wax911.sample.core.extension

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.wax911.sample.core.util.ThumbnailHelper
import io.wax911.sample.domain.entities.contract.TraktEntity

@BindingAdapter("imageUrl")
fun AppCompatImageView.setImageUrl(url: String?) = url?.also {
    Glide.with(context).load(url)
        .transition(DrawableTransitionOptions.withCrossFade(350))
        .apply(RequestOptions.centerCropTransform())
        .into(this)
}

@BindingAdapter("youtubeTrailer")
fun AppCompatImageView.setImageUrl(entity: TraktEntity) {
    val url = ThumbnailHelper.getThumbnailFromUrl(entity.trailer)
    Glide.with(context).load(url)
        .transition(DrawableTransitionOptions.withCrossFade(350))
        .apply(RequestOptions.centerCropTransform())
        .into(this)
}
