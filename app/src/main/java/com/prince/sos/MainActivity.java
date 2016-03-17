package com.prince.sos;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {
    private String LOG_STRING="Connection";
    private  GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location mlocation;
    LocationManager locationManager;
    double Latitude,Longitude;
    private Button messageSend;
    Intent in;
    boolean isGpsOn;
    DBAction dbcheck=new DBAction(MainActivity.this);
    private String message="Hey I am in trouble and need help ASAP.I am at this location at present:\n";


    public void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    public void onDestroy()
    {
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageSend=(Button)findViewById(R.id.sendMessage);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //Checking for Google Play Services
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(status==ConnectionResult.SUCCESS)
            buildGoogleApiClient();
        else{
            Toast.makeText(this,"Google Play Services Not Available",Toast.LENGTH_SHORT).show();
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }

        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGpsOn=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (dbcheck.isDBEmpty())
                    Toast.makeText(MainActivity.this, "No Contacts Saved", Toast.LENGTH_SHORT).show();
                else if(!isGpsOn)
                {
                    Toast.makeText(getApplicationContext(), "GPS is off!!Please turn it On", Toast.LENGTH_SHORT).show();
                    Intent GPSIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(GPSIntent);
                }
                else {

                    AsyncTry obj = new AsyncTry(getBaseContext());
                    obj.execute(Latitude,Longitude);
                }

            }
        });


    }



    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }




    public void onClick(View v)
    {
        if(dbcheck.getDBCount())
       {
        in = new Intent(this, contacts.class);
        startActivity(in);
       }
        else
            Toast.makeText(this,"Cannot Save More Than 5 contacts",Toast.LENGTH_SHORT).show();
    }


    public void onShow(View v)
    {
        if(dbcheck.isDBEmpty())
            Toast.makeText(MainActivity.this,"No Contacts Saved",Toast.LENGTH_SHORT).show();
        else
        {
            in = new Intent(this, ViewContact.class);
            startActivity(in);
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest= LocationRequest.create();
       mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        mlocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

          if(mlocation !=null) {
              Latitude = mlocation.getLatitude();
              Longitude = mlocation.getLongitude();
          }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_STRING,"Connection Suspended");


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_STRING,"Connection Failed");

    }

    @Override
    public void onLocationChanged(Location location) {
       // mlocation=location;
        if(location !=null) {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
        }


    }


    public void sendMessage(String add)
    {
        add=message+"\n"+add;
        List<DBAction> arrylist=new ArrayList<DBAction>();
        arrylist=dbcheck.readFromDB();
        SmsManager sm = SmsManager.getDefault();

        for(DBAction db:arrylist)
            sm.sendTextMessage(db.cNumber, null, add, null, null);

        Toast.makeText(getApplicationContext(),add,Toast.LENGTH_SHORT).show();
    }


    public class AsyncTry extends AsyncTask<Double, Integer, String> {
        Context context;
        String add;
        ProgressDialog pd;


        public AsyncTry(Context context) {

            this.context = context;
        }

        @Override
        protected String doInBackground(Double... params)
        {
            StringBuilder str = new StringBuilder();

            try {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                if (geocoder.isPresent()) {
                    List<Address> addresses = geocoder.getFromLocation(params[0],params[1], 1);
                    if (addresses != null)
                    {
                        Address fetchedAddress = addresses.get(0);
                        for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++)
                        {
                            str.append(fetchedAddress.getAddressLine(i)).append("\n");
                        }

                    }

                }
                add=str.toString();

            } catch (Exception e) {
                Log.d("Exception:", e.toString());
            }
            finally {
                if(add!=null) {
                    add = "Latitude:" + params[0]+ "\nLongitude:" + params[1] + "\n" + add;
                }
                else {
                    add = "Latitude:" + params[0] + "\nLongitude:" + params[1] + "\nCant Find Address for this location";
                }
            }

            return add;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(MainActivity.this);
            pd.setMessage("Getting Location...");
            pd.setCancelable(false);
            pd.show();
        }


        protected void onPostExecute(String result) {
            if (pd.isShowing())
                pd.dismiss();

            if (result != null) {

              sendMessage(result);

            }

        }
    }


}
