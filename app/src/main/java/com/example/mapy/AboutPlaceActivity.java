package com.example.mapy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mapy.databinding.ActivityAboutPlaceBinding;
import com.example.mapy.models.TempleModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutPlaceActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private ActivityAboutPlaceBinding binding;
    private GoogleMap mMap;
    private Marker marker;
    private LatLng aboutPlaceLocation;
    TempleModel templeModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAboutPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent returnIntent = getIntent();

        templeModel = returnIntent.getExtras().getParcelable("templeObj");

        if (templeModel != null) {
            binding.mandirNameTV.setText(templeModel.getName());
            aboutPlaceLocation = new LatLng(templeModel.getLatitude(), templeModel.getLongitude());
        } else {
            Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            finish();
        }

        // like Buttons
        binding.unlikedFavouriteButton.setOnClickListener(this);
        binding.likedFavouriteButton.setOnClickListener(this);

        // Play/Pause Buttons
        binding.playButton.setOnClickListener(this);
        binding.pauseButton.setOnClickListener(this);

        // star Buttons
        binding.likedStarButton.setOnClickListener(this);
        binding.unlikedStarButton.setOnClickListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.aboutMap);

        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        //aboutPlaceLocation = new LatLng(25.3176, 82.9739);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(aboutPlaceLocation);
        markerOptions.draggable(true);
        markerOptions.title(templeModel.getName());

        marker = mMap.addMarker(markerOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(aboutPlaceLocation, 13f));


    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.unlikedFavouriteButton) {
            binding.unlikedFavouriteButton.setVisibility(View.GONE);
            binding.likedFavouriteButton.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.likedFavouriteButton) {
            binding.likedFavouriteButton.setVisibility(View.GONE);
            binding.unlikedFavouriteButton.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.playButton) {
            binding.playButton.setVisibility(View.GONE);
            binding.pauseButton.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.pauseButton) {
            binding.pauseButton.setVisibility(View.GONE);
            binding.playButton.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.unlikedStarButton) {
            binding.unlikedStarButton.setVisibility(View.GONE);
            binding.likedStarButton.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.likedStarButton) {
            binding.likedStarButton.setVisibility(View.GONE);
            binding.unlikedStarButton.setVisibility(View.VISIBLE);
        }


    }
}