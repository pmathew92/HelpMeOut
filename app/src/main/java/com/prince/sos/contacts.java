package com.prince.sos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class contacts extends AppCompatActivity {
    private EditText inputNumber,inputName;
    private Button saveContact,gotoContacts;
    DBAction db=new DBAction(contacts.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        inputNumber=(EditText)findViewById(R.id.inputNumber);
        inputName=(EditText)findViewById(R.id.inputName);
        saveContact=(Button)findViewById(R.id.saveContact);
        gotoContacts=(Button)findViewById(R.id.viewContactList);
        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=inputName.getText().toString();
                String number=inputNumber.getText().toString();
                long cn=db.addContacts(name,number);
                if(cn>=-1)
                {
                    Toast.makeText(contacts.this,"Contact saved",Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(contacts.this,MainActivity.class);
                    startActivity(in);
                    finish();
                }


            }
        });

        gotoContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(contacts.this,ContactList.class);
                startActivity(in);

            }
        });

    }

}
