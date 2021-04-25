package com.androidstudio.easyGOBand;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ActivityProviderDetail extends AppCompatActivity {
    TextView txfId, txfName, txfAccessGroupId, txfAccessGroupName, txfTotalUses, txfSessionId, txfSessionName, txfEventId,
                txfStructureDecode, txfModified, txfBasicProductId;
    int IDExtra;
    Provider provider;
    List<Session> sessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_detail);

        //Recogemos el ID del proveedor del ListView
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        IDExtra = extras.getInt("id");

        txfId = findViewById(R.id.txfId);
        txfName = findViewById(R.id.txfName);
        txfSessionId = findViewById(R.id.txfSessionId);
        txfSessionName = findViewById(R.id.txfSessionName);

        // Ejecutamos el Asynctask
        SelectProviderByIdTask selectProviderByIdTask = new SelectProviderByIdTask();
        selectProviderByIdTask.execute("https://pnny0h3cuf.execute-api.eu-west-1.amazonaws.com/dev/providers/1");

    }
    private class SelectProviderByIdTask extends AsyncTask<String, Void, String> {
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
            // Llenamos el objeto Provider a partir de json
            getProviderByIdFromJSON(jsonStr);
            // Pegamos el contenido del objeto en los EditText
            txfId.setText(""+provider.getId());
            txfName.setText(provider.getName());

            String cadSessionId = "";
            String cadSessionName = "";
            sessions = provider.getSessions();
            // Recorremos la list de Sessions y acumulamos cada campo a una vble String
            for(Session session: sessions) {
                cadSessionId += session.getId() + "\n";
                cadSessionName += session.getName() + "\n";
            }
            // Volcamos las vbles a los EditText
            txfSessionId.setText(cadSessionId);
            txfSessionName.setText(cadSessionName);
        }
    }
    private void getProviderByIdFromJSON(String jsonStr) {
        // JSON keys values {"id":78, "name":"TKT CE ABONO 95", "access_group_id":1, "access_group_name":"Abono",
        //  "total_uses:0", "sessions":["id":19, "name":"01-ABONO"], "structure_decode":false,
        //  "modified":"2017-05-30T17:42:27.000Z", "basic_product_id":27}
        // Recorremos JSON y añadimos lista String
        try {
            JSONArray array = new JSONArray(jsonStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = (JSONObject) array.get(i);
                // Si el id del objeto coincide con el ID del Proveedor clicado
                if(o.getInt("id") == IDExtra) {
                    provider = new Provider();
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
                }

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
