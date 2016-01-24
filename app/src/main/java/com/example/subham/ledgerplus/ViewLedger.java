package com.example.subham.ledgerplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class ViewLedger extends ActionBarActivity {

    private String INTENT_VALUE = null;
    private String POS_VALUE = null;
    private Cursor c;
    MyAdapter adapter;
    private String COUNT_ITEMS = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ledger);

        Intent r = getIntent();
        Bundle extras = r.getExtras();
        if(extras!= null){
            String keyID = extras.getString("VIEW_ID");
            INTENT_VALUE = keyID;
            Log.d(keyID, "_VALUE INTENT");
            DbHelper rd = new DbHelper(this);
            c = rd.returnList(keyID);
            setTitle("Account of "+rd.returnUser(keyID));
            //rd.close();
            if(c!=null){
                COUNT_ITEMS = Integer.toString(c.getCount());
                TextView tcount = (TextView) findViewById(R.id.countListItems);
                tcount.setText("Showing "+COUNT_ITEMS+" items");
                ListView l1 = (ListView) findViewById(R.id.listViewLedgerItems);
                String[] fromColumns = {"_id","type","amount","date","desc"};
                Log.d("OK","_VALUE CUR");
                int[] toViews = {R.id.typeAmount,R.id.dateAmount,R.id.descAmount};
                adapter = new MyAdapter(getBaseContext(),R.layout.list_item_ledger,c,fromColumns,toViews,0);
                l1.setAdapter(adapter);
                l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.getCursor().moveToPosition(position);
                        POS_VALUE = adapter.getCursor().getString(0);
                        editElement(POS_VALUE);
                        //Toast.makeText(getApplicationContext(),adapter.getCursor().getString(0),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

    }

    private void editElement(final String pos_value) {
        Log.d("INSIDE edit Element","_value edit iem");
        DbHelper rd = new DbHelper(this);
        Cursor c22 = rd.getItem(pos_value);
        if(c22!=null){
            Log.d(Integer.toString(c22.getCount())+"->"+Integer.toString(c22.getColumnCount())+"->"+c22.getColumnName(1)+c22.getColumnName(2)+c22.getColumnName(3)+c22.getColumnName(4),"_value edit iem cur");
            AlertDialog.Builder editItem = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View editDialog = inflater.inflate(R.layout.edit_ledger_item,null);
            editItem.setView(editDialog);
            editItem.setTitle("View a record");
            c22.moveToFirst();
            Log.d(c22.getString(c22.getColumnIndex("desc"))+c22.getString(c22.getColumnIndex("amount"))+c22.getString(c22.getColumnIndex("type"))+c22.getString(c22.getColumnIndex("desc")),"_VALUE CUR EDIT");
            TextView t20 = (TextView) editDialog.findViewById(R.id.dateEditListItem);
            t20.setText("Date: "+c22.getString(c22.getColumnIndex("date")));
            TextView t21 = (TextView) editDialog.findViewById(R.id.amountEditListItem);
            t21.setText("Rs. "+c22.getString(c22.getColumnIndex("amount")));
            TextView t22 = (TextView) editDialog.findViewById(R.id.descEditListItem);
            t22.setText("Description: "+c22.getString(c22.getColumnIndex("desc")));
            TextView t23 = (TextView) editDialog.findViewById(R.id.typeEditListItem);
            if(c22.getString(c22.getColumnIndex("type")).matches("C")){
                t23.setText("Credit");
            }else{
                t23.setText("Debit");
            }
            editItem.setPositiveButton("EDIT",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*Intent editIntent = new Intent(ViewLedger.this,EditRecord.class);
                    editIntent.putExtra("PARTY_ID",INTENT_VALUE);
                    editIntent.putExtra("LEDGER_ID",pos_value);
                    startActivity(editIntent);*/
                    editRecord(pos_value);
                    dialog.dismiss();
                }
            });

            editItem.setNeutralButton("DELETE",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteItem(pos_value);
                    cursorItem();
                    dialog.dismiss();
                }
            });

            editItem.setNegativeButton("CLOSE",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            editItem.show();
            rd.close();
        } else{
            Toast.makeText(this,"Oops some error. Please contact Administrator",Toast.LENGTH_SHORT).show();
        }
    }

    private void editRecord(final String pos_value) {
        final DbHelper rd = new DbHelper(this);
        Cursor c2 = rd.getItem(pos_value);
        if(c2!=null){
            c2.moveToFirst();
            LayoutInflater inf = LayoutInflater.from(this);
            View edit = inf.inflate(R.layout.edit_individual_item,null);
            AlertDialog.Builder editItems = new AlertDialog.Builder(ViewLedger.this);
            editItems.setView(edit);
            editItems.setTitle("Edit a record");
            final EditText e1 = (EditText) edit.findViewById(R.id.amountEditItems);
            e1.setText(c2.getString(c2.getColumnIndex("amount")));
            final EditText e2 = (EditText) edit.findViewById(R.id.descEditItems);
            e2.setText(c2.getString(c2.getColumnIndex("desc")));

            editItems.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(e1.getText().toString().matches("")||e1.getText().toString().matches(" ")){
                        Toast.makeText(getApplicationContext(),"Enter valid amount",Toast.LENGTH_SHORT).show();
                    }else{
                        rd.updateItem(e1.getEditableText().toString(), e2.getEditableText().toString(),pos_value);
                        Toast.makeText(getApplicationContext(),"Data updated",Toast.LENGTH_SHORT).show();
                        cursorItem();
                        rd.close();
                    }
                    dialog.dismiss();
                }
            });

            editItems.setNegativeButton("CLOSE",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            editItems.show();
        }

    }

    private void cursorItem(){
        DbHelper rd1 = new DbHelper(ViewLedger.this);
        Cursor c1 = rd1.returnList(INTENT_VALUE);
        if(c1!=null){

            COUNT_ITEMS = Integer.toString(c1.getCount());
            TextView tcount2 = (TextView) findViewById(R.id.countListItems);
            tcount2.setText("Showing "+COUNT_ITEMS+" items");
            Log.d("NOT NULL","_value c1");
            //adapter.swapCursor(c1);
            ListView l1 = (ListView) findViewById(R.id.listViewLedgerItems);
            String[] fromColumns = {"_id","type","amount","date","desc"};
            Log.d("OK","_VALUE CUR");
            int[] toViews = {R.id.typeAmount,R.id.dateAmount,R.id.descAmount};
            adapter = new MyAdapter(getBaseContext(),R.layout.list_item_ledger,c1,fromColumns,toViews,0);
            adapter.TOTAL = 0;
            l1.setAdapter(adapter);
        }
    }

    private void deleteItem(String pos_value) {
        DbHelper rd = new DbHelper(this);
        String result = rd.delItem(pos_value);
        rd.close();
        if(result.matches("OK")){
            Toast.makeText(getApplicationContext(),"Record deleted",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Unable to delete. Please contact Administrator",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_ledger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addRecord) {
            addRecord();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addRecord() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View addDialog = inflater.inflate(R.layout.add_direct,null);
        AlertDialog.Builder insert =  new AlertDialog.Builder(ViewLedger.this);
        insert.setTitle("Add a record");
        insert.setView(addDialog);
        insert.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Switch s1 = (Switch) addDialog.findViewById(R.id.amountTypeDirect);
                DatePicker d1 = (DatePicker) addDialog.findViewById(R.id.datePickerDirect);
                final EditText e1 = (EditText) addDialog.findViewById(R.id.addAmountDirect);
                final EditText desc = (EditText) addDialog.findViewById(R.id.descLedgerDirect);

                Log.d(e1.getText().toString()+desc.getText().toString(),"_value test");
                DbHelper rd = new DbHelper(ViewLedger.this);
                String day = Integer.toString(d1.getDayOfMonth());
                String month = Integer.toString(d1.getMonth() + 1);
                String year = Integer.toString(d1.getYear());
                String date_curr = year+"-"+month+"-"+day;
                char SW;
                if(s1.isChecked()){
                    Log.d("TRUE", "_VALUE SWITCH"); SW = 'C';}
                else {Log.d("FALSE","_VALUE SWITCH"); SW ='D';}
                if(e1.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Enter valid amount and try again", Toast.LENGTH_SHORT).show();
                    //e1.requestFocus();
                }else{
                    rd.insertPartyEntryDirect(INTENT_VALUE, e1.getText().toString(), desc.getText().toString(), SW, date_curr);
                    Toast.makeText(getApplicationContext(),"Data saved",Toast.LENGTH_SHORT).show();
                    DbHelper rd1 = new DbHelper(ViewLedger.this);
                    Cursor c1 = rd1.returnList(INTENT_VALUE);
                    if(c1!=null){
                        COUNT_ITEMS = Integer.toString(c1.getCount());
                        TextView tcount2 = (TextView) findViewById(R.id.countListItems);
                        tcount2.setText("Showing "+COUNT_ITEMS+" items");
                        Log.d("NOT NULL","_value c1");
                        //adapter.swapCursor(c1);
                        ListView l1 = (ListView) findViewById(R.id.listViewLedgerItems);
                        String[] fromColumns = {"_id","type","amount","date","desc"};
                        Log.d("OK","_VALUE CUR");
                        int[] toViews = {R.id.typeAmount,R.id.dateAmount,R.id.descAmount};
                        adapter = new MyAdapter(getBaseContext(),R.layout.list_item_ledger,c1,fromColumns,toViews,0);
                        l1.setAdapter(adapter);
                    }
                    //rd1.close();

                }
                dialog.dismiss();
               // rd1.close();




            }
        });
        insert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        insert.show();
    }



}
