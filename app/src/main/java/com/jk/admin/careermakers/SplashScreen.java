package com.jk.admin.careermakers;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    ImageView logoIv;
    boolean handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logoIv=(ImageView) findViewById(R.id.LogoIv);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.splash_screen);
        logoIv.setAnimation(animation);

        handler=new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                finish();
            }
        },2500);
    }
}
