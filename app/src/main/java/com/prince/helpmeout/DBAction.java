package com.prince.helpmeout;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



import java.util.ArrayList;

/**
 * Created by prince on 26/2/16.
 */
public class DBAction {
    String cName,cNumber;

    Context ctx;
    DBAction(Context context)

    {
        ctx=context;
    }


    public boolean getDBCount()
    {   boolean canSave=true;
        SQLiteDatabase db=null;
        Cursor cb=null;
        DBhelper helper=new DBhelper(ctx);
        String query="select * from mycontacts";
        try{
            db=helper.getReadableDatabase();
            cb=db.rawQuery(query,null);
            if(cb.getCount()==5) {
                canSave= false;
            }


        }catch(Exception e)
        {
            Log.d("DataDelete",e.toString());
        }finally {
            if (db != null) {
                db.close();
            }
            if (helper != null) {
                helper.close();
            }

        }
        return canSave;
    }





    public boolean isDBEmpty()
    {   boolean isEmpty=false;
        SQLiteDatabase db=null;
        Cursor cb=null;
        DBhelper helper=new DBhelper(ctx);
        String query="select * from mycontacts";
        try{
            db=helper.getReadableDatabase();
            cb=db.rawQuery(query,null);
            if(cb.getCount()==0) {
                isEmpty = true;
            }


        }catch(Exception e)
        {
            Log.d("DataDelete",e.toString());
        }finally {
            if (db != null) {
                db.close();
            }
            if (helper != null) {
                helper.close();
            }

        }
        return isEmpty;
    }







    public long addContacts(String name,String number)
    {   long flag=-1;
        SQLiteDatabase db = null;
        DBhelper helper=new DBhelper(this.ctx);
        try {
            db=helper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("Name",name);
            values.put("Number", number);
          flag=db.insert("mycontacts", null, values);

        }
        catch (Exception e) {
            Log.d("Database", e.toString());
        }
        finally
        {
            if(db!=null)
            {
                db.close();
            }
            if(helper!=null) {
                helper.close();
            }
        }

        return flag;
    }




    public void deleteFromDB(String number)
    {
        SQLiteDatabase db=null;
        DBhelper helper=new DBhelper(ctx);
        try{
            db=helper.getWritableDatabase();
            db.delete("mycontacts","Number=?",new String[]{number});



        }catch(Exception e)
        {
            Log.d("DataDelete",e.toString());
        }finally {
            if (db != null) {
                db.close();
            }
            if (helper != null) {
                helper.close();
            }

        }
    }



    public ArrayList<DBAction> readFromDB() {

        ArrayList<DBAction> object=new ArrayList<>();
        SQLiteDatabase db = null;
        DBhelper helper=new DBhelper(ctx);
        Cursor cb=null;

        String query = "select * from mycontacts";
        try {
            db = helper.getReadableDatabase();
            cb = db.rawQuery(query, null);

            if(cb!=null)
            {
                while(cb.moveToNext())
                {

                    DBAction temp=new DBAction(ctx);
                    temp.cName=cb.getString(0);
                    temp.cNumber= cb.getString(1);
                   object.add(temp);
                }
            }

        } catch (Exception e) {
            Log.d("Database", e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
            if (helper != null) {
                helper.close();
            }
        }

        return object;

    }




}
