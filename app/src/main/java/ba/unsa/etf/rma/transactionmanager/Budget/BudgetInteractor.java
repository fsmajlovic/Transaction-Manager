package ba.unsa.etf.rma.transactionmanager.Budget;

import android.content.res.Resources;
import android.graphics.Movie;
import android.os.AsyncTask;

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

import ba.unsa.etf.rma.transactionmanager.Account;

public class BudgetInteractor extends AsyncTask<String, Integer, Void> implements IBudgetInteractor {

    Account account;
    private OnAccountSearchDone caller;

    public BudgetInteractor(OnAccountSearchDone p){
        caller = p;
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
        String query = null;
        try {
            query = URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/" + "1a90adbb-4968-4995-98f6-bde3431728d5";
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
            System.out.println("IZUZETAK1");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IZUZETAK2");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("IZUZETAK3");
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onDone(account);
    }

    public interface OnAccountSearchDone{
        void onDone(Account result);
    }
}
