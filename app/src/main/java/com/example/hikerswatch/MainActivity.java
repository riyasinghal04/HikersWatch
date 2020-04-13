package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            }

    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location){
        Log.i("locationInfo",location.toString());

        TextView latitudeTV=(TextView)findViewById(R.id.LatTextView);
        TextView longitudeTV=(TextView)findViewById(R.id.LongTextView);
        TextView accuracyTV=(TextView)findViewById(R.id.accuracyTextView);
        TextView altitudeTV=(TextView)findViewById(R.id.AltitudeTextView);
        TextView bearingTV=(TextView)findViewById(R.id.bearingTextView);
        TextView addressTV=(TextView)findViewById(R.id.addressTextView);

        latitudeTV.setText("Latitude : "+location.getLatitude());
        longitudeTV.setText("Longitude : "+location.getLongitude());
        accuracyTV.setText("Accuracy : "+location.getAccuracy());
        altitudeTV.setText("Altitude : "+location.getAltitude());
        bearingTV.setText(("Bearing : "+location.getBearing()));

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        try{
            String address="could not find address";

            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if(addresses!=null && addresses.size()>0){
                address="";
                 if(addresses.get(0).getSubThoroughfare()!=null){
                     address+=addresses.get(0).getSubThoroughfare()+" ";
                 }
                if(addresses.get(0).getThoroughfare()!=null){
                    address+=addresses.get(0).getThoroughfare()+"\n";
                }
                if(addresses.get(0).getLocality()!=null){
                    address+=addresses.get(0).getLocality()+"\n";
                }
                if(addresses.get(0).getPostalCode()!=null){
                    address+=addresses.get(0).getPostalCode()+"\n";
                }
                if(addresses.get(0).getCountryName()!=null){
                    address+=addresses.get(0).getCountryName()+"\n";
                }

            }
            addressTV.setText("Address :\n"+address);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation= locationManager.getLastKnownLocation((LocationManager.GPS_PROVIDER));
                if(lastKnownLocation!=null){
                    updateLocationInfo(lastKnownLocation);
                }

            }

       }



    }
}
