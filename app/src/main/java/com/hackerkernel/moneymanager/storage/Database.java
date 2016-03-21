package com.hackerkernel.moneymanager.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.hackerkernel.moneymanager.R;
import com.hackerkernel.moneymanager.pojo.TransactionPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle SQlite database
 */
public class Database extends SQLiteOpenHelper {
    private static String DB_NAME = "money_manager";
    private static int DB_VERSION = 6;
    private Context mContext;

    //money type
    public static int MONEY_WALLET = 1,
            MONEY_BANK = 2;

    public static int MONEY_ADDED = 1,
            MONEY_SUBTRACTED = 2;

    //table schema
    private static String TABLE_MONEY = "money",
            COL_MONEY_ID = "_id",
            COL_MONEY_AMOUNT = "amount",
            COL_MONEY_TYPE = "money_type",
            COL_MONEY_LAST_EDITED = "last_edited";

    private static String TABLE_TRANSACTION = "transaction_table",
            COL_TRANSACTION_ID = "_id",
            COL_TRANSACTION_AMOUNT = "amount",
            COL_TRANSACTION_DESCRIPTION = "description",
            COL_TRANSACTION_TYPE = "type",
            COL_TRANSACTION_MONEY_TYPE = "money_type",
            COL_TRANSACTION_TIME = "time";

    //Query to create table
    private String CREATE_MONEY_TABLE = "CREATE TABLE " + TABLE_MONEY + "(" +
            COL_MONEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_MONEY_AMOUNT + " TEXT, " +
            COL_MONEY_TYPE + " INTEGER, " +
            COL_MONEY_LAST_EDITED + " TEXT);";

    private String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "(" +
            COL_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_TRANSACTION_AMOUNT + " TEXT," +
            COL_TRANSACTION_DESCRIPTION + " TEXT," +
            COL_TRANSACTION_TYPE + " INTEGER," +
            COL_TRANSACTION_MONEY_TYPE+" INTEGER," +
            COL_TRANSACTION_TIME + " TEXT);";

    //update query
    private String DROP_MONEY_TABLE = "DROP TABLE IF EXISTS " + TABLE_MONEY,
            DROP_TRANSACTION_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRANSACTION;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    /*
    * Method to add money
    * */
    public void addCash(String amount, int moneyType) {
        SQLiteDatabase db = this.getWritableDatabase();

        //get Current time stamp
        Long tsLong = System.currentTimeMillis() / 1000;
        String timestamp = tsLong.toString();

        //Check money field is present in database or not
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MONEY + " WHERE " + COL_MONEY_TYPE + "=?", new String[]{moneyType + ""});
        int count = cursor.getCount();
        //if count = 0 add money row, else increment money
        if (count == 0) {
            //add money
            ContentValues cv = new ContentValues();
            cv.put(COL_MONEY_AMOUNT, amount);
            cv.put(COL_MONEY_TYPE, moneyType);
            cv.put(COL_MONEY_LAST_EDITED, timestamp);
            db.insert(TABLE_MONEY, null, cv);
        } else {
            //Update money
            cursor.moveToFirst();

            //get the old amount from database
            String oldAmount = cursor.getString(cursor.getColumnIndex(COL_MONEY_AMOUNT));

            //Create new amount
            int newAmount = Integer.parseInt(oldAmount) + Integer.parseInt(amount);
            //update amount
            ContentValues cv = new ContentValues();
            cv.put(COL_MONEY_AMOUNT, newAmount);
            cv.put(COL_MONEY_LAST_EDITED, timestamp);
            db.update(TABLE_MONEY, cv, COL_MONEY_TYPE + "=?", new String[]{moneyType + ""});
        }
        cursor.close();
    }

    /*
    * Method to add transaction details
    * */
    public void addTranscation(String amount,String description,int transcationType,int moneyType) {
        SQLiteDatabase db = this.getWritableDatabase();

        //get current timestamp
        Long tmLong = System.currentTimeMillis() / 1000;
        String timestamp = tmLong.toString();

        ContentValues cv = new ContentValues();
        cv.put(COL_TRANSACTION_AMOUNT,amount);
        cv.put(COL_TRANSACTION_DESCRIPTION,description);
        cv.put(COL_TRANSACTION_MONEY_TYPE,moneyType);
        cv.put(COL_TRANSACTION_TYPE,transcationType);
        cv.put(COL_TRANSACTION_TIME,timestamp);

        db.insert(TABLE_TRANSACTION,null,cv);
        db.close();
    }

    /*
    * Subtract money
    * */
    public boolean subtractMoney(String amountString, int moneyType) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean returnVal = false;

        //get Current time stamp
        Long tsLong = System.currentTimeMillis() / 1000;
        String timestamp = tsLong.toString();

        //Check money field is present in database or not
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MONEY + " WHERE " + COL_MONEY_TYPE + "=?", new String[]{moneyType + ""});
        int count = cursor.getCount();
        //if count = 0 add money row, else increment money
        if (count != 0) {
            //Update money
            cursor.moveToFirst();

            //get the old amount from database
            String oldAmountString = cursor.getString(cursor.getColumnIndex(COL_MONEY_AMOUNT));
            int oldAmount = Integer.parseInt(oldAmountString);
            int amount = Integer.parseInt(amountString);

            //check amount is less then old amount
            if (amount > oldAmount){
                Toast.makeText(mContext,"Cannot subtract "+amount+" RS because you only have "+oldAmount+" Rs",Toast.LENGTH_LONG).show();
                return false;
            }

            //Create new amount
            int newAmount = oldAmount - amount;

            //update amount
            ContentValues cv = new ContentValues();
            cv.put(COL_MONEY_AMOUNT, newAmount);
            cv.put(COL_MONEY_LAST_EDITED, timestamp);
            db.update(TABLE_MONEY, cv, COL_MONEY_TYPE + "=?", new String[]{moneyType + ""});
            //set return to success
            returnVal = true;
        }else{
            Toast.makeText(mContext, R.string.no_money_found,Toast.LENGTH_LONG).show();
            //set return to false
            returnVal = false;
        }
        cursor.close();
        return returnVal;
    }

    /*
    * Return wallet amount
    * */
    public int getMoney(int moneyType){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] col = new String[]{COL_MONEY_AMOUNT};
        String[] selArgs = new String[]{moneyType+""};
        Cursor cursor = db.query(TABLE_MONEY, col, COL_MONEY_TYPE + "=?", selArgs, null, null, null);
        if (cursor.getCount() > 0){
            //fetch amount
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(COL_MONEY_AMOUNT));
        }
        return 0;
    }

    /*
    * Get all the list of transaction
    * */
    public List<TransactionPojo> getAllTransaction(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRANSACTION, null, null, null, null, null, COL_TRANSACTION_ID + " DESC");
        List<TransactionPojo> list = new ArrayList<>();
        while (cursor.moveToNext()){
            TransactionPojo pojo = new TransactionPojo();
            String id = cursor.getString(cursor.getColumnIndex(COL_TRANSACTION_ID));
            String amount = cursor.getString(cursor.getColumnIndex(COL_TRANSACTION_AMOUNT));
            String description = cursor.getString(cursor.getColumnIndex(COL_TRANSACTION_DESCRIPTION));
            String type = cursor.getString(cursor.getColumnIndex(COL_TRANSACTION_TYPE));
            String moneyType = cursor.getString(cursor.getColumnIndex(COL_TRANSACTION_MONEY_TYPE));
            String timestamp = cursor.getString(cursor.getColumnIndex(COL_TRANSACTION_TIME));

            pojo.setId(id);
            pojo.setAmount(amount);
            pojo.setDescription(description);
            pojo.setType(type);
            pojo.setMoneyType(moneyType);
            pojo.setTimestamp(timestamp);

            list.add(pojo);
        }
        cursor.close();
        return list;
    }

    /*
    * Method to get transaction count
    * */
    public int getTransactionCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRANSACTION, null, null, null, null, null, null);
        return cursor.getCount();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MONEY_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_MONEY_TABLE);
        db.execSQL(DROP_TRANSACTION_TABLE);
        onCreate(db);
    }

}
