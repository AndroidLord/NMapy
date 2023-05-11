package com.example.mapy;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.util.DataUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapy.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Marker marker;

    private Spinner spinner;

    private String spinnerSelectedItem;

    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    private static final int REQUEST_CODE_GPS = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);




        // Setting Up Spinner
        settingUpSpinner();


        checkGpsEnabled();

        // Setting up Map to get Current Location

    binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {


            checkGpsEnabled();
            binding.refreshLayout.setRefreshing(false);
        }
    });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();
                spinnerSelectedItem = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(binding.poiNameET.getText())) {

                    // POI Name is there

                    if (!TextUtils.isEmpty(spinnerSelectedItem)) {

                        // For Address
                        if (!TextUtils.isEmpty(binding.addressET.getText())) {

                            if (!binding.remarkET.getText().toString().isEmpty()) {

                                if (!binding.latitude.getText().toString().isEmpty() &&
                                        !binding.longitude.getText().toString().isEmpty()) {

                                    // Starting Activity
                                    Toast.makeText(MapsActivity.this, "POI Name: " + binding.poiNameET.getText().toString()
                                                    + "\n Category: " + spinnerSelectedItem +
                                                    "\n Address: " + binding.addressET.getText().toString() +
                                                    "\n Lat: " + binding.latitude.getText().toString() +
                                                    "\n Long: " + binding.longitude.getText().toString() +
                                                    "\n Remarks: " + binding.remarkET.getText().toString()
                                            , Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(MapsActivity.this, "Please Open Your GPS", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                binding.remarkET.setError("Enter Remarks");
                                binding.remarkET.requestFocus();
                                //Toast.makeText(MapsActivity.this, "Please Enter Remarks", Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            binding.addressET.setError("Enter Address");
                            binding.addressET.requestFocus();
                            Toast.makeText(MapsActivity.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // for category
                        binding.spinner.requestFocus();
                        binding.spinner.setPrompt("Select A Category");
                        Toast.makeText(MapsActivity.this, "Please Select A Category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //binding.poiNameET.requestFocus();
                    binding.poiNameET.setError("Enter Your Name");
                    binding.poiNameET.requestFocus();
                    Toast.makeText(MapsActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void checkGpsEnabled() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // GPS is disabled, request the user to turn it on

                showGPSDialog();


            }
            else
                getLastLocation();

        }
        else{
            askPermission();
        }




    }

    private void showGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enable GPS to use this app")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(gpsIntent, REQUEST_CODE_GPS);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void settingUpSpinner() {

        spinner = findViewById(R.id.spinner);

        String[] category = {"Mandir", "Majid", "Petrol Pump"};

        // Initialize the Spinner with an adapter

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, category);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // GPS is enabled, request the user to turn it on

                Task<Location> task = fusedLocationProviderClient.getLastLocation();


                Log.d("gps", "Outside the task Listener");

                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        Log.d("gps", "Inside the task Listener");
                        Log.d("gps", "Location is " + location);

                        if(location!=null){

                            Log.d("gps", "onSuccess: Location isn't null");

                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true).title("Current Location");



                            if (mMap != null) {

                                Log.d("gps", "onSuccess: mMap isn't null");

                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                                binding.latitude.setText(String.valueOf(location.getLatitude()));
                                binding.longitude.setText(String.valueOf(location.getLongitude()));

                                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDrag(@NonNull Marker marker) {

                                    }

                                    @Override
                                    public void onMarkerDragEnd(@NonNull Marker marker) {


                                        binding.latitude.setText(String.valueOf(marker.getPosition().latitude));
                                        binding.longitude.setText(String.valueOf(marker.getPosition().longitude));

                                    }

                                    @Override
                                    public void onMarkerDragStart(@NonNull Marker marker) {

                                    }
                                });
                            }
                            else{
                                Log.d("gps", "onSuccess: mMap is Null");
                            }
                        }
                        else{
                            // Location is null


                        }

                    }
                });

            }
            else{
                showGPSDialog();
            }



        } else {

            askPermission();


        }


    }



    private void askPermission() {

        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_CODE) {

            Log.d("gps", "Outside onRequestPermissionsResult of Location Permission: ");

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d("gps", "Inside onRequestPermissionsResult of Location Permission: ");
                checkGpsEnabled();

            } else {

                Toast.makeText(MapsActivity.this, "Please Provide The Required Permission", Toast.LENGTH_SHORT).show();

            }


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GPS) {

            Log.d("gps", "Outside onActivityResult of GPS Permission: ");

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // GPS is now enabled, continue with your app logic
                Log.d("gps", "Inside onActivityResult of GPS Permission: ");
                Toast.makeText(this, "GPS has turned on", Toast.LENGTH_SHORT).show();
                Log.d("gps", "Going to get the last location");
                getLastLocation();
            } else {
                // GPS is still not enabled, show a message or take appropriate action
                Log.d("gps", "Please Turn on GPS, Inside of onActivityResult of GPS Permission: ");
                Toast.makeText(this, "Please Turn on Your GPS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("gps", " Inside onMapReady: and mMap is Empty");

        mMap = googleMap;

        Log.d("gps", " Inside onMapReady: and mMap has value");
//        LatLng lucknow = new LatLng(26.8467, 80.9462);
//        marker = mMap.addMarker(new MarkerOptions().position(lucknow).draggable(true).title("Marker 1"));


        //mMap.setOnMarkerClickListener(this);
        //mMap.setOnMarkerDragListener(this);

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(lucknow));
        //  Toast.makeText(this, "Zoom level " + mMap.getMaxZoomLevel() , Toast.LENGTH_SHORT).show();


    }

//    @Override
//    public boolean onMarkerClick(@NonNull Marker marker) {
//
////        binding.latitude.setText(String.valueOf(marker.getPosition().latitude));
////        binding.longitude.setText(String.valueOf(marker.getPosition().longitude));
////
////        Toast.makeText(this, "The Postion: "+marker.getPosition(), Toast.LENGTH_SHORT).show();
//
//        return false;
//    }


}