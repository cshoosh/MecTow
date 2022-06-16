package com.example.mectow.ui.about_us;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.mectow.R;
import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        aboutViewModel =
                ViewModelProviders.of(this).get(AboutViewModel.class);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_nav_about_us, container, false);
//        final TextView textView = viewGroup.findViewById(R.id.text_about_us);
//        aboutViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

//        View aboutPage = new AboutPage(getActivity())
//                .isRTL(false)
//                .setDescription("WELCOME TO MECTOW : NO. 1 CAR TOW SERVICE, NO. 1 MECHANIC SERVICE\n" +
//                        "MECTOW IS FAST AND GIVES IMMEDIATE RESPONSES TO THE CUSTOMERS AND PROVIDES CERTIFIED MECHANICS FOR CARS, BIKES, AND ALL VEHICLE , CAR TOWING AND CAR RECOVERY, MECTOW ALWAYS UNDERSTANDS THE REQUIREMENTS OF THE CUSTOMER AND WORKS ON IT ON A DAILY BASIS.\n")
//                .addItem(adsElement)
//                .addGroup("Connect with us")
//                .addEmail("mectow20@gmail.com")
//                .create();
//
//        viewGroup.addView(aboutPage);
        return viewGroup;
    }

//    private Element createCopyright()
//    {
//        Element copyright = new Element();
//        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Your Name", Calendar.getInstance().get(Calendar.YEAR));
//        copyright.setTitle(copyrightString);
//        // copyright.setIcon(R.mipmap.ic_launcher);
//        copyright.setGravity(Gravity.CENTER);
//        copyright.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),copyrightString,Toast.LENGTH_SHORT).show();
//            }
//        });
//        return copyright;
//  }
}
