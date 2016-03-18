package com.prince.helpmeout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prince on 17/3/16.
 */
public class ContactListAdapter extends ArrayAdapter {

    Context con;
    List<String> name=new ArrayList<String>();
    List<String> number=new ArrayList<String>();
    public ContactListAdapter(Context con,List<String> name,List<String> number)
    {
        super(con,R.layout.customlist1,name);
        this.con=con;
        this.name=name;
        this.number=number;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            LayoutInflater myInflator=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=myInflator.inflate(R.layout.contactlist,null,true);
            convertView.setMinimumHeight(100);
        }
        TextView tv1=(TextView)convertView.findViewById(R.id.contactName);

        TextView tv2=(TextView)convertView.findViewById(R.id.contactNumber);

        tv1.setText(name.get(position));
        tv2.setText(number.get(position));

        return convertView;

    }
}
