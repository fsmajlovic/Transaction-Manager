package ba.unsa.etf.rma.transactionmanager.TransactionList;

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
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.userModel;

public class TransactionsInteractor extends AsyncTask<String, Integer, Void> implements ITransactionInteractor {

    private ArrayList<String> transactionTypes;
    private ArrayList<Transaction> transactions;
    private OnGetTransactionTypesDone callerTypes;

    public TransactionsInteractor(){

    }

    public TransactionsInteractor(OnGetTransactionTypesDone p) {
        callerTypes = p;
        transactionTypes = new ArrayList<String>();
        transactions = new ArrayList<Transaction>();
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
        String query = "";
        try {
            query = URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/transactionTypes";
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            JSONObject jo = new JSONObject(result);
            JSONArray results = jo.getJSONArray("rows");
            transactionTypes.add("All");
            for (int i = 0; i < results.length(); i++) {
                JSONObject transactionType = results.getJSONObject(i);
                String title = transactionType.getString("name");
                transactionTypes.add(title);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int page = 0; page < 3; page++) {
            String url2 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/1a90adbb-4968-4995-98f6-bde3431728d5/transactions/filter?page=" + page + "&" + strings[0];
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                URL url = new URL(url2);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = convertStreamToString(in);
                JSONObject jo = new JSONObject(result);
                JSONArray results = jo.getJSONArray("transactions");
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
        callerTypes.onDoneTransactionType(transactionTypes, transactions);
    }

    public interface OnGetTransactionTypesDone{
        public void onDoneTransactionType(ArrayList<String> results, ArrayList<Transaction> transactions);
    }


    //OLD
    @Override
    public ArrayList<Transaction> getTransactions() {
        return userModel.transactions;
    }

    @Override
    public double getBudget() {
        return userModel.account.getBudget();
    }

    @Override
    public double getTotalLimit() {
        return userModel.account.getTotalLimit();
    }

    @Override
    public double getMonthLimit() {
        return userModel.account.getMonthLimit();
    }

    @Override
    public void setBudget(double budget) {
        userModel.account.setBudget(budget);
    }

    @Override
    public void setMonthLimit(double monthLimit) {
        userModel.account.setMonthLimit(monthLimit);
    }

    @Override
    public void setTotalLimit(double totalLimit) {
        userModel.account.setTotalLimit(totalLimit);
    }

    @Override
    public Account getAccount() {
        return userModel.account;
    }
}
