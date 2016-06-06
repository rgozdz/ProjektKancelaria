package com.example.rafal.projektkancelaria;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class fragment_document extends ListFragment {

    private ListView list1;
    public View listView;
    public static ArrayList<HashMap<String, String>> list;
    private Calendar cal;
    private int selectedCount=0;
    public static DocumentListViewAdapter adapter;
    public List<Object> idList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View listView =inflater.inflate(R.layout.fragment_fragmet, container, false);
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

        idList = new ArrayList<>();
        list=new ArrayList<HashMap<String,String>>();


        SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        cal= Calendar.getInstance();
        String dateNow = dateFormat.format(cal.getTime());
        if(MainActivity.idCheck==1) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put(ListConstants.DATE_COLUMN, dateNow);
            temp.put(ListConstants.NAME_COLUMN, "Dokument ws. budowy chodnika");
            temp.put(ListConstants.STATUS_COLUMN, "Oczekuję");
            temp.put(ListConstants.TYPE_COLUMN, "Wniosek");
            temp.put(ListConstants.DESCRIBTION_COLUMN, "W przypadku faktury wystawionej" +
                    " w walucie obcej i stanowiącej dowód poświadczający poniesienie wydatku " +
                    "w ramach projektu, w celu rozliczenia faktury (uwzględnienia we wniosku o " +
                    "płatność), konieczne jest przeliczenie wartości zapłaconej faktury na walutę polską." +
                    " W takim przypadku należy zastosować kurs z dnia, w którym beneficjent opłacił fakturę. ");
            list.add(temp);

            HashMap<String, String> temp2 = new HashMap<String, String>();
            temp2.put(ListConstants.DATE_COLUMN, dateNow);
            temp2.put(ListConstants.NAME_COLUMN, "Wniosek urlopowy");
            temp2.put(ListConstants.STATUS_COLUMN, "Odrzucono");
            temp2.put(ListConstants.TYPE_COLUMN, "Wniosek");
            temp2.put(ListConstants.DESCRIBTION_COLUMN, "W przypadku faktury wystawionej" +
                    " w walucie obcej i stanowiącej dowód poświadczający poniesienie wydatku " +
                    "w ramach projektu, w celu rozliczenia faktury (uwzględnienia we wniosku o " +
                    "płatność), konieczne jest przeliczenie wartości zapłaconej faktury na walutę polską." +
                    " W takim przypadku należy zastosować kurs z dnia, w którym beneficjent opłacił fakturę. ");
            list.add(temp2);

            HashMap<String, String> temp3 = new HashMap<String, String>();
            temp3.put(ListConstants.DATE_COLUMN, dateNow);
            temp3.put(ListConstants.NAME_COLUMN, "Akta sprawy");
            temp3.put(ListConstants.STATUS_COLUMN, "Zatwierdzono");
            temp3.put(ListConstants.TYPE_COLUMN, "Akta");
            temp3.put(ListConstants.DESCRIBTION_COLUMN, "W przypadku faktury wystawionej" +
                    " w walucie obcej i stanowiącej dowód poświadczający poniesienie wydatku " +
                    "w ramach projektu, w celu rozliczenia faktury (uwzględnienia we wniosku o " +
                    "płatność), konieczne jest przeliczenie wartości zapłaconej faktury na walutę polską." +
                    " W takim przypadku należy zastosować kurs z dnia, w którym beneficjent opłacił fakturę. ");
            list.add(temp3);

            HashMap<String, String> temp4 = new HashMap<String, String>();
            temp4.put(ListConstants.DATE_COLUMN, dateNow);
            temp4.put(ListConstants.NAME_COLUMN, "Dokument ws. budowy chodnika");
            temp4.put(ListConstants.STATUS_COLUMN, "Zatwierdzono");
            temp4.put(ListConstants.TYPE_COLUMN, "Akta");
            temp4.put(ListConstants.DESCRIBTION_COLUMN, "W przypadku faktury wystawionej" +
                    " w walucie obcej i stanowiącej dowód poświadczający poniesienie wydatku " +
                    "w ramach projektu, w celu rozliczenia faktury (uwzględnienia we wniosku o " +
                    "płatność), konieczne jest przeliczenie wartości zapłaconej faktury na walutę polską." +
                    " W takim przypadku należy zastosować kurs z dnia, w którym beneficjent opłacił fakturę. ");
            list.add(temp4);
        }if(MainActivity.idCheck!=1){

            HashMap<String, String> temp = new HashMap<String, String>();
            list.add(temp);

        }

        adapter=new DocumentListViewAdapter(getActivity(), list);
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
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {return false;}

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
                Toast.makeText(getContext(),"Usunięto "
                        +idList.size()+" elem.",Toast.LENGTH_SHORT).show();

                list1.clearFocus();

            }
        });


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String nameDokBundle= list.get(position).get(ListConstants.NAME_COLUMN).toString();
        String typeDokBundle= list.get(position).get(ListConstants.TYPE_COLUMN).toString();
        String statusDokBundle= list.get(position).get(ListConstants.STATUS_COLUMN).toString();
        String describtionDokBundle= list.get(position).get(ListConstants.DESCRIBTION_COLUMN).toString(); //in future
        Intent intent = new Intent(getContext(),DocumentDetails.class);
        intent.putExtra("NAME",nameDokBundle);
        intent.putExtra("TYPE",typeDokBundle);
        intent.putExtra("STATUS",statusDokBundle);
        intent.putExtra("DESCRIBTION",describtionDokBundle);

        this.startActivity(intent);
        super.onListItemClick(l, v, position, id);



    }
}
