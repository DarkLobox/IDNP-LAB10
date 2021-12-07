package com.example.idnp_lab10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private LocationListener locListenD;
    private String coordenadas = "";
    public TextView datos;
    public Location lastLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = findViewById(R.id.textViewCoordenadas);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (loc != null) {
            Toast.makeText(this, loc.getLatitude() + "\n " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            coordenadas += loc.getLatitude()+" "+loc.getLongitude()+" "+loc.getAltitude()+"\n";
            guardar();
            datos.setText(coordenadas);
            lastLocation = loc;
        }

        locListenD = new DispLocListener();

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListenD);
    }

    private void guardar(){
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("ruta01.txt",Activity.MODE_PRIVATE));
            archivo.write(coordenadas);
            archivo.flush();
            archivo.close();
        }catch (IOException e){

        }
    }
    private class DispLocListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null && location.distanceTo(lastLocation) > 100) {
                lastLocation = location;
                Toast.makeText(getApplicationContext(), location.getLatitude() + "\n " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                coordenadas += location.getLatitude()+" "+location.getLongitude()+" "+location.getAltitude()+"\n";
                guardar();
                datos.setText(coordenadas);
            }
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }
}

