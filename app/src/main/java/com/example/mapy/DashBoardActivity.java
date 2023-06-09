package com.example.mapy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.mapy.adaptor.AdaptorOnClickListener;
import com.example.mapy.adaptor.DashboardAdaptor;
import com.example.mapy.databinding.ActivityDashBoardBinding;
import com.example.mapy.models.DashBoardModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity implements AdaptorOnClickListener {

    Context context = this;
   ActivityDashBoardBinding binding;

   AdaptorOnClickListener adaptorOnClickListener;
    ArrayList<DashBoardModel> modelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //getSupportActionBar().setTitle("DashBoard");

        SpannableString spannableString = new SpannableString("DashBoard");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(spannableString);

        // Setting Up DashBoard List Model
        ArrayList<DashBoardModel> modelArrayList = SettingUpDashBoard();

        // Setting Up Image Slider
        SettingUpImageSlider();


        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        if (getResources().getConfiguration().orientation==2){
            binding.recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        }


        // new DashboardAdaptor(this);

        DashboardAdaptor dashboardAdaptor = new DashboardAdaptor(
                modelArrayList,
                DashBoardActivity.this
                ,this);

        binding.recyclerView.setAdapter(dashboardAdaptor);

        }



    private void SettingUpImageSlider() {
        List<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.image8,null,ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.ghatarti,null,ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.kaalbarvmandir,null,ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.image7,null,ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.image6,null,ScaleTypes.CENTER_CROP));

        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT);
    }

    @NonNull
    private ArrayList<DashBoardModel> SettingUpDashBoard() {

        modelArrayList = new ArrayList<>();
        modelArrayList.add(new DashBoardModel(R.drawable.god,"Yatra"));
        modelArrayList.add(new DashBoardModel(R.drawable.travel,"Book Vehical"));
        modelArrayList.add(new DashBoardModel(R.drawable.tourists,"Travel Destination"));
        modelArrayList.add(new DashBoardModel(R.drawable.gallery,"Varanasi Photos"));
        modelArrayList.add(new DashBoardModel(R.drawable.rating,"Rating"));
        modelArrayList.add(new DashBoardModel(R.drawable.technicalsupport,"Support"));
        modelArrayList.add(new DashBoardModel(R.drawable.customerexperience,"Customer Experience"));
        modelArrayList.add(new DashBoardModel(R.drawable.place,"Add POI"));
        modelArrayList.add(new DashBoardModel(R.drawable.route,"Route"));

        modelArrayList.add(new DashBoardModel(R.drawable.qrcodescan,"QR Scanner"));
        return modelArrayList;

    }

    @Override
    public void onClick(int position) {

        DashBoardModel dashBoardModel = modelArrayList.get(position);
        Snackbar.make(binding.recyclerView,"Selected: " +dashBoardModel.getName(),Snackbar.LENGTH_SHORT).show();

        if(dashBoardModel.getName().equals("Add POI")){

            startActivity(new Intent(context,MapsActivity.class));

        } else if(dashBoardModel.getName().equals("About Mandir")){

            startActivity(new Intent(context,AboutPlaceActivity.class));

        }else if(dashBoardModel.getName().equals("Route")){

            startActivity(new Intent(context,RouteMapsActivity.class));


        }
        else if(dashBoardModel.getName().equals("QR Scanner")){

            startActivity(new Intent(context,QrScannerActivity.class));


        }else if(dashBoardModel.getName().equals("Yatra")){

            startActivity(new Intent(context,YatraActivity.class));


        }



    }

    @Override
    public void onLongClick(int position) {

    }
}