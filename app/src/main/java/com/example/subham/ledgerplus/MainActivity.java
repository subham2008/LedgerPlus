package com.example.subham.ledgerplus;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating a database for Ledger
        createDB();
        at.markushi.ui.CircleButton c1 = (at.markushi.ui.CircleButton) findViewById(R.id.circle1);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(),MenuLedger.class);
                startActivity(i1);
            }
        });

    }

    private void createDB() {
        DbHelper helper = new DbHelper(this);
        helper.getWritableDatabase();
    }






}
