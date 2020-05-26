package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.content.Context;
import android.graphics.Movie;
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

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Transaction;


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

}
