package com.example.subham.ledgerplus;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Subham on 26-11-2015.
 */
public class MyAdapter extends SimpleCursorAdapter {

    private Context context;
    private Cursor c;
    public int TOTAL = 0;

    public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View iview, ViewGroup parent ){
        View v = iview;

        if(v==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_ledger,null);
        }
        this.c.moveToPosition(pos);

        int value_id = this.c.getInt(0);
        String type = this.c.getString(1);
        int amount = this.c.getInt(2);
        String c_date = this.c.getString(3);
        String desc = this.c.getString(4);
        String SW = "Credit";
        ImageView i1 = (ImageView) v.findViewById(R.id.typeImage);
        TextView t3 = (TextView) v.findViewById(R.id.typeAmount);
        if(type.matches("D")){
            SW = "Debit";
            TOTAL = TOTAL - amount;
            i1.setImageResource(R.drawable.ic_plus_box_white_36dp);
            i1.setColorFilter(Color.parseColor("#009900"));
        }else{
            TOTAL = TOTAL + amount;
            t3.setTextColor(Color.RED);
            i1.setImageResource(R.drawable.ic_minus_box_white_36dp);
            i1.setColorFilter(Color.RED);
        }



        TextView t1 = (TextView) v.findViewById(R.id.dateAmount);
        t1.setText(c_date);
        TextView t2 = (TextView) v.findViewById(R.id.idAmount);
        t2.setText(Integer.toString(value_id));
        t3.setText(SW);
        TextView t4 = (TextView) v.findViewById(R.id.subAmount);
        t4.setText("Rs "+Integer.toString(amount));
        TextView t5 = (TextView) v.findViewById(R.id.descAmount);
        t5.setText(desc);
        TextView t6 = (TextView) v.findViewById(R.id.totalAmount);
        if(TOTAL > 0){
            t6.setText("Rs "+Integer.toString(TOTAL)+ " CR");
        }else if (TOTAL == 0){
            String s = Integer.toString(TOTAL);
            t6.setText("Rs "+ TOTAL);
        }else{
            String s = Integer.toString(TOTAL);
            t6.setText("Rs "+s.substring(1)+ " DR");
        }



        return v;
    }

}
