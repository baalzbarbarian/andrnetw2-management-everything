package com.practice.andr_networking_asm.ui.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.practice.andr_networking_asm.R;
import com.practice.andr_networking_asm.controller.IntentController;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread couldow = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }catch (Exception e){

                }finally {
                    IntentController.directinal(SplashScreenActivity.this, LoginActivity.class);
                    finish();
                }
            }
        };
        couldow.start();

    }
}