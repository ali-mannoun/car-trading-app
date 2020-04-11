package com.example.projectapp.ui.cars_details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.CarDetailsListener;
import com.example.projectapp.R;

import java.util.List;

public class UserCarDetailsAdapter extends RecyclerView.Adapter<UserCarDetailsAdapter.DetailsViewHolder> {
   // private List<CarImage> images;
    private CarDetailsListener listener;

  //  public UserCarDetailsAdapter(List<CarImage> images, CarDetailsListener listener) {
   //     this.images = images;
   //     this.listener = listener;
   // }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_car_details, parent,false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.car_image_iv);
            itemView.setOnClickListener(this);
        }

        private void bind(int position) {
        }

        @Override
        public void onClick(View view) {
            listener.onListItemClick(getAdapterPosition());
        }
    }
}
