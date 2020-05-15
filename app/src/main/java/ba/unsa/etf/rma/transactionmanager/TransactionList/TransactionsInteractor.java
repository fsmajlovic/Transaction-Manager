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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.userModel;

public class TransactionsInteractor extends AsyncTask<String, Integer, Void> implements ITransactionInteractor {

    ArrayList<String> transactionTypes;
    ArrayList<Transaction> transactions;
    private OnGetTransactionTypesDone callerTypes;
    private OnGetTransactionListDone callerTransactions;

    public TransactionsInteractor(){

    }

    public TransactionsInteractor(OnGetTransactionTypesDone p) {
        callerTypes = p;
        transactionTypes = new ArrayList<String>();
    };

    public TransactionsInteractor(OnGetTransactionListDone p){
        callerTransactions = p;
        transactions = new ArrayList<>();
    }

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
        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/transactionTypes";
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            JSONObject jo = new JSONObject(result);
            JSONArray results = jo.getJSONArray("rows");
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
        return null;
    }

    protected Void getTransactions(String... strings) {
        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/1a90adbb-4968-4995-98f6-bde3431728d5/transactions";
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            JSONObject jo = new JSONObject(result);
            JSONArray results = jo.getJSONArray("transactions");
            for (int i = 0; i < results.length(); i++) {
                JSONObject transactionJSON = results.getJSONObject(i);
                int id = transactionJSON.getInt("id");

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        callerTypes.onDoneTransactionType(transactionTypes);
        callerTransactions.onDoneTransactionList(transactions);
    }

    public interface OnGetTransactionTypesDone{
        public void onDoneTransactionType(ArrayList<String> results);
    }

    public interface OnGetTransactionListDone{
        public void onDoneTransactionList(ArrayList<Transaction> results);
    }


}
