package com.prince.helpmeout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ViewContact extends AppCompatActivity {
    ListView list;
    ArrayList<DBAction> arryList= new ArrayList();
  List<String> Name=new ArrayList<String>();
    List<String> Number=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        list=(ListView)findViewById(R.id.listView1);
        DBAction object=new DBAction(ViewContact.this);
        arryList=object.readFromDB();

        for(DBAction db:arryList)
        {
            Name.add(db.cName);
            Number.add(db.cNumber);

        }

        MyAdapter1 ad=new MyAdapter1(ViewContact.this,Name,Number);
        ad.notifyDataSetChanged();
        list.setAdapter(ad);




    }
}
