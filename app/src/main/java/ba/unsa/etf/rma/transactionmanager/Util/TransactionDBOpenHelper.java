package ba.unsa.etf.rma.transactionmanager.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TransactionDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TransactionManager.db";
    public static final int DATABASE_VERSION = 2;

    public TransactionDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TransactionDBOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static final String TRANSACTION_TABLE = "transactions";
    public static final String TRANSACTION_INTERNAL_ID = "_id";
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_TITLE = "title";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_TYPE = "type";
    public static final String TRANSACTION_ITEM_DESCRIPTION = "itemDescription";
    public static final String TRANSACTION_INTERVAL = "interval";
    public static final String TRANSACTION_END_DATE = "endDate";
    public static final String TRANSACTION_TYPE_ID = "typeID";
    public static final String TRANSACTION_ACTION = "actionParam";

    private static final String TRANSACTION_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " (" + TRANSACTION_INTERNAL_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSACTION_AMOUNT + " FLOAT, "
                    + TRANSACTION_TYPE_ID + " INTEGER, "
                    + TRANSACTION_ACTION +  " INTEGER, "
                    + TRANSACTION_TITLE + " TEXT NOT NULL, "
                    + TRANSACTION_ID + " INTEGER, "
                    + TRANSACTION_INTERVAL + " INTEGER, "
                    + TRANSACTION_DATE + " TEXT, "
                    + TRANSACTION_END_DATE + " TEXT, "
                    + TRANSACTION_TYPE + " TEXT, "
                    + TRANSACTION_ITEM_DESCRIPTION + " TEXT); ";





    private static final String TRANSACTION_TABLE_DROP = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;

    public static final String ACCOUNT_TABLE = "accounts";
    public static final String ACCOUNT_INTERNAL_ID = "_id";
    public static final String ACCOUNT_BUDGET = "budget";
    public static final String ACCOUNT_MONTH_LIMIT = "monthLimit";
    public static final String ACCOUNT_TOTAL_LIMIT = "totalLimit";

    private static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " (" + ACCOUNT_INTERNAL_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ACCOUNT_BUDGET + " FLOAT, "
            + ACCOUNT_MONTH_LIMIT + " FLOAT, "
            + ACCOUNT_TOTAL_LIMIT + " FLOAT);";

    private static final String ACCOUNT_TABLE_DROP = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_CREATE);
        sqLiteDatabase.execSQL(TRANSACTION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_DROP);
        sqLiteDatabase.execSQL(TRANSACTION_TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
