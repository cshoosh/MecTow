package com.example.mectow;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splashscreen extends AppCompatActivity {
    ImageView Splashlogo,Logobeat,Logoheader,Logobottom;
    TextView textLogo;
    CharSequence charSequence;
    int index;
    long delay=50;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Splashlogo=findViewById(R.id.splashlogo);
        Logobeat=findViewById(R.id.logobeat);
        Logoheader=findViewById(R.id.logoheader);
        Logobottom=findViewById(R.id.logobottom);
        textLogo=findViewById(R.id.textlogo);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation animation1= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        Splashlogo.setAnimation(animation1);

        ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(Logoheader,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f));

        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        animatText("It's Ready to Get Back on It's Track");
        Animation animation2= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        Logobottom.setAnimation(animation2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splashscreen.this,login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        },4000);

    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            textLogo.setText(charSequence.subSequence(0,index++));
            if (index<=charSequence.length()){
                handler.postDelayed(runnable,delay);
            }
        }
    };
    public void animatText(CharSequence cs){
        charSequence=cs;
        index=0;
        textLogo.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);
    }

}
