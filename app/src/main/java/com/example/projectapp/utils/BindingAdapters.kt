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
import com.example.projectapp.ui.cars.CarsApiStatus

/*
The binding adapters take care of all the work of formatting and updating the views as the data changes,
simplifying the ViewHolder and giving the code much better structure than it had before.

However, it turns out that with this new click-handling mechanism,
it is now possible for the binding adapters to get called with a null value for item.
In particular, when the app starts, (the LiveData starts as nul), so you need to add null checks to each of the adapters.

for each of the binding adapters, change the type of the item argument to nullable,
and wrap the body with item?.let{...}.
 */
@BindingAdapter("bindingCarNameAndModel")
fun TextView.setCarNameAndModel(item: Car?) {
    item?.let {
        Log.e("Binding", it.mainImageUrl)
        this.text = it.mainImageUrl
    }
    //TODO this.text = item.model
}

@BindingAdapter("bindinCarCompany")
fun TextView.setCarCompany(item: Car?) {
    //TODO this.text = item.carCompany.name
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
    val mainImageUrl = "http://192.168.1.102/img/$imageName"

    imgUrl?.let {
        val imgUri2 = mainImageUrl.toUri().buildUpon().scheme("http").build()
        // imgUri = imgUrl.toUri().buildUpon().build()
        //Log.e("Glide",imgUri.toString())
        Log.e("Glide2",mainImageUrl.toString())

        Glide.with(this.context)
                .load(mainImageUrl)
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
    Log.e("STATUS", status.toString())
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