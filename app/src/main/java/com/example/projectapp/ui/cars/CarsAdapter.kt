package com.example.projectapp.ui.cars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectapp.R
import com.example.projectapp.databinding.ListItemCarBinding
import com.example.projectapp.network.CarProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarsAdapter(val clickListener: CarsListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(CarsDiffCallback()) {
//Delete the override of getItemCount(), because the ListAdapter implements this method for you.

    /*
    The Adapter displays data items in views, so you could handle clicks in the adapter.
    However, the adapter's job is to adapt data for display, not deal with app logic.

     So, you have taken a click listener from the adapter constructor,
     and passed it all the way to the view holder and into the binding object.
     */
    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1
    private val adapterScope = CoroutineScope(Dispatchers.Default)
/*
It doesn't matter much for a short list with one header,
but you should not do list manipulation in addHeaderAndSubmitList() on the UI thread.
Imagine a list with hundreds of items, multiple headers, and logic to decide where items need to be inserted.
This work belongs in a coroutine.
 */
    fun addHeaderAndSubmitList(list: List<CarProperty>?) {
    //uses coroutines to add the header to the dataset and then calls submitList().
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.CarItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> CarViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CarViewHolder -> {
                val carItem = getItem(position) as DataItem.CarItem
                holder.bind(clickListener, carItem.car)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CarItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class CarViewHolder private constructor(val binding: ListItemCarBinding)
        : RecyclerView.ViewHolder(binding.root) {
        //The best pace to get information about one clicked item is in the ViewHolder object, since it represents one list item.
        //While the ViewHolder is a great place to listen for clicks, it's not usually the right place to handle them.
        fun bind(clickListener: CarsListener, item: CarProperty) {
            //here we put in this way because we use binding adapters and set the values in the xml file using data bindings
            binding.carData = item
            binding.clickListener = clickListener
            /*
            This call is an optimization that asks data binding to execute any pending bindings right away.
            It's always a good idea to call executePendingBindings() when you use binding adapters in a RecyclerView,
            because it can slightly speed up sizing the views.
            */
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CarViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCarBinding.inflate(layoutInflater, parent, false)

                return CarViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class CarsDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

/**
 * Giving the lambda that handles the click a name, clickListener ,
 * helps keep track of it as it is passed between classes.
 * The clickListener callback only needs the night.nightId to access data from the database.
 */
class CarsListener(val clickListener: (carId: Long) -> Unit) {
    fun onClick(car: CarProperty) = clickListener(car.id.toLong())
}
/*
A sealed class defines a closed type, which means that all subclasses of DataItem must be defined in this file.
As a result, the number of subclasses is known to the compiler.
It's not possible for another part of your code to define a new type of DataItem that could break your adapter.
 */
sealed class DataItem {
    data class CarItem(val car: CarProperty) : DataItem() {
        override val id = car.id.toLong()
    }
/*
The second class is Header, to represent a header.
Since a header has no actual data, you can declare it as an object.
That means there will only ever be one instance of Header. Again, have it extend DataItem.
 */
    object Header : DataItem() {
    //So,very small values , this will never conflict with any nightId in existence.
        override val id = Long.MIN_VALUE
    }
/*
When the adapter uses DiffUtil to determine whether and how an item has changed,
the DiffItemCallback needs to know the id of each item.
 */
    abstract val id: Long
}