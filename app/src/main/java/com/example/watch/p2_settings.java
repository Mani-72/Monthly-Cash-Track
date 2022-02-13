package com.example.watch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class p2_settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnApply,goToCateg, changePINbtn ;
    Spinner sCurrency, sPinSelection;
    String currency, selectedPin;
    EditText newPin, oldPin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2_settings);

//Implementing currency & PIN spinners
        valueImplement();

        btnApply = (Button) findViewById(R.id.p2_apply);
        changePINbtn = (Button) findViewById(R.id.changePin);
        goToCateg=(Button) findViewById(R.id.goToCategory);
        newPin =(EditText) findViewById(R.id.settings_newPin) ;
        oldPin =(EditText) findViewById(R.id.settings_oldPin) ;

        goToCategory();

        // Currency setting fixed from Main activity
        sCurrency = (Spinner) findViewById(R.id.p2_currency);
        for(int i= 0; i < sCurrency.getAdapter().getCount(); i++)
        {
            if(sCurrency.getAdapter().getItem(i).toString().contains(getIntent().getStringExtra("message")))
            {
                sCurrency.setSelection(i);
            }
        }

        sPinSelection= (Spinner) findViewById(R.id.p2_pin);
        Applybutton();
        changePINbutton();

        // new

    }

    public void changePINbutton() {
        changePINbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPIN = newPin.getText().toString();
                String oldPIN = oldPin.getText().toString();
                if(newPIN.length()!= 0
                        && oldPIN.length()!= 0
                        && oldPIN.length()== 4
                        && newPIN.length()== 4
                ){
                    String currentPin;
                    SharedPreferences settings = getSharedPreferences("PREFS",0);
                    currentPin = settings.getString("password","1234");
                   if(currentPin.equals(oldPIN)){
                       SharedPreferences.Editor editor= settings.edit();
                       editor.putString("password",newPIN);
                       editor.apply();
                       Toast.makeText(p2_settings.this, "Password Changed!", Toast.LENGTH_LONG).show();
                       newPin.setText("");
                       oldPin.setText("");
                   }
                   else
                       Toast.makeText(p2_settings.this, "Incorrect password!", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(p2_settings.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });


    }

    /* public void goToHome(){
         Intent intent = new Intent(this, MainActivity.class);
         startActivity(intent);
     }
 */
    public void valueImplement() {
        Spinner sCurrency = findViewById(R.id.p2_currency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCurrency.setAdapter(adapter);
        sCurrency.setOnItemSelectedListener(this);

        Spinner sPin = findViewById(R.id.p2_pin);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.pin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPin.setAdapter(adapter2);
        String selectedPin;

        SharedPreferences settings = getSharedPreferences("PREFS",0);
        selectedPin = settings.getString("selectedPin","");
        if(selectedPin=="ON") sPin.setSelection(0);
        else sPin.setSelection(1);
        sPin.setOnItemSelectedListener(this);
    }

    public void goToCategory(){
        goToCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2Category();
            }
        });
    }

    public void go2Category(){
        Intent intent2 = new Intent(p2_settings.this, editCategory.class);
        startActivity(intent2);
    }

    public void Applybutton(){
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPin=sPinSelection.getSelectedItem().toString();
                SharedPreferences settings = getSharedPreferences("PREFS",0);
                SharedPreferences.Editor editor= settings.edit();
                editor.putString("selectedPin",selectedPin);
                //  selectedPinT.setText("OFF");
                currency = sCurrency.getSelectedItem().toString();
                editor.putString("selectedCurrency",currency);
                editor.apply();
                Intent intent=new Intent(p2_settings.this, MainActivity.class);
                startActivity(intent);


            }
        });

    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
