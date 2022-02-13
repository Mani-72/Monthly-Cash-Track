package com.example.watch;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mitch on 2016-05-13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "watchData.db";
    public static final String TABLE_NAME = "watch_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "CATEGORY";
    public static final String COL3 = "DATE";
    public static final String COL4 = "AMOUNT";
    public static final String COL5 = "PAYMENT";
    public static final String COL6 = "RECURRING";
    public static final String COL7 = "COMMENTS";

        public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY TEXT, DATE DATE , AMOUNT TEXT, PAYMENT TEXT, RECURRING TEXT, COMMENTS TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String sCategory, String sDate, String sAmount, String sPayment, String sRecurrent, String sComments){
        SQLiteDatabase db = this.getWritableDatabase();
      //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL2, sCategory);
        contentValues.put(COL3, sDate);
        contentValues.put(COL4, sAmount);
        contentValues.put(COL5, sPayment);
        contentValues.put(COL6, sRecurrent);
        contentValues.put(COL7, sComments);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getFilteredData( String category ,String startDate,String endDate, String amount ,String mode, String recurring){

        SQLiteDatabase db = this.getWritableDatabase();

        String isNull = "IS NOT NULL",categoryN=isNull, startDateN="'1900-12-01'", endDateN="'2050-12-01'",modeN=isNull,amountN=isNull,recurringN=isNull;
        if(category.length()!=0) categoryN ="IN ("+category+")";
        if(startDate.length()!=0) startDateN =" '"+startDate+"'";
        if(endDate.length()!=0) endDateN =" '"+endDate+"'";
        if(amount.length()!=0) amountN ="= '"+amount+"'";
        if(mode.length()!=0) modeN ="= '"+mode+"'";
        if(recurring.length()!=0) recurringN ="= '"+recurring+"'";

        String query = new StringBuilder().append("SELECT * FROM ")
                .append(TABLE_NAME)
                .append(" WHERE CATEGORY ").append(categoryN)
                .append(" AND DATE BETWEEN ").append(startDateN)
                .append(" AND ").append(endDateN)
                .append(" AND AMOUNT ").append(amountN)
                .append(" AND PAYMENT ").append(modeN)
                .append(" AND RECURRING ").append(recurringN)
                .append(" ORDER BY DATE ASC")
                .toString();
        Cursor  res= db.rawQuery(query,null);

        // Cursor res = db.rawQuery(query,new String[] {categoryN});


        return res;
    }

    public boolean deleteTransfer(Integer IDs){
        SQLiteDatabase db = this.getWritableDatabase();

        boolean check=  db.delete(TABLE_NAME,"ID = " +IDs,null)>0;
    return check;
    }

    public Integer getSpentAmount(String category){
        Integer spent =0;
        String amount="";
        SQLiteDatabase db = this.getWritableDatabase();
        category="= '"+category+"'";

        String query = new StringBuilder().append("SELECT * FROM ")
                .append(TABLE_NAME)
                .append(" WHERE CATEGORY ").append(category).toString();
        Cursor  res= db.rawQuery(query,null);
        while (res.moveToNext()) {
            amount = res.getString(res.getColumnIndex("AMOUNT"));
            String[] parts = amount.split(" ");
            amount = parts[0];
            spent = spent + Integer.valueOf(amount);
        }
        return spent;
    }

}
