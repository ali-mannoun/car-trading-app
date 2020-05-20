package com.example.projectapp.ui.cars_specifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.projectapp.R

//Change the superclass to RecyclerView.Adapter for paging through views, or FragmentStateAdapter for paging through fragments.
class CarImagesViewPagerAdapter(var mContext: Context, var mListScreen: List<ImageItem>)
    : RecyclerView.Adapter<CarImagesViewPagerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarImagesViewPagerAdapter.MyViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.car_image_slide, parent, false)
        // LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View layoutScreen = inflater.inflate(R.layout.fragment_item_slider, parent,false);

        //parent.addView(layoutScreen);
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: CarImagesViewPagerAdapter.MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mListScreen.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgSlide: ImageView

        init {
            imgSlide = itemView.findViewById(R.id.main_image_image_view)
        }

        fun bind(position: Int) {

            val imageName = mListScreen.get(position).imageUrl.drop(24) //to get image name not the full url. we use this to replace the host name website.test with the local host ip
            val mainImageUrl = "http://192.168.1.102/img/$imageName"

            imageName?.let {
                val imgUri2 = mainImageUrl.toUri().buildUpon().scheme("http").build()
                Glide.with(itemView.context)
                        .load(mainImageUrl)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.ic_broken_image_black_24dp))
                        .into(imgSlide)
            }
            //imgSlide.setImageResource(mListScreen[position].imageUrl)
        }
    }
}

class ImageItem(val imageUrl: String)