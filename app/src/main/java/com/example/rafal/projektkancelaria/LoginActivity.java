package com.example.rafal.projektkancelaria;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by rafal on 22.03.2016.
 */
public class LoginActivity extends Activity implements AsyncResponse {

    EditText loginField;
    EditText passField;
    CheckBox checkLogin;
    String PREFS_NAME;
    String PREF_USERNAME;
    String PREF_PASSWORD;
    String login;
    String pass;
    static String globalIdUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginField = (EditText) findViewById(R.id.name_login);
        passField = (EditText) findViewById(R.id.passText);
        checkLogin = (CheckBox) findViewById(R.id.checLog);
        PREFS_NAME = "PrefsFile";
        PREF_USERNAME = "username";
        PREF_PASSWORD = "password";

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String loginPref = pref.getString(PREF_USERNAME, null);
        final String passPref = pref.getString(PREF_PASSWORD, null);

        if (loginPref != null) {
            checkLogin.setChecked(true);
        }
        loginField.setText(loginPref);
        passField.setText(passPref);

        Button logBtn = (Button) findViewById(R.id.login_button);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = loginField.getText().toString();
                pass = passField.getText().toString();

                HttpAsyncTask task = new HttpAsyncTask(LoginActivity.this);

                task.delegate = LoginActivity.this;
                task.execute(login, pass);

            }
        });
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @Override
    public void processFinish(String idUser) {

        if (idUser.length() != 17) {


            if (checkLogin.isChecked()) {
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                        .putString(PREF_USERNAME, login)
                        .putString(PREF_PASSWORD, pass)
                        .commit();
            } else {

                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().commit();
            }
            globalIdUser=idUser;
            Toast.makeText(getApplicationContext(), "Zalogowano" +idUser, Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(this, MainActivity.class);
            LoginActivity.this.startActivity(mainIntent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Błędny login lub hasło", Toast.LENGTH_SHORT).show();
        }

    }


    public class HttpAsyncTask extends AsyncTask<String, Void, String> {

        public AsyncResponse delegate = null;
        private ProgressDialog dialog;

        public HttpAsyncTask(LoginActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Logowanie...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing())
                dialog.hide();

            delegate.processFinish(s);
        }


        @Override
        protected String doInBackground(String... params) {

            String urlString = "http://dreja-programistyczny.rhcloud.com/login/get-user";

            URL url = null;
            String idUser = null;
            try {

                url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                String login = params[0];
                String pass = params[1];

                JSONObject jsonObj = new JSONObject();

                jsonObj.put("login", login);
                jsonObj.put("password", pass);

                String json = jsonObj.toString();

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(urlString);
                StringEntity se = new StringEntity(json);

                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);

                InputStream inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null)
                    idUser = convertInputStreamToString(inputStream).toString();
                else
                    System.out.println("Did not work!");

                System.out.println("janusz " + idUser);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            return idUser;
        }


    }


}