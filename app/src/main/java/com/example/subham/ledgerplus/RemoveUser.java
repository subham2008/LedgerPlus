package com.example.subham.ledgerplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class RemoveUser extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);
        ListView l1 = (ListView) findViewById(R.id.listUser);
        DbHelper rd = new DbHelper(this);
        Cursor c = rd.returnUserList();
        if(c != null){
            String[] fromColumns = {"_id","name","phone"};
            int[] toViews = {R.id.idUserRemove,R.id.userListLedger,R.id.phoneUserListLedger};
            final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getBaseContext(),R.layout.list_user_layout,c,fromColumns,toViews,0);
            l1.setAdapter(adapter);
            rd.close();

            l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   try{
                       adapter.getCursor().moveToPosition(position);
                       final int delId = adapter.getCursor().getInt(0);

                       AlertDialog.Builder alert = new AlertDialog.Builder(
                               RemoveUser.this);
                       alert.setTitle("Alert!!");
                       alert.setMessage("Are you sure to delete record");
                       alert.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               DbHelper rd = new DbHelper(getApplicationContext());
                               rd.removeUser(delId);
                               dialog.dismiss();
                               Toast.makeText(getApplicationContext(),"Account and ledger deleted",Toast.LENGTH_SHORT).show();
                               Intent i2 = new Intent(getApplicationContext(),MenuLedger.class);
                               startActivity(i2);
                           }
                       });
                       alert.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       });
                       alert.show();
                   }catch (Exception e){
                        e.printStackTrace();
                   }


                }
            });
        }else{
            Toast.makeText(this,"No accounts registered",Toast.LENGTH_SHORT).show();
            rd.close();
        }




    }
}
