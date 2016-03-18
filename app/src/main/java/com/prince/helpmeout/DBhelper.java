package com.prince.helpmeout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prince on 26/2/16.
 */
public class DBhelper extends SQLiteOpenHelper{

  public  DBhelper(Context context)
  {
      super(context,"DB",null,1);

  }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mycontacts(Name varchar2(20),Number varchar2(15));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table mycontacs if exists;");
        onCreate(db);

    }
}
