package com.example.subham.ledgerplus;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class AddRecord extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        DbHelper rd = new DbHelper(getApplicationContext());
        String[] names = rd.returnParty();
        //Polulating account name
        AutoCompleteTextView t1 = (AutoCompleteTextView) findViewById(R.id.accountLedger);
        ArrayAdapter a1 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,names);
        t1.setThreshold(1);
        t1.setAdapter(a1);
        rd.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveRecord) {
            addRecord();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addRecord() {
        Switch s1 = (Switch) findViewById(R.id.amountType);
        DatePicker d1 = (DatePicker) findViewById(R.id.datePicker);
        final EditText e1 = (EditText) findViewById(R.id.amountLedger);
        final EditText desc = (EditText) findViewById(R.id.descLedger);
        DbHelper rd = new DbHelper(this);
        AutoCompleteTextView t1 = (AutoCompleteTextView) findViewById(R.id.accountLedger);
        String day = Integer.toString(d1.getDayOfMonth());
        String month = Integer.toString(d1.getMonth() + 1);
        String year = Integer.toString(d1.getYear());
        String date_curr = year+"-"+month+"-"+day;
        char SW;
        if(s1.isChecked()){
            Log.d("TRUE", "_VALUE SWITCH"); SW = 'C';}
        else {Log.d("FALSE","_VALUE SWITCH"); SW ='D';}
        if(e1.getText().toString().matches("")){
            Toast.makeText(this,"Enter valid amount",Toast.LENGTH_SHORT).show();
            e1.requestFocus();
        }else{
            String result = rd.insertPartyEntry(t1.getEditableText().toString(),e1.getText().toString(),desc.getText().toString(),SW,date_curr);
            if(result.matches("Entry name successfully")){
                t1.setText("");
                t1.requestFocus();
                e1.setText("");
                desc.setText("");
                Toast.makeText(getApplicationContext(),"Data saved",Toast.LENGTH_SHORT).show();
            }else{
                t1.setText("");
                t1.requestFocus();
                Toast.makeText(getApplicationContext(),"Not a valid account name, please check!!!",Toast.LENGTH_SHORT).show();
            }
        }


    }

}
