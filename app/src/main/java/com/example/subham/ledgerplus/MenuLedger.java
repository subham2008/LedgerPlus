package com.example.subham.ledgerplus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MenuLedger extends ActionBarActivity {

    private int COUNT_ITEMS = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ledger);
        authCheck();

        ListView l1 = (ListView) findViewById(R.id.listViewLedger);
        DbHelper rd = new DbHelper(this);
        Cursor c = rd.returnUserList();

        if(c != null) {
            COUNT_ITEMS = c.getCount();
            TextView t20 = (TextView) findViewById(R.id.countUserLedger);
            if(COUNT_ITEMS == 0){
                t20.setText("There is no account. Please add an account");
            }else{
                t20.setText("Showing "+Integer.toString(COUNT_ITEMS)+" accounts below");
            }
            String[] fromColumns = {"_id","name","phone"};
            int[] toViews = {R.id.idUserRemoveList,R.id.userListLedgerList,R.id.phoneUserListLedgerList};
            final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getBaseContext(),R.layout.list_user_ledger,c,fromColumns,toViews,0);
            l1.setAdapter(adapter);
            rd.close();
            l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        Log.d(Integer.toString(position),"_value pos");
                        adapter.getCursor().moveToPosition(position);
                        Log.d(adapter.getCursor().getString(0),"_value cursor");
                        String value = adapter.getCursor().getString(0);
                        Intent i_ViewLedger = new Intent(MenuLedger.this,ViewLedger.class);
                        i_ViewLedger.putExtra("VIEW_ID",value);
                        startActivity(i_ViewLedger);

                    }catch(Exception e){
                        e.printStackTrace();
                    }




                }
            });

        }

        at.markushi.ui.CircleButton addLedgerItem = (at.markushi.ui.CircleButton) findViewById(R.id.addRecordLedger);
        addLedgerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(getApplicationContext(),AddRecord.class);
                startActivity(i4);
            }
        });

        at.markushi.ui.CircleButton addUserItem = (at.markushi.ui.CircleButton) findViewById(R.id.addUserLedger2);
        addUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(getApplicationContext(),AddUser.class);
                startActivity(i5);
            }
        });



    }



    @Override
    protected void onResume() {
        super.onResume();
        authCheck();
    }

    private void authCheck() {
        DbHelper rd = new DbHelper(getApplicationContext());
        Log.d(rd.getAuth(), "_VALUE");
        if (rd.getAuth()== null) {
            Intent i1 = new Intent(this, Login.class);
            startActivity(i1);
            rd.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_ledger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(item.getTitle().toString()){
            case "Logout":
                DbHelper rd = new DbHelper(this);
                rd.removeAuth();
                Intent i2 = new Intent(getApplicationContext(),Login.class);
                startActivity(i2);
                break;
            case "Change Password":
                Intent i5 = new Intent(getApplicationContext(),ChangePassword.class);
                startActivity(i5);
                break;
            case "Add Account":
                Intent i3 = new Intent(getApplicationContext(),AddUser.class);
                startActivity(i3);
                break;
            case "Backup":
                Intent i4 = new Intent(getApplicationContext(),BackupActivity.class);
                startActivity(i4);
                break;
            case "Remove Account":
                Intent i6 = new Intent(getApplicationContext(),RemoveUser.class);
                startActivity(i6);
                break;
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
