package com.androidstudio.easyGOBand;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ActivitySearchForProviderId extends AppCompatActivity {
    EditText txfSearchForProviderId;
    TextView txvProviders;
    Button btnSearch;
    ArrayList<String> cont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfor_providerid);

        txfSearchForProviderId = findViewById(R.id.txfSearchForProviderId);
        txvProviders = findViewById(R.id.txvProviders);
        btnSearch = findViewById(R.id.btnSearch);

        cont = new ArrayList<>();

        // Si pulsamos boton Search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchTask searchTask = new SearchTask();
                searchTask.execute("https://pnny0h3cuf.execute-api.eu-west-1.amazonaws.com/dev/providers/1");
            }
        });

    }

    private class SearchTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            InputStream in;
            String urlStr = strings[0];
            String s = null;
            StringBuilder stBuilder = new StringBuilder();
            try {
                in = openHttpConnection(urlStr);
                BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
                while ((s = inReader.readLine()) != null) {
                    stBuilder.append(s + "\n");
                }
                s = stBuilder.toString();
                in.close();
            } catch (IOException ex) {
                Log.d("NetworkingActivity", ex.getLocalizedMessage());
            }
            return s;
        }

        @Override
        protected void onPostExecute(String resultStr) {
            txvProviders.setText(getDataFromJSON(resultStr));
        }

        private String getDataFromJSON(String jsonStr) {
            // JSON keys values {"id":78, "name":"TKT CE ABONO 95", "access_group_id":1, "access_group_name":"Abono",
            //  "total_uses:0", "sessions":["id":19, "name":"01-ABONO"], "structure_decode":false,
            //  "modified":"2017-05-30T17:42:27.000Z", "basic_product_id":27}
            // Recorremos JSON y añadimos lista String
            String resultStr = "";
            try {
                JSONArray array = new JSONArray(jsonStr);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o = (JSONObject) array.get(i);
                    if(o.getInt("id") == Integer.parseInt(txfSearchForProviderId.getText().toString())) {
                        resultStr += "Id: " + o.getInt("id") + "\n" +
                                "Name: " + o.getString("name") + "\n" +
                                "Sessions: " + "\n";
                        JSONArray arrSessions = o.getJSONArray("sessions");
                        for(int j = 0; j < arrSessions.length(); j++) {
                            JSONObject oSession = (JSONObject) arrSessions.get(j);
                            resultStr += "\tSession id: " + oSession.getInt("id") + "\n" +
                                         "\tSession name: " + oSession.getString("name") + "\n";
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultStr;
        }
        private InputStream openHttpConnection(String urlString) throws IOException {
            InputStream in = null;
            int response;
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Authorization", "Basic cJmAc71jah17sgqi1jqaksvaksda=");
            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");
            try{
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setInstanceFollowRedirects(true);

                httpConn.setRequestMethod("GET"); //not used at this point
                httpConn.connect();
                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            }
            catch (Exception ex) {
                Log.d("Networking", ex.getLocalizedMessage());
                throw new IOException("Error connecting");
            }
            return in;
        }
    }
}
