package com.prince.sos;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.res.TypedArrayUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by prince on 1/3/16.
 */
public class MyAdapter1 extends ArrayAdapter<String> {
    Context con;
   List<String> name=new ArrayList<String>();
    List<String> number=new ArrayList<String>();
    public MyAdapter1(Context con,List<String> name,List<String> number)
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
           convertView=myInflator.inflate(R.layout.customlist1,null,true);
           convertView.setMinimumHeight(200);
       }

           TextView tv1=(TextView)convertView.findViewById(R.id.dispName);

           TextView tv2=(TextView)convertView.findViewById(R.id.dispNumber);
           ImageButton delete=(ImageButton)convertView.findViewById(R.id.delButton);

           delete.setTag(position);

           final int pos=position;
           tv1.setText(name.get(position));
           tv2.setText(number.get(position));
           delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {


                   AlertDialog.Builder al = new AlertDialog.Builder(con);
                   al.setCancelable(false);
                   al.setMessage("Do You Want To Delete This Contact?");
                   al.setTitle("Delete")
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                               @Override
                               public void onClick(DialogInterface dialog, int id) {



                                   DBAction obj = new DBAction(con);
                                   obj.deleteFromDB(number.get(pos));

                                   name.remove(pos);
                                   number.remove(pos);
                                   MyAdapter1.this.notifyDataSetChanged();


                               }
                           })
                           .setNegativeButton("No", new DialogInterface.OnClickListener() {

                               @Override
                               public void onClick(DialogInterface dialog, int id) {

                                   dialog.cancel();
                               }
                           });

                   AlertDialog a = al.create();
                   a.show();


               }
           });



        return convertView;
       }

}
