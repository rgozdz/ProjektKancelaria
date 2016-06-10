package com.example.rafal.projektkancelaria;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    Toolbar toolbar;
    boolean doubleBackPress;
    public static FloatingActionButton fab;
    public static FloatingActionButton fabDelete;
    public static FloatingActionButton fabEdit;
    public static int idCheck=1;
    public static boolean brakDok= false;
    private boolean status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.overridePendingTransition(R.anim.animation_enter,R.anim.animation_leave);
        fragment_document fragment =new fragment_document();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabDelete =(FloatingActionButton) findViewById(R.id.fabDelete);
        fabEdit =(FloatingActionButton) findViewById(R.id.fabEdit);
        fabDelete.hide();
        fabEdit.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddDocumentDialog dialog = new AddDocumentDialog();
                dialog.show(getFragmentManager(), "My alert");
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditDocumentDialog dialog = new EditDocumentDialog();
                //dialog.setArguments();
                dialog.show(getFragmentManager(), "My alert");

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(doubleBackPress){
            super.onBackPressed();
        }else{

            doubleBackPress=true;
            Toast.makeText(this,"Naciśnij drugi raz aby wyjść",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackPress=false;
                }
            },2000);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }




//    @Override
//    public void invalidateOptionsMenu() {
//
//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            return super.onCreateOptionsMenu(menu);
//        }
//
//        super.invalidateOptionsMenu();
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_logout:
                Intent loguotIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loguotIntent);
                finish();
                break;
            case R.id.nav_kadrowe:
                idCheck=1;
                getSupportActionBar().setTitle("Wnioski kadrowe");
                fragment_document fragment =new fragment_document();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_urlopowe:
                idCheck=2;
                getSupportActionBar().setTitle("Wnioski urlopowe");
                fragment_document fragmentAccept =new fragment_document();
                fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentAccept);
                fragmentTransaction.commit();
                break;
            case R.id.nav_podwyzka:

                idCheck=3;
                getSupportActionBar().setTitle("Wnioski o podwyżkę");
                fragment_document fragmentWait =new fragment_document();
                fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentWait);
                fragmentTransaction.commit();
                break;
            case R.id.nav_acceptation:

                idCheck=4;
                getSupportActionBar().setTitle("Do akceptacji");
                fragment_document fragmentWait2 =new fragment_document();
                fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentWait2);
                fragmentTransaction.commit();
                break;


            case R.id.nav_photo:
                EditUserDialog editDialog = new EditUserDialog();
                editDialog.show(getSupportFragmentManager(),"photoEdit");
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
