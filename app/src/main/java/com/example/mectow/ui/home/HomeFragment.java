package com.example.mectow.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mectow.Cartow_Question;
import com.example.mectow.Mechanic_Questions;
import com.example.mectow.R;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

public class HomeFragment extends Fragment {
    private SliderLayout sliderLayout;
    private HomeViewModel homeViewModel;
    Button mechanicbtn,cartowbtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mechanicbtn = (Button) view.findViewById(R.id.mechanic_icon);
        cartowbtn=(Button) view.findViewById(R.id.cartow_icon);
        cartowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Cartow_Question.class);
                startActivity(intent);
            }
        });
        mechanicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Mechanic_Questions.class);
                startActivity(intent);
            }
        });
        sliderLayout=view.findViewById(R.id.slider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(1);

        setsliderviews();
        final TextView textView = view.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return view;
    }

    private void setsliderviews() {
        for (int i = 0; i < 5; i++) {
            DefaultSliderView sliderView = new DefaultSliderView(getContext());
            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/mectow-b5e8d.appspot.com/o/img1.jpeg?alt=media&token=e541567f-9923-4540-952e-69b776159bd6");
                    break;
                case 1:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/mectow-b5e8d.appspot.com/o/img2.jpg?alt=media&token=68abf58e-7669-4ce5-9c7e-e3b39bf82623");
                    break;
                case 2:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/mectow-b5e8d.appspot.com/o/img3.png?alt=media&token=6897f88c-3d8e-43cf-8769-be41d5bab344");
                    break;
                case 3:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/mectow-b5e8d.appspot.com/o/img4.jpg?alt=media&token=ca0e285c-4f22-4b8e-9426-51e719b7cceb");
                    break;
                case 4:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/mectow-b5e8d.appspot.com/o/img5.jpg?alt=media&token=89a8dbc7-4ad2-4467-a0b0-da8bd7663fcb");
                    break;


            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderLayout.addSliderView(sliderView);
        }
    }
}