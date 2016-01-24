package com.example.subham.ledgerplus;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LEDGER";


    public DbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE IF NOT EXISTS user(id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(20), pass VARCHAR(20), auth INTEGER default 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS party(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100) UNIQUE, phone VARCHAR(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS ledger(id INTEGER PRIMARY KEY AUTOINCREMENT, party_id INTEGER, type CHAR(1), amount INTEGER(10) default 0, date DATE, desc VARCHAR(200))");
        db.execSQL("INSERT into user(username,pass) VALUES ('admin','1234')");
        Log.d("DATABASE CREATED", "RAMDOOT");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS party");
        db.execSQL("DROP TABLE IF EXISTS ledger");
        onCreate(db);
    }
    //Get the password for the username
    public String getPass(String user){
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor c_get = db.rawQuery("SELECT pass from user WHERE username = '"+user+"'",null);
            Log.d("SELECT pass from user WHERE username = '"+user+"'","RETURN_VALUE");
            //db.close();
            if(c_get.moveToFirst()){
                Log.d(c_get.getString(0),"RETURN_VALUE");
                return c_get.getString(0);
            }else {
                Log.d("NULL RETURNED","RETURN_VALUE");
                return "";
            }

        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    //Authenticate and SET the session for Users
    public void setAuth(String user){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE user SET auth = 1 where username = '"+user+"'");
            Log.d("SESSION AUTHENTICATED","RAMDOOT");
            db.close();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public String getAuth(){
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor c_get = db.rawQuery("SELECT username from user WHERE auth = 1",null);
            if(c_get.moveToFirst()){
                return c_get.getString(0);
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void removeAuth(){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE user SET auth = 0");
            Log.d("SESSION REMOVED","RAMDOOT");
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean insertParty(String name, String phone){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("INSERT into party(name,phone) VALUES ('"+name+"','"+phone+"')");
            Log.d("INSERT into party(name,phone) VALUES ('"+name+"','"+phone+"')","_VALUE INSERTED");
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String[] returnParty(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c_get = db.rawQuery("SELECT name from party",null);
        int i = c_get.getCount();
        int j =0;
        String [] name = new String[i];
        if(c_get.moveToFirst()){
            do{
                name[j] = c_get.getString(0);
                j++;
            }while (c_get.moveToNext());
        }
        c_get.close();
        //Log.d(name,"_VALUE NAME ARRAY");
        return name;

    }

    public String insertPartyEntry(String name, String amount, String desc, char s,String date_c){
        String id;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id from party where name = '"+name+"'",null);
        if(c.moveToFirst()){
            id = c.getString(0);
            db.execSQL("INSERT into ledger(party_id,type,amount,desc,date) VALUES ('"+id+"','"+s+"','"+amount+"','"+desc+"','"+date_c+"')");
            Log.d("INSERT into ledger(party_id,type,amount,desc,date) VALUES ('"+id+"','"+s+"','"+amount+"','"+desc+"','"+date_c+"')","_VALUE PARTY ENTRY");
            db.close();
            return "Entry name successfully";
        }else{
            db.close();
            return "Party name not Valid";
        }

    }

    public void insertPartyEntryDirect(String id, String amount, String desc, char s,String date_c){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT into ledger(party_id,type,amount,desc,date) VALUES ('"+id+"','"+s+"','"+amount+"','"+desc+"','"+date_c+"')");
        Log.d("INSERT into ledger(party_id,type,amount,desc,date) VALUES ('"+id+"','"+s+"','"+amount+"','"+desc+"','"+date_c+"')","_VALUE PARTY ENTRY");
        db.close();
    }

    public Cursor returnList(){
        try{
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery("SELECT ledger.ID as _id, type, amount, name, date, desc from ledger,party where ledger.party_id = party.id order by name",null);
            return c;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    public Cursor returnList(String new_id){
        try{
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery("SELECT ledger.ID as _id, type, amount, date, desc from ledger where ledger.party_id = '"+new_id+"' order by date(date) desc",null);
            return c;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    public String returnUser(String keyID){
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT name from party where ID = "+keyID, null);
            if(c.moveToFirst()){
                return c.getString(0);
            }else {
                return "View Ledger";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "View Ledger";
        }

    }
    public String changePassword(String old, String newPass){
        try{
            if(old.matches("") || newPass.matches("")){
                return "KO";
            }else{
                SQLiteDatabase db = getWritableDatabase();
                Cursor c = db.rawQuery("SELECT pass from user where username = 'admin'",null);
                if(c.moveToFirst()){
                    if(c.getString(0).matches(old)){
                        db.execSQL("UPDATE user SET pass = '"+newPass+"' where username = 'admin'");
                        Log.d("UPDATE user SET pass = '"+newPass+"' where username = 'admin'","_VALUE PASSWORD");
                        return "OK";
                    }else{
                        return "KO";
                    }
                }else{
                    return "KO";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return "KO";
        }
    }

    public Cursor returnUserList(){
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT ID as _id, name , phone from PARTY order by name",null);
            return c;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void removeUser(int del){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE from ledger where party_id ='"+del+"'");
            db.execSQL("DELETE from party where ID = '"+del+"'");
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Cursor getItem(String position){
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT ID as _id, date, desc, amount, type from ledger where ID = '"+position+"'",null);
            return c;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String delItem(String position){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE from ledger where ID = '"+position+"'");
            return "OK";
        }catch(Exception e){
            e.printStackTrace();
            return "KO";
        }

    }

    public void updateItem(String vamount,String vdesc,String pos){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE ledger SET amount = '"+vamount+"',desc = '"+vdesc+"' where id = '"+pos+"'");
            Log.d("UPDATE ledger SET amount = '"+vamount+"',desc = '"+vdesc+"' where id = '"+pos+"'","_value update");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

   /* public void checkUser(String user){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ");
    }*/

}


