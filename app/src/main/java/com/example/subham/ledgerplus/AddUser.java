package com.example.subham.ledgerplus;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddUser extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        at.markushi.ui.CircleButton createParty = (at.markushi.ui.CircleButton) findViewById(R.id.circleAddUser);
        final EditText ename = (EditText) findViewById(R.id.accountUserLedger);
        final EditText ephone = (EditText) findViewById(R.id.phoneUserLedger);
        createParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if condition to restrict empty response//
                if((ename.getText().toString().isEmpty())){
                    Toast.makeText(getApplication(), "Please enter valid name and number", Toast.LENGTH_SHORT).show();
                }else{
                    DbHelper rd = new DbHelper(getApplicationContext());
                    boolean a = rd.insertParty(ename.getText().toString().toUpperCase(),ephone.getText().toString());
                    if(a){
                        Toast.makeText(getApplication(),"Account created",Toast.LENGTH_SHORT).show();
                        Intent i2 = new Intent(getApplication(),MenuLedger.class);
                        startActivity(i2);
                    }else{
                        Toast.makeText(getApplication(),"Account exists !!!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



}
