package com.example.mapy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.mapy.adaptor.DashboardAdaptor;
import com.example.mapy.databinding.ActivityDashBoardBinding;
import com.example.mapy.databinding.DashboardListItemBinding;
import com.example.mapy.models.DashBoardModel;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

   ActivityDashBoardBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<DashBoardModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new DashBoardModel(R.drawable.artighat,"Ghat Arti"));
        modelArrayList.add(new DashBoardModel(R.drawable.travel,"Book Vehical"));
        modelArrayList.add(new DashBoardModel(R.drawable.travelhuman,"Travel Destination"));
        modelArrayList.add(new DashBoardModel(R.drawable.varanasi,"Varanasi Photos"));
        modelArrayList.add(new DashBoardModel(R.drawable.rating,"Rating"));
        modelArrayList.add(new DashBoardModel(R.drawable.support,"Support"));
        modelArrayList.add(new DashBoardModel(R.drawable.customerexperience,"Customer Experience"));


        List<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.image7,"Image 1",ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.image6,"Image 2",ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.image8,"Image 3",ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.image4,"Image 4",ScaleTypes.CENTER_CROP));

        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT);


        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        DashboardAdaptor dashboardAdaptor = new DashboardAdaptor(modelArrayList,DashBoardActivity.this);
        binding.recyclerView.setAdapter(dashboardAdaptor);

    }
}