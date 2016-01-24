package com.example.subham.ledgerplus;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class ChangePassword extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        at.markushi.ui.CircleButton changePass = (at.markushi.ui.CircleButton) findViewById(R.id.circleChangePass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText oldPass = (EditText) findViewById(R.id.oldPass);
                final EditText newPass1 = (EditText) findViewById(R.id.newPass1);
                final EditText newPass2 = (EditText) findViewById(R.id.newPass2);

                if(newPass1.getText().toString().matches(newPass2.getText().toString())){
                    if(oldPass.getText().toString().matches("")){
                        Toast.makeText(getApplicationContext(),"Please enter valid old password",Toast.LENGTH_SHORT).show();
                    }else{
                        DbHelper rd = new DbHelper(getApplicationContext());
                        if(rd.changePassword(oldPass.getText().toString(), newPass1.getText().toString()).matches("OK")){
                            Toast.makeText(getApplicationContext(),"Password changed",Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(getApplicationContext(),MenuLedger.class);
                            rd.close();
                            startActivity(i1);
                        }else{
                            Toast.makeText(getApplicationContext(),"Please enter valid password",Toast.LENGTH_SHORT).show();
                            rd.close();
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"New password doesn't match",Toast.LENGTH_SHORT).show();
                    newPass1.setText("");
                    newPass2.setText("");
                    newPass1.requestFocus();
                }

            }
        });

    }


}
