package com.example.watch;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ViewTransfers extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    EditText deleteIDs, amount;
    TextView startDate , endDate;
    Button view, delete , addDate , addEndDate , makeFile;
    Spinner mop_ViewTransfer,recurring_ViewTransfer,currency;
    DatabaseHelper myDb;
    DataHelper cDb;
    int findDatebtn;

    Button categorySelection;
    TextView categoryList;

    String categoriesChecked ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transfers);

        myDb = new DatabaseHelper(this);


        deleteIDs=(EditText) findViewById(R.id.deleteCatID);
        startDate =(TextView) findViewById(R.id.filterDateStart);
        endDate =(TextView) findViewById(R.id.filterDateEnd);
        amount=(EditText) findViewById(R.id.amount_filter);
        currency=(Spinner) findViewById(R.id.currency_filter);
        recurring_ViewTransfer=(Spinner) findViewById(R.id.recurring_viewTransfer);
        mop_ViewTransfer=(Spinner) findViewById(R.id.mop_viewTransfer);
        view = (Button) findViewById(R.id.viewTranfers);
        delete=(Button) findViewById(R.id.delTransfer);
        addDate = (Button) findViewById(R.id.addFilterDate);
        addEndDate = (Button) findViewById(R.id.addFilterEndDate);

        implementValue();
        dateimplement();
        ViewData();
        DeleteData();
        categoryListImplement();
        makeFile();





    }

    public void categoryListImplement() {

        categoryList = (TextView) findViewById(R.id.categorylistText);
        categorySelection = (Button) findViewById(R.id.categorybttn);
        cDb = new DataHelper(this);



        
        categorySelection.setOnClickListener(new View.OnClickListener() {

            @Override
             public void onClick(View v) {
                 categoriesChecked ="";
                categoryList.setText("");
                 final   List<String> ListofCategories = cDb.getAllCategories();
                CharSequence[] list = cDb.getAllCategories().toArray(new CharSequence[0]);
                 final boolean[] checkedItems = new boolean[list.length];
                  String categoryString ="";
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewTransfers.this);
                mBuilder.setTitle("Categories");
                mBuilder.setMultiChoiceItems(list, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                             checkedItems[which] = isChecked;
                             String currentItem=   ListofCategories.get(which);
                            // Toast.makeText(ViewTransfers.this,currentItem+" "+ isChecked,Toast.LENGTH_SHORT).show();
                            
                    }
                });
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String clickedCategory ="";
                          CharSequence[] list = cDb.getAllCategories().toArray(new CharSequence[0]);
                          for(int i=0;i<list.length;i++) {
                                boolean checked = checkedItems[i];
                                if (checked) {
                                    appendCategory(ListofCategories.get(i));
                                    if (clickedCategory.length() != 0) {
                                        clickedCategory = ListofCategories.get(i) + " , " + clickedCategory;
                                    }
                                    else clickedCategory = ListofCategories.get(i);

                                }
                          }
                           Toast.makeText(ViewTransfers.this,categoriesChecked,Toast.LENGTH_SHORT).show();
                        categoryList.setText(clickedCategory);
                    }
                }) ;

                mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                }) ;

                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    public void appendCategory(String checkedCategory){
        if(categoriesChecked.length() != 0) {
            categoriesChecked = "'" + checkedCategory + "' , " + categoriesChecked;
        }
        else categoriesChecked = "'" + checkedCategory + "'";
    }

    public void DeleteData() {
        delete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String text =deleteIDs.getText().toString();
                if (text.length() != 0) {
                    Integer IDs = Integer.parseInt(text) ;
                    boolean check = myDb.deleteTransfer(IDs);
                    if (check == true) {
                        Toast.makeText(ViewTransfers.this, "Data deleted!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ViewTransfers.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(ViewTransfers.this,"Enter a valid ID!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void ViewData() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String categoryS , startDateS,endDateS, recurrentS, modeS,amountS,currencyS;
                categoryS = categoriesChecked;
                startDateS= startDate.getText().toString();
                endDateS= endDate.getText().toString();
                amountS=amount.getText().toString();
                currencyS=currency.getSelectedItem().toString();
                modeS= mop_ViewTransfer.getSelectedItem().toString();
                recurrentS= recurring_ViewTransfer.getSelectedItem().toString();

                if (categoryS.length() !=0) {
                    if (amountS.length() != 0) amountS = amountS + " " + currencyS;

                    Cursor res = myDb.getFilteredData(categoryS, startDateS, endDateS, amountS, modeS, recurrentS);
                    if (res.getCount() == 0) {
                        showMessage("Error", "No records found!");
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("ID : " + res.getString(0) + "\n");
                        buffer.append("Category : " + res.getString(1) + "\n");
                        buffer.append("Date : " + res.getString(2) + "\n");
                        buffer.append("Amount : " + res.getString(3) + "\n");
                        buffer.append("Payment : " + res.getString(4) + "\n");
                        buffer.append("Recurring : " + res.getString(5) + "\n");
                        buffer.append("Comments : " + res.getString(6) + "\n \n");

                    }
                    showMessage("Money Tracker", buffer.toString());
                }

                else Toast.makeText(ViewTransfers.this,"Select atleast one category!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void implementValue(){
        Spinner recurring_ViewTransfer = findViewById(R.id.recurring_viewTransfer);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurring_ViewTransfer.setAdapter(adapter);
        recurring_ViewTransfer.setOnItemSelectedListener(this);

        Spinner mop_ViewTransfer = findViewById(R.id.mop_viewTransfer);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.mop, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mop_ViewTransfer.setAdapter(adapter2);
        mop_ViewTransfer.setOnItemSelectedListener(this);

        Spinner currency = findViewById(R.id.currency_filter);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.currency, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency.setAdapter(adapter3);
        currency.setOnItemSelectedListener(this);

    }

    public void dateimplement(){
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFrangment();
                datePicker.show(getSupportFragmentManager(),"date picker");
                findDatebtn = R.id.addFilterDate;
            }
        });

        addEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker2=new DatePickerFrangment();
                datePicker2.show(getSupportFragmentManager(),"date picker");
                findDatebtn = R.id.addFilterEndDate;
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String currentDateString= sdf.format(c.getTime());
        //  String currentDateString= DateFormat.getDateInstance().format(c.getTime());
        switch (findDatebtn){

            case R.id.addFilterDate: startDate.setText(currentDateString);break;
            case R.id.addFilterEndDate: endDate.setText(currentDateString);break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void makeFile(){
        makeFile = (Button) findViewById(R.id.save_as_file);
        makeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FILENAME = "transaction.csv";
                boolean check = false;

                String startdate = String.valueOf( Calendar.getInstance().get(Calendar.YEAR))+"-"+
                        String.valueOf( new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()))+"-01";

                String enddate = String.valueOf( Calendar.getInstance().get(Calendar.YEAR))+"-"+
                        String.valueOf( new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()))+"-31";

                Cursor res = myDb.getFilteredData("",startdate,enddate,"","","");
                if (res.getCount() == 0) {
                    showMessage("Error", "No records found!");
                    return;
                }
                else {
                    try {
                        File dir = getFilesDir();
                        File f = new File(dir,(FILENAME));
                        f.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String Entry = "ID,Category,Date,Amount,Mode of Payment,Recurring \n";
                    while (res.moveToNext()) {
                        Entry = Entry + res.getString(0) + "," +
                                res.getString(1) + "," +
                                res.getString(2) + "," +
                                res.getString(3) + "," +
                                res.getString(4) + "," +
                                res.getString(5) + "," +
                                res.getString(6) + " \n";
                    }

                    try {

                        FileOutputStream out = openFileOutput(FILENAME, Context.MODE_APPEND);
                        out.write(Entry.getBytes());
                        out.close();
                        check =true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (check) Toast.makeText(ViewTransfers.this,"Done!",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(ViewTransfers.this,"Error!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
