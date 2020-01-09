package com.s3a513114.appclass_hw23_return_url_string;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    private ListView listView;
    private ListAdapter listAdapter;
    ArrayList<String> myList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myList = new ArrayList<String>(); //指定是String的型態
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        listView.setAdapter(listAdapter);
        textView = (TextView)findViewById(R.id.textView);
        myList.add("ε٩(๑> ₃ <)۶з");
    }
    public void btnJsonOnClick(View view){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Create background thread to connect and get data
            new TransTask()
                    .execute("http://atm201605.appspot.com/h");
            listView.setAdapter(listAdapter);

        } else {
            textView.setText("No network connection available.");
        }
    }
    class TransTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while(line!=null){
                    Log.d("HTTP", line);
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("JSON", s);
            parseJSON(s);
        }
        private void parseJSON(String s) {
        try{
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String account = jsonObject.getString("account");
                String date = jsonObject.getString("date");
                String amount = jsonObject.getString("amount");
                String type = jsonObject.getString("type");
                //myList.add("\naccount: " + account + "\ndate: " + date +"\namount: " + amount + "\ntype: " +type +"\n");
                textView.setText(textView.getText() + "\naccount: " + account + "   date: " + date +"\namount: " + amount + "   type: " +type +"\n");
            }
        }catch (JSONException e) {
                System.err.println("Error: " + e.getMessage());
        }
        }
    }
}
