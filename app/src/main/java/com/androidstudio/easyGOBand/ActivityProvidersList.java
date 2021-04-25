package com.androidstudio.easyGOBand;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class ActivityProvidersList extends AppCompatActivity {
    ListView lstProviders;
    ArrayList<Provider> providers;
    ArrayList<Session> sessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providerslist);

        lstProviders = findViewById(R.id.lstProviders);

        FillListProvidersTask fillListProvidersTask = new FillListProvidersTask();
        fillListProvidersTask.execute("https://pnny0h3cuf.execute-api.eu-west-1.amazonaws.com/dev/providers/1");

    }

    private class FillListProvidersTask extends AsyncTask<String, Void, String> {
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
        protected void onPostExecute(String jsonStr) {
            // Llenamos la lista de proveedores
            fillListFromJSON(jsonStr);
            // Añadimos la lista al adaptador
            AdaptadorTitulares adaptador =
                    new AdaptadorTitulares(getApplicationContext(), providers);
            lstProviders.setAdapter(adaptador);
            Toast.makeText(getApplicationContext(), "Size: " + providers.size(), Toast.LENGTH_LONG).show();
            // Cuando se pulsa sobre un elemento de la ListView
            lstProviders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Vamos a la actividad detalle del Proveedor
                    Intent intent = new Intent(ActivityProvidersList.this, ActivityProviderDetail.class);
                    //Pasamos el id del proveedor que hemos clicado
                    intent.putExtra("id", providers.get(i).getId());
                    startActivity(intent);
                }
            });
        }

        private void fillListFromJSON(String jsonStr) {
            // JSON keys values {"id":78, "name":"TKT CE ABONO 95", "access_group_id":1, "access_group_name":"Abono",
            //  "total_uses:0", "sessions":["id":19, "name":"01-ABONO"], "structure_decode":false,
            //  "modified":"2017-05-30T17:42:27.000Z", "basic_product_id":27}
            providers = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(jsonStr);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o = (JSONObject) array.get(i);
                    Provider provider = new Provider();
                    provider.setId(o.getInt("id"));
                    provider.setName(o.getString("name"));

                    sessions = new ArrayList<>();
                    JSONArray arrSessions = o.getJSONArray("sessions");
                    for(int j = 0; j < arrSessions.length(); j++) {
                        JSONObject oSession = (JSONObject) arrSessions.get(j);
                        Session session = new Session();
                        session.setId(oSession.getInt("id"));
                        session.setName(oSession.optString("name"));
                        sessions.add(session);
                    }
                    provider.setSessions(sessions);
                    providers.add(provider);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private InputStream openHttpConnection(String urlString) throws IOException {
            InputStream in = null;
            int response;
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Authorization", "Basic cJmAc71jah17sgqi1jqaksvaksda=");
            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");
            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setInstanceFollowRedirects(true);

                httpConn.setRequestMethod("GET"); //not used at this point
                httpConn.connect();
                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (Exception ex) {
                Log.d("Networking", ex.getLocalizedMessage());
                throw new IOException("Error connecting");
            }
            return in;
        }

    }
}
