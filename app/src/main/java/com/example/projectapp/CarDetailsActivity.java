package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

//import com.example.projectapp.databinding.ActivityCarDetailsBinding;
//import com.example.projectapp.domain.Car;

import java.util.ArrayList;
import java.util.List;

public class CarDetailsActivity extends AppCompatActivity {
   // private ActivityCarDetailsBinding binding;
  //  private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_car_details);

     //   binding = DataBindingUtil.setContentView(this,R.layout.activity_car_details);


      //  car = CarDetailsActivityArgs.fromBundle(Objects.requireNonNull(getIntent().getExtras())).getCar();
     //   car = getIntent().getParcelableExtra("car");
     //   List<CarImage> images = new ArrayList<>();
     //   images.add(new CarImage(getResources().getDrawable(R.drawable.image2)));
     //   images.add(new CarImage(getResources().getDrawable(R.drawable.image3)));
     //   images.add(new CarImage(getResources().getDrawable(R.drawable.image4)));

      //  car.setCarImages(images);

    /*    CarDetailsAdapter adapter = new CarDetailsAdapter(car.getCarImages(), new CarDetailsListener() {
            @Override
            public void onListItemClick(int position) {

            }
        });
        binding.carDetailsRecyclerView.setAdapter(adapter);*/
    }
}
