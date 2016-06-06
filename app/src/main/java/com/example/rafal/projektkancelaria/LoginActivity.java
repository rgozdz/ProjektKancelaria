package com.example.rafal.projektkancelaria;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by rafal on 22.03.2016.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final EditText loginField = (EditText)findViewById(R.id.name_login);
        final EditText passField = (EditText)findViewById(R.id.passText);
        final CheckBox checkLogin = (CheckBox)findViewById(R.id.checLog);
        final String PREFS_NAME = "PrefsFile";
        final String PREF_USERNAME ="username";
        final String PREF_PASSWORD="password";

        SharedPreferences pref= getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String loginPref = pref.getString(PREF_USERNAME, null);
        final String passPref =pref.getString(PREF_PASSWORD,null);

        if(loginPref!=null){
            checkLogin.setChecked(true);
        }
        loginField.setText(loginPref);
        passField.setText(passPref);

        Button logBtn =(Button)findViewById(R.id.login_button);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = loginField.getText().toString();
                String pass = passField.getText().toString();

               if(login.trim().contentEquals("rgozdz") && pass.trim().contentEquals("raffi5458")){

                   if(checkLogin.isChecked()) {
                       getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                               .putString(PREF_USERNAME, login)
                               .putString(PREF_PASSWORD, pass)
                               .commit();
                   }else {

                       getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().commit();
                   }
                    Toast.makeText(getApplicationContext(),"Zalogowano",Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(v.getContext() ,MainActivity.class);
                    LoginActivity.this.startActivity(mainIntent);
                    finish();


                }else
                    Toast.makeText(getApplicationContext(),"Błędny login lub hasło",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class RestLoginOperation extends AsyncTask<String ,Void, Void>{

        //final HttpClient httpClient = new DefaultHttpClient();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
