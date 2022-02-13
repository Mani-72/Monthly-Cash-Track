package com.example.watch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "category.db";
    public static final String TABLE_PRO = "category_db";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_RPO = "CREATE TABLE IF NOT EXISTS "+ TABLE_PRO+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, pname TEXT NOT NULL UNIQUE, pbudget TEXT, picon TEXT)";

    public static final String DELETE_PRO="DROP TABLE IF EXISTS " + TABLE_PRO;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(CREATE_RPO);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop older table if existed
        db.execSQL(DELETE_PRO);
        //Create tables again
        onCreate(db);

    }


    public boolean insertCategories(String pname,String pbudget,String picon) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;


        try

        {
            values = new ContentValues();
            values.put("pname", pname);
            values.put("pbudget", pbudget);
            values.put("picon", picon);
            // Insert Row
            db.insert(TABLE_PRO, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
            return true;

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        finally

        {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return false;

    }


    public ArrayList<String> getAllCategories(){

        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();


            String selectQuery = "SELECT * FROM "+ TABLE_PRO;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String pname = cursor.getString(cursor.getColumnIndex("pname"));
                    list.add(pname);

                }

            }
        return list;


    }

    public String getIcon(String category){
        String icon = "";
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery =  "SELECT * FROM "+ TABLE_PRO+ " WHERE pname = '"+category+"'";
        Cursor cursor1 = db.rawQuery(selectQuery, null);
            while (cursor1.moveToNext()) 
            icon = cursor1.getString(cursor1.getColumnIndex("picon"));
      return icon;
    }


    public boolean update(String id ,String categoryNew,String budget,String icon){
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("id", id);
            contentvalues.put("pname", categoryNew);
            contentvalues.put("pbudget", budget);
            contentvalues.put("picon", icon);
            db.update(TABLE_PRO, contentvalues, "id = ?", new String[]{id});
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return false;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_PRO,null);
        return res;
    }

    public boolean deleteCategory(Integer IDs){
        SQLiteDatabase db = this.getWritableDatabase();

        boolean check=  db.delete(TABLE_PRO,"ID = " +IDs,null)>0;
        return check;
    }

    public Integer getBudgetAmount(String category){
        Integer budget =0;
        String amount="";
        SQLiteDatabase db = this.getWritableDatabase();
        category="= '"+category+"'";

        String query = new StringBuilder().append("SELECT * FROM ")
                .append(TABLE_PRO)
                .append(" WHERE pname ").append(category).toString();
        Cursor  res= db.rawQuery(query,null);
        while (res.moveToNext()) {
            amount = res.getString(res.getColumnIndex("pbudget"));
            budget = budget + Integer.valueOf(amount);
        }
        return budget;
    }

}
