package com.example.watch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class splashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


       Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
           @Override
        public void run() {
                String selectedPin;
                SharedPreferences settings = getSharedPreferences("PREFS",0);
                selectedPin = settings.getString("password","");
                //  String check = selectedPin ;
              // selectedPin="ON";
                if(selectedPin.equals("ON"))

                {
                    Intent intent = new Intent(getApplicationContext(), enterPassword.class);
                    startActivity(intent);

                    // finish();
                }
                else

                {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    //  finish();
                }

           }
     },4000);




    }




}
