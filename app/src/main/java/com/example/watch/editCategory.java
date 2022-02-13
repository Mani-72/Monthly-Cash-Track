package com.example.watch;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class editCategory extends AppCompatActivity {

    private ArrayList<iconItem> mCountryList;
    private iconAdapter mAdapter;
    String iconID ="imageview1";

    Button addCategory, updateCategory,viewCategory , deleteCategory;
    EditText categoryNew, budget1, id1 , deleteId;
    DataHelper dataHelper;
    Spinner spinner1;
    ArrayList<String> list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        dataHelper= new DataHelper(this);


       dataHelper.insertCategories("Grocery", String.valueOf(100),"imageview1");
        dataHelper.insertCategories("Rent", String.valueOf(400),"imageview2");

        spinnerCategory();
        addCategory();
        updateCategorymain();
        viewCategorymain();
        deleteCategory();
        spinner1.setSelection(0);
        initList();
        iconSpinner();

    }

    private void deleteCategory() {
        deleteCategory = (Button) findViewById(R.id.deleteCatbtn);
        deleteId = (EditText) findViewById(R.id.deleteCatID);
        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =deleteId.getText().toString();
                if (text.length() != 0) {
                     Integer IDs = Integer.parseInt(text) ;
                     boolean check = dataHelper.deleteCategory(IDs);
                    if (check == true) {
                        spinnerCategory();
                        Toast.makeText(editCategory.this, "Data deleted!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(editCategory.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }
                else Toast.makeText(editCategory.this,"Enter a valid ID!",Toast.LENGTH_LONG).show();
            }

        });

    }

    public void spinnerCategory() {
        spinner1 = (Spinner) findViewById(R.id.categorySpinner);
        list = dataHelper.getAllCategories();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.categoryspinner_layout, R.id.category, list);
        spinner1.setAdapter(adapter);


    }

    public void addCategory(){
        addCategory = (Button) findViewById(R.id.addcategorybtn);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String icon = iconID;

                categoryNew = (EditText) findViewById(R.id.newCategory);
                budget1 = (EditText) findViewById(R.id.budget);
                id1 = (EditText) findViewById(R.id.editID);

                String categorynew = categoryNew.getText().toString();
                String budget = budget1.getText().toString();
                String id = id1.getText().toString();
                setCategory(id,categorynew, budget,icon);

                categoryNew.setText("");
                budget1.setText("");
                id1.setText("");

            }


        });
    }


    public void updateCategorymain(){
        updateCategory=(Button) findViewById(R.id.updateCategory);
        updateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String icon = iconID ;

                categoryNew = (EditText) findViewById(R.id.newCategory);
                budget1 = (EditText) findViewById(R.id.budget);
                id1 = (EditText) findViewById(R.id.editID);

                String categorynew = categoryNew.getText().toString();
                String budget = budget1.getText().toString();
                String id = id1.getText().toString();
                updateCategory(id,categorynew, budget,icon);

                categoryNew.setText("");
                budget1.setText("");
                id1.setText("");

            }


        });
    }

    public void viewCategorymain(){
        viewCategory = (Button) findViewById(R.id.viewCategory);
        viewCategory.setOnClickListener(new View.OnClickListener() {
                                            @Override
        public void onClick(View v) {
         Cursor res = dataHelper.getAllData();
          if (res.getCount() == 0) {
                // show message
                showMessage("Error", "Nothing found");
                return;
          }

           StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                      buffer.append("Id :" + res.getString(0) + "\n");
                      buffer.append("Category :" + res.getString(1) + "\n");
                      buffer.append("Budget :" + res.getString(2) + "\n\n\n");
                                                }

                        // Show all data
        showMessage("Data", buffer.toString());
         }
         }
        );
    }

    public void setCategory(String id,String categorynew, String budget, String icon) {
        if(categorynew.length()!=0 && budget.length()!=0) {
            boolean isInserted = dataHelper.insertCategories(categorynew, budget,icon);
            if (isInserted == true)
                Toast.makeText(editCategory.this, "Data Added", Toast.LENGTH_LONG).show();

            else
                Toast.makeText(editCategory.this, "Data not Added", Toast.LENGTH_LONG).show();
            spinnerCategory();
        }
        else {
            Toast.makeText(editCategory.this, "Enter the mandatory fields!", Toast.LENGTH_LONG).show();
        }
    }


    public void updateCategory(String id,String categorynew, String budget, String icon){

        if(categorynew.length()!=0 && id.length()!=0 && budget.length()!=0) {
            boolean isUpdate = dataHelper.update(id, categorynew, budget,icon);
            if (isUpdate == true)
                Toast.makeText(editCategory.this, "Data Updated", Toast.LENGTH_LONG).show();

            else
                Toast.makeText(editCategory.this, "Data not Updated", Toast.LENGTH_LONG).show();
            spinnerCategory();
        }
        else {
            Toast.makeText(editCategory.this, "Enter all mandatory fields!", Toast.LENGTH_LONG).show();
        }
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }



    public void iconSpinner(){
        Spinner spinnerCountries = findViewById(R.id.spinner_countries);

        mAdapter = new iconAdapter(this, mCountryList);
        spinnerCountries.setAdapter(mAdapter);

        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iconItem clickedItem = (iconItem) parent.getItemAtPosition(position);
                iconID = clickedItem.getName();
               // Toast.makeText(MainActivity.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initList() {
        mCountryList = new ArrayList<>();
        mCountryList.add(new iconItem("imageview1", R.drawable.imageview1));
        mCountryList.add(new iconItem( "imageview2",R.drawable.imageview2));
        mCountryList.add(new iconItem( "imageview3",R.drawable.imageview3));
        mCountryList.add(new iconItem( "imageview4",R.drawable.imageview4));
    }

}
