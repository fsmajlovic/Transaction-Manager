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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
