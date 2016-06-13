package com.example.rafal.projektkancelaria;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddDocumentDialog extends DialogFragment implements AsyncResponse {


    private Calendar cal;
    private EditText nameField;
    private Spinner typeField;
    private EditText describtionField;
    static boolean isEditable = false;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Rejestrowanie wniosku");
        builder.setMessage("Wprowadź dane wniosku");

        final HttpAsyncTaskAdd task = new HttpAsyncTaskAdd((MainActivity) getActivity());

        task.delegate=this;

        View view = getActivity().getLayoutInflater().inflate(R.layout.add_layout, null);
        nameField= (EditText)view.findViewById(R.id.dok_name);
        typeField =(Spinner)view.findViewById(R.id.type_spinner);
        describtionField =(EditText)view.findViewById(R.id.dok_describtion);

        List<String> categories = new ArrayList<String>();
        categories.add("wniosek_podwyzka");
        categories.add("wniosek_urlop");
        categories.add("wniosek_kadrowy");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeField.setAdapter(dataAdapter);


        builder.setView(view)
                .setPositiveButton(R.string.add_document, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                        cal = Calendar.getInstance();
                        String dateNow = dateFormat.format(cal.getTime());

                        String name = nameField.getText().toString();
                        String type = typeField.getSelectedItem().toString();
                        String desctibtion = describtionField.getText().toString();


                        if (!name.isEmpty()) {

                            task.execute(LoginActivity.globalIdUser,type,name,desctibtion);
                            Toast.makeText(getActivity(), "Dodano dokument " + name, Toast.LENGTH_SHORT).show();
                            isEditable=true;

                        } else {

                            nameField.setHint("Nie podano tytułu");
                        }
                    }
                }).setNegativeButton(R.string.cancel_adding, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog=builder.create();

        return dialog;


    }

    @Override
    public void processFinish(String output) {

    }


    public class HttpAsyncTaskAdd extends AsyncTask<String, Void, String> {

        public AsyncResponse delegate = null;
        private ProgressDialog dialog;

        public HttpAsyncTaskAdd(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Dodawanie dokumentu...");
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

            String urlString = "http://dreja-programistyczny.rhcloud.com/wniosek/add";

            URL url = null;
            String result = null;
            try {

                url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                String userId = params[0];
                String wniosekTyp = params[1];
                String tytul = params[2];
                String tresc = params[3];

                JSONObject jsonObj = new JSONObject();

                jsonObj.put("userId", userId);
                jsonObj.put("wniosekTyp", wniosekTyp);
                jsonObj.put("tytul", tytul);
                jsonObj.put("tresc", tresc);

                String json = jsonObj.toString();

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(urlString);
                StringEntity se = new StringEntity(json);

                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                result = reader.readLine();

                System.out.println("janusz " + result);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            return result;
        }


    }


    public class RafalException extends Exception{

        RafalException(String message){

            super(message);


        }



    }



}
