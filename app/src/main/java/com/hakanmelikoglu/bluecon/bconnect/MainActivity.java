package com.hakanmelikoglu.bluecon.bconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String VT2 = "uuid";
    public String[] sutunlar2 = {"secure", "insecure"};
    public ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(getResources().getString(R.string.bt_connect));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT); // açılışta drawerın açık kalmasını sağlar


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation!");
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(getTitle().toString());
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(0).setChecked(true); // ilk elemanı seçili yapar
        //navigationView.setKeepScreenOn(true);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);


    }

    private void vtGoster(){
        Veritabani veritabani = new Veritabani(this);
        try {

            // SQL verileri alır
            SQLiteDatabase db1 = veritabani.getReadableDatabase();
            Cursor okunanlar = db1.query(VT2, sutunlar2, null, null, null, null, null);
            while (okunanlar.moveToNext()) {
                String secure = okunanlar.getString(okunanlar.getColumnIndex("secure"));
                String insecure = okunanlar.getString(okunanlar.getColumnIndex("insecure"));

            }
        }finally {
            veritabani.close();
        }
    }

    @Override
    public void onBackPressed() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.bt_connect));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        /*
        if (id == R.id.action_settings) {
            return true;
        }
*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homePage) { // home

            fragment = new BluetoothChatFragment();
            fab.setVisibility(View.VISIBLE);


        } else if (id == R.id.gamePadPage) { // gamepad
            //fragment = new GamePad();
            //Intent intent = new Intent(this,TamekranActivity.class);
            //startActivity(intent);
            fragment = new GameSelect();
            fab.setVisibility(View.INVISIBLE);

        } else if (id == R.id.keyPadPage) { // keypad
            fragment = new KeyPad();
            fab.setVisibility(View.INVISIBLE);

        }
        else if (id == R.id.chatPage) { // customize

            fragment = new Chat();
            fab.setVisibility(View.INVISIBLE);

        }

        else if (id == R.id.customPadPage) { // customize

            fragment = new CustomizePad();
            fab.setVisibility(View.INVISIBLE);

        }
        else if (id == R.id.person_id){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_share) { // share

            fab.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_send) { // send

            fab.setVisibility(View.INVISIBLE);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        //transaction.setCustomAnimations(R.anim.enter, R.anim.exit);

        transaction.add(R.id.sample_content_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        hideSoftKeyboard(MainActivity.this);

        return true;
    }


}
