package com.example.mapy;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mapy.models.TempleModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapy.databinding.ActivityYatraRouteBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YatraRouteActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityYatraRouteBinding binding;

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

        binding = ActivityYatraRouteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        String day = intent.getStringExtra("day");
        if(day.equals("Day 1")){
            settingDay1List();
        } else if(day.equals("Day 2")){
            settingDay2List();
        }
        else if(day.equals("Custom")){

            templeModelList = intent.getParcelableArrayListExtra("temples");

            //ArrayList<Integer> getArrayPosition = intent.getIntegerArrayListExtra("positions");
            //settingCustomDayList(getArrayPosition);
        }




        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching route, please wait...");
        mProgressDialog.setCancelable(false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void settingCustomDayList(ArrayList<Integer> getArrayPosition) {

        templeModelList = new ArrayList<>();

       // for

    }


    private void settingDay2List() {

        templeModelList = new ArrayList<>();

        templeModelList.add(new TempleModel(25.3124, 83.0109, "id1", "Kashi Vishwanath Temple", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.3081, 83.0079, "id2", "Durga Temple", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.2773, 82.9996, "id3", "New Vishwanath Temple (Birla Temple)", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.3088, 83.0085, "id4", "Kaal Bhairav Temple", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.3124, 83.0109, "id5", "Kashi Vishwanath Temple", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.3011, 83.0063, "id6", "Tulsi Manas Temple", R.drawable.tulsimanastemple));
        templeModelList.add(new TempleModel(25.2755, 82.9994, "id7", "Sankat Mochan Hanuman Temple", R.drawable.sankatmochanmandir));


    }



    private void settingDay1List() {

        templeModelList = new ArrayList<>();

        templeModelList.add(new TempleModel(25.3124, 83.0109, "id1", "Kashi Vishwanath Temple", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.3011, 83.0063, "id3", "Tulsi Manas Temple", R.drawable.tulsimanastemple));
        templeModelList.add(new TempleModel(25.2755, 82.9994, "id2", "Sankat Mochan Hanuman Temple", R.drawable.sankatmochanmandir));


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // API_KEY = getString(R.string.google_places_api2);
        mMap.setOnMarkerClickListener(this);

        for (int a=0;a<templeModelList.size();a++){

            Log.d("yatra", "onMapReady: Name " + templeModelList.get(a).getName());

        }

        for (int i = 0; i < templeModelList.size(); i++) {

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

            if (i+1 < templeModelList.size()) {
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

                Intent intent = new Intent(YatraRouteActivity.this, AboutPlaceActivity.class);
                intent.putExtra("templeObj", templeModel);
                startActivity(intent);

            }


        }


        return false;
    }
}