package com.example.projectapp.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.projectapp.R
import com.example.projectapp.domain.Car
import com.example.projectapp.network.CarProperty
import com.example.projectapp.network.MAIN_URL
import com.example.projectapp.ui.cars.CarsApiStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode

/*
The binding adapters take care of all the work of formatting and updating the views as the data changes,
simplifying the ViewHolder and giving the code much better structure than it had before.

However, it turns out that with this new click-handling mechanism,
it is now possible for the binding adapters to get called with a null value for item.
In particular, when the app starts, (the LiveData starts as nul), so you need to add null checks to each of the adapters.

for each of the binding adapters, change the type of the item argument to nullable,
and wrap the body with item?.let{...}.
 */
@BindingAdapter("bindingCarModel")
fun TextView.setCarModel(item: Car?) {
    item?.let {
        this.text = it.model.smartTruncate(20)
    }
}

@BindingAdapter("bindingCarBrand")
fun TextView.setCarBrand(item: Car?) {
    item?.let {
        this.text = it.brand
    }
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun ImageView.bindImage(imgUrl: String?) {
    val imageName = imgUrl?.drop(24) //to get image name not the full url. we use this to replace the host name website.test with the local host ip
    val mainImageUrl = MAIN_URL.plus("img/$imageName")

    imgUrl?.let {
        val imgUri2 = mainImageUrl.toUri().buildUpon().scheme("http").build()
        Glide.with(this.context)
                .load(imgUri2)
                .apply(RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image_black_24dp))
                .into(this)
    }
}

/**
 * This binding adapter displays the [CarsApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */

@BindingAdapter("carsApiStatus")
fun ImageView.bindStatus(status: CarsApiStatus?) {
    when (status) {
        CarsApiStatus.LOADING -> {
            this.visibility = View.VISIBLE
            this.setImageResource(R.drawable.loading_animation)
        }
        CarsApiStatus.ERROR -> {
            this.visibility = View.VISIBLE
            this.setImageResource(R.drawable.ic_connection_error_24dp)
        }
        CarsApiStatus.DONE -> {
            this.visibility = View.GONE
        }
    }
}

/**
 * Binding adapter used to hide the spinner once data is available.
 */
@BindingAdapter("isNetworkError", "playlist")
fun View.hideIfNetworkError(isNetWorkError: Boolean, playlist: Any?) {
    visibility = if (playlist != null) View.GONE else View.VISIBLE
    //TODO
    if (isNetWorkError) {
        visibility = View.GONE
    }
}

/**
 * Binding adapter used to show the bottom navigation view when car,profile,recommended and hide otherwise.
 */
@BindingAdapter("bottomNavVisibility")
fun BottomNavigationView.controlVisibility(isVisible : Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}