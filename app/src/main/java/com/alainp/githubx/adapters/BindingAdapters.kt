package com.alainp.githubx.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.alainp.githubx.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    val glide =
        if (url.isNullOrEmpty()) {
            Glide.with(view.context).load(R.drawable.ic_baseline_person_24)
        } else {
            Glide.with(view.context).load(url)
        }

    glide.transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(R.drawable.ic_baseline_person_24)
        .circleCrop()
        .into(view)
}