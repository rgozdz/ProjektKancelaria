package com.example.rafal.projektkancelaria;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.rafal.projektkancelaria.ListConstants.NAME_COLUMN;
import static com.example.rafal.projektkancelaria.ListConstants.STATUS_COLUMN;
import static com.example.rafal.projektkancelaria.ListConstants.TYPE_COLUMN;

/**
 * Created by rafal on 27.03.2016.
 */
public class DocumentListViewAdapter extends BaseAdapter {


    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView dateText;
    TextView nameText;
    TextView typeText;
    TextView statusText;


    public DocumentListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.list = list;
        this.activity=activity;
    }




    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();

        if(MainActivity.brakDok){

            convertView = inflater.inflate(R.layout.no_elements_row, null);
            this.notifyDataSetChanged();
            return convertView;

        }else if(convertView == null) {

            convertView = inflater.inflate(R.layout.list_row, null);
            nameText = (TextView) convertView.findViewById(R.id.name);
            typeText = (TextView) convertView.findViewById(R.id.type);
            statusText = (TextView) convertView.findViewById(R.id.status);
        }
            HashMap<String, String> map=list.get(position);
            nameText.setText(map.get(NAME_COLUMN));
            typeText.setText(map.get(TYPE_COLUMN));
            statusText.setText(map.get(STATUS_COLUMN));


        this.notifyDataSetChanged();
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {

      return !MainActivity.brakDok;
    }
}
