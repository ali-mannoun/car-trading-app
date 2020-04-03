package com.example.projectapp.ui.cars_details;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectapp.R;
import com.example.projectapp.databinding.FragmentCarDetailsBinding;

public class UserCarDetailsFragment extends Fragment {
    private FragmentCarDetailsBinding binding;


    public UserCarDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_details, container, false);

      /*  Car car = new Car("this is edited model", new Company("Ali company"));
        List<CarImage> images = new ArrayList<>();
        images.add(new CarImage(getContext().getDrawable(R.drawable.image2)));
        images.add(new CarImage(getContext().getDrawable(R.drawable.image3)));
        images.add(new CarImage(getContext().getDrawable(R.drawable.image4)));
        images.add(new CarImage(getContext().getDrawable(R.drawable.image5)));
        images.add(new CarImage(getContext().getDrawable(R.drawable.image6)));
        images.add(new CarImage(getContext().getDrawable(R.drawable.image7)));
        images.add(new CarImage(getContext().getDrawable(R.drawable.image8)));
*/
      //  car.setCarImages(images);
      /*  UserCarDetailsAdapter adapter = new UserCarDetailsAdapter(car.getCarImages(), new CarDetailsListener() {
            @Override
            public void onListItemClick(int position) {
                // Toast.makeText(getContext(), "details", Toast.LENGTH_SHORT).show();
            }
        });
*/
     //   binding.carImageDetailsRv.setAdapter(adapter);


        return binding.getRoot();
    }

}
