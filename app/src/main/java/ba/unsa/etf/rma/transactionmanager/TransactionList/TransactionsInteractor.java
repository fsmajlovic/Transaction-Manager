package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.Util.TransactionDBOpenHelper;


public class TransactionsInteractor extends AsyncTask<String, Integer, Void> implements ITransactionInteractor {

    private ArrayList<Transaction> transactionsAll;
    private ArrayList<Transaction> transactions;
    private OnGetTransactionTypesDone callerTypes;
    private Account account;
    private Context context;

    public TransactionsInteractor(){

    }

    public TransactionsInteractor(Context context, OnGetTransactionTypesDone p) {
        this.context = context;
        callerTypes = p;
        transactionsAll = new ArrayList<Transaction>();
        transactions = new ArrayList<Transaction>();
        account = new Account();
    };


    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    @Override
    protected Void doInBackground(String... strings) {

        addFromDatabaseToWeb();

        String api_id = this.context.getResources().getString(R.string.api_id);
        //Getting account info
        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/" + api_id;
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            JSONObject jsonObject = new JSONObject(result);
            int id = jsonObject.getInt("id");
            double budget = jsonObject.getInt("budget");
            double totalLimit = jsonObject.getDouble("totalLimit");
            double monthLimit = jsonObject.getDouble("monthLimit");
            account = new Account(id, budget, totalLimit, monthLimit);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //Getting all transactions
        for(int page = 0; page < 10; page++) {
            String url2 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+api_id+"/transactions/?page=" + page;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                URL url = new URL(url2);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = convertStreamToString(in);
                JSONObject jo = new JSONObject(result);
                JSONArray results = jo.getJSONArray("transactions");
                if(results.length() <= 0 )
                    break;
                for (int i = 0; i < results.length(); i++) {
                    JSONObject transactionJSON = results.getJSONObject(i);
                    int id = transactionJSON.getInt("id");
                    String date = transactionJSON.getString("date");
                    String title = transactionJSON.getString("title");
                    Double amount = transactionJSON.getDouble("amount");
                    String itemDescription = transactionJSON.getString("itemDescription");
                    String transactionInterval = transactionJSON.getString("transactionInterval");
                    String endDate = transactionJSON.getString("endDate");
                    int transactionTypeID = transactionJSON.getInt("TransactionTypeId");

                    if (transactionInterval.equals("null")) {
                        transactionInterval = "0";
                    }
                    try {
                        if (endDate.equals("null")) {
                            transactionsAll.add(new Transaction(id, sdf.parse(date), amount, title, itemDescription,
                                    Integer.valueOf(transactionInterval), null, transactionTypeID));
                        } else {
                            transactionsAll.add(new Transaction(id, sdf.parse(date), amount, title, itemDescription,
                                    Integer.valueOf(transactionInterval), sdf.parse(endDate), transactionTypeID));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //Getting filtered transactions
        for(int page = 0; page < 10; page++) {
            String url3 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+api_id+"/transactions/filter?page=" + page + "&" + strings[0];
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                URL url = new URL(url3);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = convertStreamToString(in);
                JSONObject jo = new JSONObject(result);
                JSONArray results = jo.getJSONArray("transactions");
                if(results.length() <= 0 )
                    break;
                for (int i = 0; i < results.length(); i++) {
                    JSONObject transactionJSON = results.getJSONObject(i);
                    int id = transactionJSON.getInt("id");
                    String date = transactionJSON.getString("date");
                    String title = transactionJSON.getString("title");
                    Double amount = transactionJSON.getDouble("amount");
                    String itemDescription = transactionJSON.getString("itemDescription");
                    String transactionInterval = transactionJSON.getString("transactionInterval");
                    String endDate = transactionJSON.getString("endDate");
                    int transactionTypeID = transactionJSON.getInt("TransactionTypeId");

                    if (transactionInterval.equals("null")) {
                        transactionInterval = "0";
                    }
                    try {
                        if (endDate.equals("null")) {
                            transactions.add(new Transaction(id, sdf.parse(date), amount, title, itemDescription,
                                    Integer.valueOf(transactionInterval), null, transactionTypeID));
                        } else {
                            transactions.add(new Transaction(id, sdf.parse(date), amount, title, itemDescription,
                                    Integer.valueOf(transactionInterval), sdf.parse(endDate), transactionTypeID));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        callerTypes.onDoneTransactionType(transactionsAll, transactions, account);
    }

    public interface OnGetTransactionTypesDone{
        public void onDoneTransactionType(ArrayList<Transaction> transactionsAll, ArrayList<Transaction> transactions, Account account);
    }


    public void addFromDatabaseToWeb(){
        ArrayList<Transaction> updateTransactions = GetUpdateTransactions();
        if(updateTransactions != null){
            for(Transaction t: updateTransactions){
                update(t, t.getAction());
            }
        }
    }


    public void update(Transaction transactionNew, int action){
        String api_id = this.context.getResources().getString(R.string.api_id);

        URL urlPost = null;
        try {
            if (action == 1) {
                urlPost = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+api_id+"/transactions");
            } else if (action == 2 || action == 3) {
                urlPost = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+api_id+"/transactions/"
                        + transactionNew.getId());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection con = (HttpURLConnection) urlPost.openConnection();
            if (action == 3) {
                con.setRequestMethod("DELETE");
            } else {
                con.setRequestMethod("POST");
            }
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            if (action != 3) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String strDate = dateFormat.format(transactionNew.getDate());
                String strEndDate = "null";
                if (transactionNew.getEndDate() != null) {
                    strEndDate = dateFormat.format(transactionNew.getEndDate());
                }
                String jsonInputString = "{ \"date\": \"" + strDate + "\", \"title\": \"" +
                        transactionNew.getTitle() + "\", \"amount\":" + String.valueOf(transactionNew.getAmount())
                        + ", \"endDate\": \"" + strEndDate + "\", \"itemDescription\": \"" + transactionNew.getItemDescription()
                        + "\", \"transactionInterval\": \"" + String.valueOf(transactionNew.getTransactionInterval())
                        + "\", \"typeId\": " + String.valueOf(transactionNew.getTransactionTypeID()) + " }";
                System.out.println("Ovo je moj jason string " + jsonInputString + "ovo je id " + transactionNew.getId());
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionsUri = Uri.parse("content://rma.provider.transactions/elements");
        String transactionInternalId = String.valueOf(transactionNew.getInternalD());
        cr.delete(transactionsUri, TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID + " = ?", new String[] {transactionInternalId});

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Transaction> GetUpdateTransactions(){
        ArrayList<Transaction> updateTransactions = new ArrayList<>();
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionsUri = Uri.parse("content://rma.provider.transactions/elements");

        String[] columns = new String[]{
                TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID,
                TransactionDBOpenHelper.TRANSACTION_ID,
                TransactionDBOpenHelper.TRANSACTION_TITLE,
                TransactionDBOpenHelper.TRANSACTION_DATE,
                TransactionDBOpenHelper.TRANSACTION_AMOUNT,
                TransactionDBOpenHelper.TRANSACTION_TYPE,
                TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION,
                TransactionDBOpenHelper.TRANSACTION_INTERVAL,
                TransactionDBOpenHelper.TRANSACTION_END_DATE,
                TransactionDBOpenHelper.TRANSACTION_TYPE_ID,
                TransactionDBOpenHelper.TRANSACTION_ACTION

        };
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(transactionsUri,columns,where,whereArgs,order);

        if(cur != null && cur.getCount() > 0){
            cur.moveToFirst();
            do{
                Transaction newTransaction = getThisTransaction(cur);
                updateTransactions.add(newTransaction);
            }while(cur.moveToNext());
            return updateTransactions;
        }
        return null;
    }




    public Transaction getThisTransaction(Cursor cursor){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        int titlePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE);
        int amountPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT);
        int descriptionPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION);
        int datePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_DATE);
        int typePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE);
        int intervalPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERVAL);
        int endDatePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_END_DATE);
        int internalIdPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID);
        int action = 0;
        try {
            int actionPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ACTION);
            action = cursor.getInt(actionPos);
        }
        catch (IllegalArgumentException e){

        }
        int id = 0;
        try{
            int transactionIdPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ID);
            id = cursor.getInt(transactionIdPos);
        }catch (IllegalArgumentException e){

        }
        String title = cursor.getString(titlePos);
        double amount = cursor.getDouble(amountPos);
        String description = cursor.getString(descriptionPos);
        String dateString = cursor.getString(datePos);
        String type = cursor.getString(typePos);
        int interval = cursor.getInt(intervalPos);
        String endDateString = cursor.getString(endDatePos);
        int internalId = cursor.getInt(internalIdPos);



        Transaction newTransaction = new Transaction();
        newTransaction.setId(id);
        newTransaction.setTitle(title);
        newTransaction.setAmount(amount);
        newTransaction.setItemDescription(description);
        newTransaction.setAction(action);
        newTransaction.setInternalD(internalId);

        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        Date d1 = null;
        try{
            d1 = sdf3.parse(dateString);

        }catch (Exception e){ e.printStackTrace(); }

        newTransaction.setDate(d1);

        if (type.equals("INDIVIDUALPAYMENT"))
            newTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
        else if (type.equals("REGULARPAYMENT"))
            newTransaction.setType(Transaction.Type.REGULARPAYMENT);
        else if (type.equals("PURCHASE"))
            newTransaction.setType(Transaction.Type.PURCHASE);
        else if (type.equals("REGULARINCOME"))
            newTransaction.setType(Transaction.Type.REGULARINCOME);
        else if (type.equals("INDIVIDUALINCOME"))
            newTransaction.setType(Transaction.Type.INDIVIDUALINCOME);

        Date d2 = null;
        try{
            d2 = sdf3.parse(endDateString);
        }catch (Exception e){ e.printStackTrace(); }
        newTransaction.setEndDate(d2);
        newTransaction.setTransactionInterval(interval);


        return  newTransaction;
    }


}
