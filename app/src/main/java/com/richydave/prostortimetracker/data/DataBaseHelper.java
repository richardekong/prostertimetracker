package com.richydave.prostortimetracker.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.richydave.prostortimetracker.model.Service;
import com.richydave.prostortimetracker.model.Wage;

import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {
    // declare database name and version as constants
    private static final String DATABASE_NAME = "WAGES";
    private static final String WAGE_DETAILS_TABLE = "WAGE_DETAILS_TABLE";
    private static final String COLUMN_WAGE_ID = "WAGE_ID";
    private static final String COLUMN_WAGE = "WAGE";
    private static final String COLUMN_HOURS = "HOURS";
    private static final String COLUMN_TIME = "SAVED_TIME";
    //CONSTANTS FOR SERVICE_DETAILS_TABLE
    private static final String SERVICE_DETAILS_TABLE = "SERVICES";
    private static final String COLUMN_PRICE = "PRICE";
    private static final String COLUMN_CURRENCY_CODE = "CURRENCY_CODE";
    private static final int DATABASE_VERSION = 31;
    //CONSTANT FOR COLOR_TABLE
    private static final String COLOR_TABLE = "COLOR";
    private static final String COLUMN_COLOR = "COLOR_CODE";

    //constructor
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //implement all abstract methods of the super class
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //SQL for creating WAGE_DETAILS table
        final String CREATE_WAGE_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " + WAGE_DETAILS_TABLE + "(" +
                COLUMN_WAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_WAGE + " DECIMAL," +
                COLUMN_HOURS + " DECIMAL," +
                COLUMN_TIME + " VARCHAR);";

        //SQL for creating the SERVICE_DETAILS table
        final String CREATE_SERVICE_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " + SERVICE_DETAILS_TABLE + "(" +
                COLUMN_PRICE + " DECIMAL," +
                COLUMN_CURRENCY_CODE + " VARCHAR);";

        //SQL for creating COLOR table
        final String CREATE_COLOR_TABLE = "CREATE TABLE IF NOT EXISTS " + COLOR_TABLE + "(" + COLUMN_COLOR + " VARCHAR)";

        //create the tables
        sqLiteDatabase.execSQL(CREATE_WAGE_DETAILS_TABLE);
        sqLiteDatabase.execSQL(CREATE_SERVICE_DETAILS_TABLE);
        sqLiteDatabase.execSQL(CREATE_COLOR_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int version, int newVersion) {
        //Drop tables if they exist
        final String DROP_TABLE1 = "DROP TABLE IF EXISTS " + WAGE_DETAILS_TABLE;
        final String DROP_TABLE2 = "DROP TABLE IF EXISTS " + SERVICE_DETAILS_TABLE;
        final String DROP_TABLE3 = "DROP TABLE IF EXISTS " + COLOR_TABLE;
        sqLiteDatabase.execSQL(DROP_TABLE1);
        sqLiteDatabase.execSQL(DROP_TABLE2);
        sqLiteDatabase.execSQL(DROP_TABLE3);

        //create the table again
        onCreate(sqLiteDatabase);
    }

    //insert wage details into the data base
    public void addWageDetails(Wage wageDetail) {
        //create an sqlite database object
        SQLiteDatabase database = this.getWritableDatabase();
        //create a content values object to insert wage details to the wage detail table
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_WAGE, wageDetail.getWage());
        contentValues.put(COLUMN_HOURS, wageDetail.getHours());
        contentValues.put(COLUMN_TIME, wageDetail.getSaveTime());
        //insert this content values to the database
        database.insert(WAGE_DETAILS_TABLE, null, contentValues);
        //close the database
        database.close();
    }

    public void addServiceDetails(Service serviceDetail) {
        //create an sqlite database object
        SQLiteDatabase database = this.getWritableDatabase();
        //create a content values object to insert service details to the service detail table
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRICE, serviceDetail.getPrice());
        contentValues.put(COLUMN_CURRENCY_CODE, serviceDetail.getCurrencyCode());
        //insert this content values to the database
        database.insert(SERVICE_DETAILS_TABLE, null, contentValues);
        //close the database
        database.close();
    }

    public void addColor(String hex) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COLOR, hex);
        database.insert(COLOR_TABLE, null, contentValues);
        database.close();
    }

    public String getColor() {
        String hex = "";
        final String SELECT_COLOR = "SELECT * FROM " + COLOR_TABLE;
        try (SQLiteDatabase database = this.getReadableDatabase()) {
            try (Cursor cursor = database.rawQuery(SELECT_COLOR, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        hex = cursor.getString(0);
                    } while (cursor.moveToNext());
                }
            }
        }
        return hex;
    }

    public void deleteWageDetails(Wage wageDetail) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(WAGE_DETAILS_TABLE, COLUMN_WAGE_ID + "=?",
                new String[]{String.valueOf(wageDetail.getWageId())});
        sqLiteDatabase.close();

    }

    //obtain all wage details from the data base
    public List<Wage> getListOfWageDetails() {
        //create a list of wageDetails
        List<Wage> wageDetails = new ArrayList<>();
        //create an SQL query to select all records of wage details from the database
        final String SELECT_ALL_WAGES = "SELECT * FROM " + WAGE_DETAILS_TABLE;
        //create a database object
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //create a database cursor
        try (Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL_WAGES, null)) {
            //loop through all wage records in the cursor
            if (cursor.moveToFirst()) {
                do {
                    Wage wage = new Wage();
                    wage.setWageId(cursor.getInt(0));
                    wage.setWage(cursor.getDouble(1));
                    wage.setHours(cursor.getDouble(2));
                    wage.setSaveTime(cursor.getString(3));
                    //add the extracted wage details to the list
                    wageDetails.add(wage);
                } while (cursor.moveToNext());
            }
        }
        return wageDetails;
    }

    public Cursor getServiceDetail() {
        String SELECT_SERVICE_DETAILS = "SELECT * FROM " + SERVICE_DETAILS_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_SERVICE_DETAILS, null);
    }

    public Service getService() {
        Service service = new Service();
        //create a database object
        SQLiteDatabase database = this.getReadableDatabase();
        //initialize a cursor object
        Cursor cursor = null;
        try {
            final String SELECT_SERVICE_DETAILS = "SELECT * FROM " + SERVICE_DETAILS_TABLE;
            cursor = database.rawQuery(SELECT_SERVICE_DETAILS, null);
            //fetch data from cursor field
            if (cursor.moveToFirst()) {
                do {
                    service.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                    service.setCurrencyCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CURRENCY_CODE)));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return service;
    }

    public Cursor getAllWageDetails() {
        final String SELECT_ALL_WAGE_DETAILS = "SELECT * FROM " + WAGE_DETAILS_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery(SELECT_ALL_WAGE_DETAILS, null);
    }

    public double getTotalWage() {
        final String SELECT_TOTAL_WAGE = "SELECT TOTAL (" + COLUMN_WAGE + ") as " + COLUMN_WAGE + " FROM " + WAGE_DETAILS_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        double totalWage;
        try (Cursor cursor = sqLiteDatabase.rawQuery(SELECT_TOTAL_WAGE, null)) {
            totalWage = 0.0;
            if (cursor.moveToFirst()) {
                totalWage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WAGE));
            }
        }
        return totalWage;
    }

    public void updateCountryCode(Service service, String newCountryCode) {
        //create a writable database
        SQLiteDatabase database = this.getWritableDatabase();
        //create a map to stage the required data
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRICE, service.getPrice());
        values.put(COLUMN_CURRENCY_CODE, newCountryCode);
        //update without specifying a where clause since the table contains one record
        database.update(SERVICE_DETAILS_TABLE, values, null, null);
    }

    public void updateServicePrice(Service service, double price) {
        //create a writable database
        SQLiteDatabase database = this.getWritableDatabase();
        //create a map to stage the required data
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_CURRENCY_CODE, service.getCurrencyCode());
        //update without specifying a where clause since the table contains one record
        database.update(SERVICE_DETAILS_TABLE, values, null, null);

    }

    public void updateColor(String hex) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COLOR, hex);
        database.update(COLOR_TABLE, contentValues, null, null);

    }

    public void resetWageDetails() {
        SQLiteDatabase database = this.getWritableDatabase();
        //Delete statement
        final String DROP_WAGE_DETAILS_TABLE = "DELETE FROM " + WAGE_DETAILS_TABLE;
        //execute statement
        database.execSQL(DROP_WAGE_DETAILS_TABLE);
    }

    //check if COLOR table is empty
    public boolean isColorRecordEmpty() {
        final String SELECT_COLOR = "SELECT * FROM " + COLOR_TABLE;
        SQLiteDatabase database = this.getWritableDatabase();
        try (Cursor cursor = database.rawQuery(SELECT_COLOR, null)) {
            return (cursor.getCount() < 1);
        }

    }

}
