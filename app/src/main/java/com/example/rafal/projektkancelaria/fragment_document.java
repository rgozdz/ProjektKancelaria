package com.example.rafal.projektkancelaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class fragment_document extends ListFragment implements AsyncJsonResponse {

    private ListView list1;
    public View listView;
    public static ArrayList<HashMap<String, String>> list;
    private Calendar cal;
    private int selectedCount = 0;
    public static DocumentListViewAdapter adapter;
    public List<Object> idList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View listView = inflater.inflate(R.layout.fragment_fragmet, container, false);
        list1 = (ListView) listView.findViewById(android.R.id.list);
        return listView;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity.brakDok=false;
        HttpAsyncTaskGetDocuments task = new HttpAsyncTaskGetDocuments((MainActivity) getActivity());

        task.delegate = this;

        switch (MainActivity.idCheck) {

            case 1:
                task.execute(LoginActivity.globalIdUser, "wniosek_kadrowy");
                break;
            case 2:
                task.execute(LoginActivity.globalIdUser, "wniosek_urlop");
                break;
            case 3:
                task.execute(LoginActivity.globalIdUser, "wniosek_podwyzka");
                break;
            case 4:
                task.execute(LoginActivity.globalIdUser, "wniosek_podwyzka");
                break;
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String nameDokBundle = list.get(position).get(ListConstants.NAME_COLUMN);
        String typeDokBundle = list.get(position).get(ListConstants.TYPE_COLUMN);
        String statusDokBundle = list.get(position).get(ListConstants.STATUS_COLUMN);
        String describtionDokBundle = list.get(position).get(ListConstants.DESCRIBTION_COLUMN); //in future
        Intent intent = new Intent(getContext(), DocumentDetails.class);
        intent.putExtra("NAME", nameDokBundle);
        intent.putExtra("TYPE", typeDokBundle);
        intent.putExtra("STATUS", statusDokBundle);
        intent.putExtra("DESCRIBTION", describtionDokBundle);

        this.startActivity(intent);
        super.onListItemClick(l, v, position, id);

    }

    @Override
    public void processFinishJSON(JSONArray output) throws JSONException {


            idList = new ArrayList<>();
            list = new ArrayList<HashMap<String, String>>();


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
            cal = Calendar.getInstance();
            String dateNow = dateFormat.format(cal.getTime());

            for (int i = 0; i < output.length(); i++) {


                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(ListConstants.DATE_COLUMN, dateNow);
                temp.put(ListConstants.NAME_COLUMN, output.getJSONObject(i).optString("tytul"));
                temp.put(ListConstants.STATUS_COLUMN, output.getJSONObject(i).getJSONObject("status").optString("wartosc"));
                temp.put(ListConstants.TYPE_COLUMN, output.getJSONObject(i).getJSONObject("typ").optString("nazwa"));
                temp.put(ListConstants.DESCRIBTION_COLUMN, output.getJSONObject(i).optString("tresc"));
                list.add(temp);
            }

            if(list.size()==0){

                MainActivity.brakDok=true;
                HashMap<String, String> temp = new HashMap<String, String>();
                list.add(temp);

            }



            adapter = new DocumentListViewAdapter(getActivity(), list);
            list1.setAdapter(adapter);
            list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            list1.setItemsCanFocus(false);

            list1.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                    if (list1.isItemChecked(position)) {
                        selectedCount++;
                        idList.add(list.get(position));
                    } else {
                        selectedCount--;
                        idList.remove(list.get(position));
                    }
                    if (selectedCount == 1) {

                        MainActivity.fabEdit.show();
                    } else if (selectedCount != 1 && MainActivity.fabEdit.isShown()) {
                        MainActivity.fabEdit.hide();
                    }
                    //list1.getItemAtPosition(position);
                    mode.setTitle("Zaznaczono " + selectedCount + " elem.");
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {

//                MenuInflater inflater = mode.getMenuInflater();
//                inflater.inflate(R.menu.menu_context,menu);
                    MainActivity.fab.hide();
                    MainActivity.fabDelete.show();

                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {


                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                    if (MainActivity.fabEdit.isShown()) {
                        MainActivity.fabEdit.hide();
                    }
                    MainActivity.fab.show();
                    MainActivity.fabDelete.hide();
                    selectedCount = 0;
                }
            });


        MainActivity.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                list.removeAll(idList);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Usunięto "
                        + idList.size() + " elem.", Toast.LENGTH_SHORT).show();

                list1.clearFocus();

            }
        });


    }

    private class HttpAsyncTaskGetDocuments extends AsyncTask<String, Void, JSONArray> {

        public AsyncJsonResponse delegate = null;
        private ProgressDialog dialog;

        public HttpAsyncTaskGetDocuments(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Wczytywanie...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(JSONArray s) {
            if (dialog.isShowing())
                dialog.hide();

            try {
                delegate.processFinishJSON(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            JSONArray finalResult = null;
            String urlString = "http://dreja-programistyczny.rhcloud.com/wniosek/get-list";
            URL url = null;
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

                JSONObject jsonObj = new JSONObject();

                jsonObj.put("userId", userId);
                jsonObj.put("wniosekTyp", wniosekTyp);

                String json = jsonObj.toString();

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(urlString);
                StringEntity se = new StringEntity(json);

                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                String recivejson = reader.readLine();
                JSONTokener tokener = new JSONTokener(recivejson);
                finalResult = new JSONArray(tokener);

                if(recivejson.length()==2)
                    MainActivity.brakDok=true;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            return finalResult;
        }
    }
}
