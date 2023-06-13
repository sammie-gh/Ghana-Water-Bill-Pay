package com.gh.sammie.ghanawater.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gh.sammie.ghanawater.R;

import java.text.MessageFormat;

public class SplashScreen extends AppCompatActivity {
    private TextView name, slogan;
    private ImageView logo;
    private View topView1, topView2, topView3;
    private View bottomView1, bottomView2, bottomView3;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setContentView(R.layout.activity_splash_screen);

        name = findViewById(R.id.name);
        slogan = findViewById(R.id.slogan);

        logo = findViewById(R.id.logo);

        topView1 = findViewById(R.id.topView1);
        topView2 = findViewById(R.id.topView2);
        topView3 = findViewById(R.id.topView3);

        bottomView1 = findViewById(R.id.bottomView1);
        bottomView2 = findViewById(R.id.bottomView2);
        bottomView3 = findViewById(R.id.bottomView3);

        Animation logoAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.zoom_animation);
        Animation nameAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.zoom_animation);

        Animation topView1Animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.top_views_animation);
        Animation topView2Animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.top_views_animation);
        Animation topView3Animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.top_views_animation);

        Animation bottomView1Animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.bottom_views_animation);
        Animation bottomView2Animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.bottom_views_animation);
        Animation bottomView3Animation = AnimationUtils.loadAnimation( SplashScreen.this, R.anim.bottom_views_animation);

        topView1.startAnimation(topView1Animation);
        bottomView1.startAnimation(bottomView1Animation);

        topView1Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topView2.setVisibility(View.VISIBLE);
                bottomView2.setVisibility(View.VISIBLE);

                topView2.startAnimation(topView2Animation);
                bottomView2.startAnimation(bottomView2Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topView2Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topView3.setVisibility(View.VISIBLE);
                bottomView3.setVisibility(View.VISIBLE);

                topView3.startAnimation(topView3Animation);
                bottomView3.startAnimation(bottomView3Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topView3Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(logoAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                name.setVisibility(View.VISIBLE);
                name.startAnimation(nameAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //move name
        nameAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                slogan.setVisibility(View.VISIBLE);
                final String animateTxt = slogan.getText().toString();
                slogan.setText("");
                count = 0;

                new CountDownTimer(animateTxt.length() * 100L, 100) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        slogan.setText(MessageFormat.format("{0}{1}", slogan.getText().toString(), animateTxt.charAt(count)));
//                        slogan.setText(slogan.getText().toString() + animateTxt.charAt(count));
                        count++;
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }.start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}