package com.example.rafal.projektkancelaria;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by rafal on 30.03.2016.
 */
public class DocumentDetails extends AppCompatActivity {

    TextView tName;
    TextView tType;
    TextView tStatus;
    TextView tDescribtion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);

        setContentView(R.layout.document_details);
        tName=(TextView)findViewById(R.id.textViewName);
        tType=(TextView)findViewById(R.id.textViewType);
        tStatus=(TextView)findViewById(R.id.textViewStatus);
        tDescribtion=(TextView)findViewById(R.id.textViewDescribtion);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){

            tName.setText(extras.getString("NAME"));
            tType.setText(extras.getString("TYPE"));
            tStatus.setText(extras.getString("STATUS"));
            tDescribtion.setText(extras.getString("DESCRIBTION"));

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                break;
            case R.id.accept :
                ConfirmDialog confDialog =new ConfirmDialog();
                confDialog.show(getSupportFragmentManager(),"akceptacja");
                break;
            case R.id.discard :
                RejectDialog rejectDialog = new RejectDialog();
                rejectDialog.show(getSupportFragmentManager(),"odrzucanie");
                break;
            case R.id.sendToAccept:
                ToAcceptDialog acceptDialog = new ToAcceptDialog();
                acceptDialog.show(getSupportFragmentManager(),"dekretowanie");
                break;
            default:
                break;
        }
        return true;
    }
}
