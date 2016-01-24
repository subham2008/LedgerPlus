package com.example.subham.ledgerplus;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //authCheck();
        loginActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //authCheck();
    }

    private void loginActivity() {
        final DbHelper rd = new DbHelper(getApplicationContext());
        final EditText t_pass = (EditText) findViewById(R.id.passLogin);
        t_pass.requestFocus();
        Button butnSearchUser = (Button) findViewById(R.id.enterLogin);
        butnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText t_user = (EditText) findViewById(R.id.userLogin);
                TextView t_result = (TextView) findViewById(R.id.resultLogin);
                Log.d(t_user.getText().toString(), "SEND_VALUE");
                Log.d(t_pass.getText().toString(), "SEND_VALUE");
                Log.d(rd.getPass(t_user.getText().toString()), "RETURNED_VALUE");
                if(t_pass.getEditableText().toString().matches(rd.getPass(t_user.getText().toString()))){
                    //Toast.makeText(this,"Successful Login",Toast.LENGTH_SHORT).show();
                    //t_result.setText("Login Successful");
                    rd.setAuth(t_user.getText().toString());
                    Intent i_menuledger = new Intent(getApplicationContext(),MenuLedger.class);
                    rd.close();
                    startActivity(i_menuledger);
                }else{
                    t_result.setText("Oops wrong secret word !!!");
                    t_user.setText("admin");
                    t_pass.setFocusable(true);
                    t_pass.setText("");
                    rd.close();
                }

            }
        });


    }



    private void authCheck() {
        DbHelper rd = new DbHelper(getApplicationContext());
        Log.d(rd.getAuth(), "_VALUE");
        if (rd.getAuth() != null) {
            Intent i1 = new Intent(this, MenuLedger.class);
            startActivity(i1);
            rd.close();
        }
    }


}
