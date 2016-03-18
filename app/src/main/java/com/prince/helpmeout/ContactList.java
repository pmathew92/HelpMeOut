package com.prince.helpmeout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity {
    private List<String> Name = new ArrayList<String>();
    private List<String> Number = new ArrayList<String>();
    private ListView contactList;
    DBAction db=new DBAction(ContactList.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list2);
        contactList = (ListView) findViewById(R.id.contactlistView);
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            Name.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

            Number.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }

        ContactListAdapter myadapter=new ContactListAdapter(ContactList.this,Name,Number);
        contactList.setAdapter(myadapter);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos=position;
                AlertDialog.Builder ad=new AlertDialog.Builder(ContactList.this);
                ad.setCancelable(false);
                ad.setMessage("Save this Contact?");
                ad.setTitle("Confirm")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(db.getDBCount()) {
                                    db.addContacts(Name.get(pos), Number.get(pos));
                                    Toast.makeText(ContactList.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(ContactList.this,"Sorry,Cannot Save More Than 5 Contacts",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                   AlertDialog a=ad.create();
                   a.show();

            }
        });

    }


}
