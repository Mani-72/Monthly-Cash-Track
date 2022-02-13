package com.example.watch;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    EditText amount,comments;
    TextView type,date;
    Button btnadd ,btnview, btnsettings, addDate;
    Spinner recurrent, payment,category;
    DatabaseHelper myDb;
    DataHelper cDb;
    SimpleDateFormat addedDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);
        cDb = new DataHelper(this);
        cDb.insertCategories("Grocery", String.valueOf(100),"imageview1");
        cDb.insertCategories("Rent", String.valueOf(400),"imageview2");

        category = (Spinner) findViewById(R.id.p1_category);
        date = (TextView) findViewById(R.id.p1_date);
        amount = (EditText) findViewById(R.id.p1_amount);
        type= (TextView) findViewById(R.id.type);
        payment = (Spinner) findViewById(R.id.mop);
        recurrent = (Spinner) findViewById(R.id.transaction);
        comments = (EditText) findViewById(R.id.p1_comments);
        btnsettings=(Button) findViewById(R.id.p1_settings);
        btnadd = (Button) findViewById(R.id.p1_add);
        btnview = (Button) findViewById(R.id.p1_view);
        addDate = (Button) findViewById(R.id.addFilterDate);
        category.setSelection(0);
        AddData(this);
        implementValue();
        viewAll();


//Currency type change
        String currentCurrency;
        SharedPreferences settings = getSharedPreferences("PREFS",0);
        currentCurrency = settings.getString("selectedCurrency","EUR");
        TextView tCurrency = (TextView)findViewById(R.id.type);
      //  if(currentCurrency!=null)
        tCurrency.setText(currentCurrency);
      //  else  tCurrency.setText("EUR");

//Page change to Settings
        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });

        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFrangment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });




    }

    //EMAIL Part

    public void email(View v){
        Intent myIntent = new Intent(MainActivity.this, EmailActivity.class);

        MainActivity.this.startActivity(myIntent);
    };



    public void graph(View v){
        Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);

        MainActivity.this.startActivity(myIntent);
    };

    public void AddData(Context context){

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sCategory = category.getSelectedItem().toString();
                String sDate = date.getText().toString();
                String sPayment = payment.getSelectedItem().toString();
                String sAmount = amount.getText().toString();

                if (sCategory.length() != 0
                        && sDate.length() != 0
                        && sPayment.length() != 0
                        && sAmount.length() != 0) {

                    Integer budget = cDb.getBudgetAmount(sCategory);
                    Integer amountSpent = myDb.getSpentAmount(sCategory);
                    if((budget-(amountSpent+Integer.valueOf(sAmount))) <=0) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                        mBuilder.setTitle("Overspent!!");
                        mBuilder.setMessage("You will cross the budget for "+sCategory+"!");
                        mBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addtoDB();
                            }
                        });
                        mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goToMain();
                            }
                        });
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();
                    }
                    else addtoDB();

                } else {
                    Toast.makeText(MainActivity.this, "Enter the mandatory fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void viewAll(){
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToView();
            }
        });
    }


    public void goToSettings(){
        String ctype=type.getText().toString();
        Intent intent2 = new Intent(MainActivity.this, p2_settings.class);
        intent2.putExtra("message",ctype);
        startActivity(intent2);
    }

    public void goToView(){
        Intent intent = new Intent(this, ViewTransfers.class);
        startActivity(intent);
    }
    public void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void implementValue(){
        Spinner transaction = findViewById(R.id.transaction);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transaction.setAdapter(adapter);
        transaction.setOnItemSelectedListener(this);

        Spinner mops = findViewById(R.id.mop);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.mop, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mops.setAdapter(adapter2);
        mops.setOnItemSelectedListener(this);
        categoryspin();

       category.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                categoryspin();
                return false;
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.p1_category : {
                String category1 = category.getSelectedItem().toString();
                String img = cDb.getIcon(category1);

              ImageView  imgIcon = (ImageView) findViewById(R.id.imageView);

                int resID = getResources().getIdentifier(img, "drawable", getPackageName());
                imgIcon.setImageResource(resID);
            }
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        addedDate = new SimpleDateFormat("YYYY-MM-dd");
        String currentDateString= addedDate.format(c.getTime());
        date.setText(currentDateString); }


    public  void categoryspin(){
        cDb = new DataHelper(this);
        ArrayList<String> list = new ArrayList<String>() ;
        list.add("");
        list.addAll(cDb.getAllCategories());

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.categoryspinner_layout, R.id.category, list);
        category.setAdapter(adapter3);
        category.setOnItemSelectedListener(this);
    }

    public void addtoDB(){
        String sCategory = category.getSelectedItem().toString();
        String sDate = date.getText().toString();
        String sPayment = payment.getSelectedItem().toString();
        String sAmount = amount.getText().toString();
        String stype = type.getText().toString();
        String sRecurrent = recurrent.getSelectedItem().toString();
        String sComments = comments.getText().toString();

        boolean isInserted = myDb.insertData(sCategory, sDate, sAmount+" "+stype, sPayment, sRecurrent, sComments);
        date.setText("");
        amount.setText("");
        comments.setText("");

        if(isInserted==true){
            Toast.makeText(MainActivity.this,"Data successfully added!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
        }
    }
}



