package com.example.mapy;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.example.mapy.models.TempleModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
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

public class RouteMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityRouteMapsBinding binding;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Polyline mPolyline;

    private List<TempleModel> templeModelList;
    private MarkerOptions lastMarker;

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

        settingTempleModelList();

        // Get the origin and destination LatLng values from your previous activity or user input
//        mOriginLatLng = new LatLng(26.8530, 80.94624);
//        mDestinationLatLng = new LatLng(26.8467, 81.0037);


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

    private void settingTempleModelList() {

        templeModelList = new ArrayList<>();

        templeModelList.add(new TempleModel(25.3124, 83.0109, "id1", "Kashi Vishwanath Temple", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.3011, 83.0063, "id3", "Tulsi Manas Temple", R.drawable.tulsimanastemple));
        templeModelList.add(new TempleModel(25.2755, 82.9994, "id2", "Sankat Mochan Hanuman Temple", R.drawable.sankatmochanmandir));

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
        } else
            Toast.makeText(this, "Polyline is null", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // API_KEY = getString(R.string.google_places_api2);
        mMap.setOnMarkerClickListener(this);


        for (int i = 0; i <= templeModelList.size() - 1; i++) {

            mOriginLatLng = new LatLng(templeModelList.get(i).getLatitude(), templeModelList.get(i).getLongitude());


            // Add markers for the origin and destination locations
            String temple1 = templeModelList.get(i).getName();

            View customMarkerView = getLayoutInflater().inflate(R.layout.custom_map_marker_item, null);
            TextView titleTextView = customMarkerView.findViewById(R.id.title_item);
            ImageView imageView = customMarkerView.findViewById(R.id.image_item);

            titleTextView.setText(temple1);
            imageView.setImageResource(templeModelList.get(i).getImage());

            mMap.addMarker(new MarkerOptions().position(mOriginLatLng)
                    .title(temple1)
                    .icon(BitmapDescriptorFactory.fromBitmap(createBitmapFromView(customMarkerView))));

            if (i+1 < templeModelList.size() -1 ) {
                String temple2 = templeModelList.get(i + 1).getName();
                mDestinationLatLng = new LatLng(templeModelList.get(i + 1).getLatitude(), templeModelList.get(i + 1).getLongitude());

                lastMarker = new MarkerOptions().position(mDestinationLatLng)
                        .title(temple2);
                mMap.addMarker(lastMarker);

            }


            // Fetch the route using the Google Maps Directions API
            fetchRoute();


        }


        // Move the camera to the origin location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(templeModelList.get(0).getLatitude(), templeModelList.get(0).getLongitude()), 15));
    }

    private Bitmap createBitmapFromView(View view) {

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;

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


        PolylineOptions options = new PolylineOptions()
                .addAll(points)
                .color(Color.RED)
                .width(5);

        mPolyline = mMap.addPolyline(options);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mOriginLatLng);
        builder.include(mDestinationLatLng);
        LatLngBounds bounds = builder.build();

        //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(this, "OnMarker Click", Toast.LENGTH_SHORT).show();

        LatLng curMarker;
        for (int i = 0; i < templeModelList.size(); i++) {

            TempleModel templeModel = templeModelList.get(i);

            if (Double.valueOf(marker.getPosition().longitude).equals(Double.valueOf(templeModel.getLongitude())) &&
                    Double.valueOf(marker.getPosition().latitude).equals(Double.valueOf(templeModel.getLatitude()))) {

                Intent intent = new Intent(RouteMapsActivity.this, AboutPlaceActivity.class);
                intent.putExtra("templeObj", templeModel);
                startActivity(intent);

            }


        }


        return false;
    }
}