package com.example.mapy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.mapy.databinding.ActivityAboutPlaceBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutPlaceActivity extends FragmentActivity implements OnMapReadyCallback {

    ActivityAboutPlaceBinding binding;

    private GoogleMap mMap;
    private Marker marker;
    private LatLng aboutPlaceLocation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAboutPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.aboutMap);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        aboutPlaceLocation = new LatLng(25.3176, 82.9739);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(aboutPlaceLocation);
        markerOptions.draggable(true);
        markerOptions.title("Near Varanasi");

        marker = mMap.addMarker(markerOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(aboutPlaceLocation,13f));



    }
}