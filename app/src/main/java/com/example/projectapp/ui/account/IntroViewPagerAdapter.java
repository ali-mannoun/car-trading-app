package com.example.projectapp.ui.account;
/*
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.R;

import java.util.List;
//Change the superclass to RecyclerView.Adapter for paging through views, or FragmentStateAdapter for paging through fragments.
public class IntroViewPagerAdapter extends RecyclerView.Adapter<IntroViewPagerAdapter.MyViewHolder> {
    Context mContext;
    List<ScreenItem> mListScreen;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_item_slider,parent,false);
       // LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View layoutScreen = inflater.inflate(R.layout.fragment_item_slider, parent,false);


        //parent.addView(layoutScreen);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mListScreen.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSlide;
        TextView title;
        TextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             imgSlide = itemView.findViewById(R.id.imageView);
             title = itemView.findViewById(R.id.title);
             description = itemView.findViewById(R.id.desc);
        }

        public void bind(int position) {
            title.setText(mListScreen.get(position).getTitle());
            description.setText(mListScreen.get(position).getDescription());
            imgSlide.setImageResource(mListScreen.get(position).getScreenImg());
        }
    }
}*/