package com.example.rafal.projektkancelaria;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by rafal on 09.04.2016.
 */
public class ToAcceptDialog extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ArrayList<String> department = new ArrayList<String>();
        department.add("Strostwo");
        department.add("Wicestarostwo");
        department.add("Księgowość");
        department.add("HR");

        ArrayList<String> persons = new ArrayList<String>();
        persons.add("Kowalski Jan");
        persons.add("Włodzimierz Kasprzak");
        persons.add("Janina Pasek");
        persons.add("Rafał Przybylski");

        View view = getActivity().getLayoutInflater().inflate(R.layout.to_accept_layout, null);
        builder.setView(view);
        final Spinner dzialSpinner = (Spinner)view.findViewById(R.id.dzial_spinner);
        final Spinner osbaSpinner =(Spinner)view.findViewById(R.id.osoba_spinner);
        ArrayAdapter<String> dzialAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, department);
        ArrayAdapter<String> osobyAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,persons);

        dzialSpinner.setAdapter(dzialAdapter);
        osbaSpinner.setAdapter(osobyAdapter);

        builder.setTitle("Przekaż do akceptacji");
        builder.setMessage("Wybierz osobę, której ma być przekazany wniosek");

        builder.setPositiveButton("Dekretuj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                String personToAccept = osbaSpinner.getSelectedItem().toString();
                String departmentToAccept =dzialSpinner.getSelectedItem().toString();
                Toast.makeText(getActivity(), "Zadekretowano wniosek na "+personToAccept+" w dziale "+departmentToAccept, Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }
}
