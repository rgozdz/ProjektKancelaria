package com.example.rafal.projektkancelaria;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class EditDocumentDialog extends DialogFragment {


    private Calendar cal;
    private EditText nameField;
    private Spinner typeField;
    private EditText describtionField;
    private String documentName;
    private String typeDoc;
    private String describtionDoc;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edycja wniosku");
        builder.setMessage("Wprowadź zmiany");


        View view = getActivity().getLayoutInflater().inflate(R.layout.add_layout, null);
        nameField= (EditText)view.findViewById(R.id.dok_name);
        typeField =(Spinner)view.findViewById(R.id.type_spinner);
        describtionField =(EditText)view.findViewById(R.id.dok_describtion);

        List<String> categories = new ArrayList<String>();
        categories.add("Wniosek o podwyżkę");
        categories.add("Wniosek o urop");
        categories.add("Decyzja");
        categories.add("Plan urlopowy");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeField.setAdapter(dataAdapter);


        builder.setView(view)
                .setPositiveButton(R.string.save_document, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                        cal = Calendar.getInstance();
                        String dateNow = dateFormat.format(cal.getTime());

                        String name = nameField.getText().toString();
                        String type = typeField.getSelectedItem().toString();
                        String desctibtion =describtionField.getText().toString();

                        if(!name.isEmpty())
                        {
                            HashMap<String, String> temp = new HashMap<String, String>();
                            temp.put(ListConstants.DATE_COLUMN, dateNow);
                            temp.put(ListConstants.NAME_COLUMN, name);
                            temp.put(ListConstants.STATUS_COLUMN, "Nowy");
                            temp.put(ListConstants.TYPE_COLUMN, type);
                            temp.put(ListConstants.DESCRIBTION_COLUMN,desctibtion);
                            fragment_document.list.add(temp);

                            Toast.makeText(getActivity(), "Dodano dokument " + name, Toast.LENGTH_SHORT).show();
                            //dialog.dismiss();
                        }else{
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




}
