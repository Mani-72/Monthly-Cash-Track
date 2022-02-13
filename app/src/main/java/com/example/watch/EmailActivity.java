package com.example.watch;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.watch._MainActivity.GMailSender;


public class EmailActivity extends AppCompatActivity {

    EditText et_to, et_message, et_subject;
    Button btn_send;
    Context c;
    String GMail = "thewatchdata@gmail.com"; //replace with you GMail
    String GMailPass = "watch@123456"; // replace with you GMail Password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        c = this;

        et_to = (EditText) findViewById(R.id.et_to);
        et_message = (EditText) findViewById(R.id.et_message);
        // et_subject = "test";
        btn_send = (Button) findViewById(R.id.btn_send2);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_to = "46.anto@gmail.com";
                String str_message = "App auto-generated Email";
                String str_subject = "App Reports";

                // Check if there are empty fields
                if (!str_to.equals("") &&
                        !str_message.equals("") &&
                        !str_subject.equals("")){


                    //Check if 'To:' field is a valid email

                    et_to.setError(null);


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(c, "Sending... Please wait", Toast.LENGTH_LONG).show();
                        }
                    });
                    sendEmail(str_to, str_subject, str_message);



                }else{
                    Toast.makeText(c, "There are empty fields.", Toast.LENGTH_LONG).show();
                }

            }


        });


    }

    private void sendEmail(final String to, final String subject, final String message) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(GMail,
                            GMailPass);
                    sender.sendMail(subject,
                            message,
                            GMail,
                            to);
                    Log.w("sendEmail","Email successfully sent!");

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, "Email successfully sent!", Toast.LENGTH_LONG).show();
                            et_to.setText("");
                            et_message.setText("");
                            et_subject.setText("");
                        }
                    });

                } catch (final Exception e) {
                    Log.e("sendEmail", e.getMessage(), e);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, "Email not sent. \n\n Error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }

        }).start();
    }


    // Check if parameter 'emailAddress' is a valid email

}
