package com.example.mapy;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapy.databinding.ActivityRouteMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RouteMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityRouteMapsBinding binding;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Polyline mPolyline;
    private LatLng mOriginLatLng;
    private LatLng mDestinationLatLng;
    private ProgressDialog mProgressDialog;

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json";
    private static String API_KEY = "AIzaSyDRxg2JNiAQaORroerqF9tFSNB1VnNn5AM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRouteMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Get the origin and destination LatLng values from your previous activity or user input
        mOriginLatLng = new LatLng(26.8530 , 80.94624);
        mDestinationLatLng = new LatLng(26.8467 , 81.0037);


        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching route, please wait...");
        mProgressDialog.setCancelable(false);

        // Initialize the location request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Initialize the location callback
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    mCurrentLocation = location;
                    updateUI();
                }
            }
        };

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Routemap);
        mapFragment.getMapAsync(this);

  }

    private void updateUI() {
        Log.d("route", "Inside the updateUI: ");
        if (mMap == null || mCurrentLocation == null) {
            Log.d("route", "mMap or mCurrent Location is null.. so return ");
            return;
        }

        Log.d("route", "mMap or mCurrent Location is not null ");
        // Update the map camera position to the current location
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));

        // If the polyline has been drawn, calculate the distance and time to destination
        if (mPolyline != null) {
            Log.d("route", "polyline is not null..");
            float[] distanceResults = new float[1];

            Location.distanceBetween(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),
                    mDestinationLatLng.latitude, mDestinationLatLng.longitude, distanceResults);

            // Convert the distance from meters to kilometers and format it with one decimal place
            float distanceInKm = distanceResults[0] / 1000f;
            String formattedDistance = String.format(Locale.getDefault(), "%.1f km", distanceInKm);

            // Calculate the time to destination in minutes, assuming a walking speed of 5 km/h
            int timeInMinutes = (int) (distanceInKm / 5f * 60f);

            // Update the distance and time text views
            // TextView distanceTextView = findViewById(R.id.distance_text_view);
//            distanceTextView.setText(formattedDistance);
//            Toast.makeText(this, "Distance is:" + formattedDistance, Toast.LENGTH_SHORT).show();
//
//            TextView timeTextView = findViewById(R.id.time_text_view);
//            timeTextView.setText(getString(timeInMinutes));
//            Log.d("route", "UpdateUI method finished");
        }
        else
            Toast.makeText(this, "Polyline is null", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       // API_KEY = getString(R.string.google_places_api2);

        // Add markers for the origin and destination locations
        mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Origin"));
        mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destination"));

        // Move the camera to the origin location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOriginLatLng, 12));

        // Fetch the route using the Google Maps Directions API
        fetchRoute();

    }


    private void fetchRoute() {
        mProgressDialog.show();

        String url = BASE_URL + "?origin=" + mOriginLatLng.latitude + "," + mOriginLatLng.longitude +
                "&destination=" + mDestinationLatLng.latitude + "," + mDestinationLatLng.longitude +
                "&key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<LatLng> points = decodePoly(response.optJSONArray("routes")
                                .optJSONObject(0).optJSONObject("overview_polyline").optString("points"));

                        if (points != null) {
                            drawPolyline(points);
                            updateUI();
                            mProgressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error fetching route: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    // Decode polyline points
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng(((double) lat / 1E5), ((double) lng / 1E5));
            poly.add(p);
        }

        return poly;
    }

    // Draw the polyline on the map
    private void drawPolyline(List<LatLng> points) {
        if (mPolyline != null) {
            mPolyline.remove();
        }

        PolylineOptions options = new PolylineOptions()
                .addAll(points)
                .color(Color.RED)
                .width(5);

        mPolyline = mMap.addPolyline(options);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mOriginLatLng);
        builder.include(mDestinationLatLng);
        LatLngBounds bounds = builder.build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }



}