package ba.unsa.etf.rma.transactionmanager.Util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TransactionContentProvider extends ContentProvider {
    private static final int ALLROWS =1;
    private static final int ONEROW = 2;
    private static final UriMatcher uM;

    static {
        uM = new UriMatcher(UriMatcher.NO_MATCH);
        uM.addURI("rma.provider.transactions","elements",ALLROWS);
        uM.addURI("rma.provider.transactions","elements/#",ONEROW);
    }

    TransactionDBOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new TransactionDBOpenHelper(getContext(),
                TransactionDBOpenHelper.DATABASE_NAME,null,
                TransactionDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArg, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try{
            database=mHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=mHelper.getReadableDatabase();
        }
        String groupby=null;
        String having=null;
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();

        switch (uM.match(uri)){
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                squery.appendWhere(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID+"="+idRow);
            default:break;
        }
        squery.setTables(TransactionDBOpenHelper.TRANSACTION_TABLE);
        Cursor cursor = squery.query(database,projection,selection,selectionArg,groupby,having,sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uM.match(uri)){
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.rma.elemental";
            case ONEROW:
                return "vnd.android.cursor.item/vnd.rma.elemental";
            default:
                throw new IllegalArgumentException("Unsuported uri: "+uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database;
        try{
            database=mHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=mHelper.getReadableDatabase();
        }


        String TRANSACTION_TABLE = "transactions";
        String TRANSACTION_INTERNAL_ID = "_id";
        String TRANSACTION_ID = "id";
        String TRANSACTION_TITLE = "title";
        String TRANSACTION_DATE = "date";
        String TRANSACTION_AMOUNT = "amount";
        String TRANSACTION_TYPE = "type";
        String TRANSACTION_ITEM_DESCRIPTION = "itemDescription";
        String TRANSACTION_INTERVAL = "interval";
        String TRANSACTION_END_DATE = "endDate";
        String TRANSACTION_TYPE_ID = "typeID";
        String TRANSACTION_ACTION = "actionParam";


        String TRANSACTION_TABLE_CREATE =
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
        database.execSQL(TRANSACTION_TABLE_CREATE);

        long id = database.insert(TRANSACTION_TABLE, null, contentValues);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int uriType = uM.match(uri);
        SQLiteDatabase sqlDB = mHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case ALLROWS:
                rowsDeleted = sqlDB.delete(mHelper.TRANSACTION_TABLE,
                        s,
                        strings);
                break;

            case ONEROW:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    rowsDeleted = sqlDB.delete(mHelper.TRANSACTION_TABLE,
                            mHelper.TRANSACTION_INTERNAL_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(mHelper.TRANSACTION_TABLE,
                            mHelper.TRANSACTION_INTERNAL_ID + "=" + id
                                    + " and " + s,
                            strings);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int uriType = uM.match(uri);
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType){
            case ALLROWS:
                rowsUpdated = sqLiteDatabase.update(mHelper.TRANSACTION_TABLE, contentValues, s, strings);
                break;
            case ONEROW:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(s)){
                    rowsUpdated = sqLiteDatabase.update(mHelper.TRANSACTION_TABLE, contentValues,
                            mHelper.TRANSACTION_INTERNAL_ID + "=" + id, null);
                }
                else{
                    rowsUpdated = sqLiteDatabase.update(mHelper.TRANSACTION_TABLE, contentValues,
                            mHelper.TRANSACTION_INTERNAL_ID + "=" + id + " and " + s, strings);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
