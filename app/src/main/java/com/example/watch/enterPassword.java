package com.example.watch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class enterPassword extends AppCompatActivity {

        EditText enterPwd;
        Button enter;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_enter_password);

            final String password;
            SharedPreferences settings = getSharedPreferences("PREFS",0);
            password = settings.getString("password","1234");
                enterPwd = (EditText) findViewById(R.id.enterPassword);
                enter = (Button) findViewById(R.id.Enter);

                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Text = enterPwd.getText().toString();
                        enterPwd.setText("");

                        if (Text.equals(password)) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(enterPassword.this, "Incorrect password!", Toast.LENGTH_LONG).show();
                        }
                        enterPwd.setText("");
                    }
                });
            }




    }

