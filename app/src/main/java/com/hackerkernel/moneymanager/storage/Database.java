package com.hackerkernel.moneymanager.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to handle SQlite database
 */
public class Database extends SQLiteOpenHelper {
    private static String DB_NAME = "money_manager";
    private static int DB_VERSION = 2;

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
            COL_TRANSACTION_TIME = "time";

    //Query to create table
    private String CREATE_MONEY_TABLE = "CREATE TABLE " + TABLE_MONEY + "(" +
            COL_MONEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_MONEY_AMOUNT + " INTEGER, " +
            COL_MONEY_TYPE + " INTEGER, " +
            COL_MONEY_LAST_EDITED + " TEXT);";

    private String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "(" +
            COL_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_TRANSACTION_AMOUNT + " INTEGER," +
            COL_TRANSACTION_DESCRIPTION + " TEXT," +
            COL_TRANSACTION_TYPE + " INTEGER," +
            COL_TRANSACTION_TIME + " TEXT);";

    //update query
    private String DROP_MONEY_TABLE = "DROP TABLE IF EXISTS " + TABLE_MONEY,
            DROP_TRANSACTION_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRANSACTION;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
