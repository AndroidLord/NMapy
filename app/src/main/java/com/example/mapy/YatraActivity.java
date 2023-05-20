package com.example.mapy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.example.mapy.adaptor.AdaptorOnClickListener;
import com.example.mapy.adaptor.YatraAdaptor;
import com.example.mapy.databinding.ActivityYatraBinding;
import com.example.mapy.models.TempleModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class YatraActivity extends AppCompatActivity implements AdaptorOnClickListener {

    private Context context = this;
    ActivityYatraBinding binding;
    private AdaptorOnClickListener adaptorOnClickListener;
    private List<String> daylist;

    RouteMapsActivity routeMapsActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYatraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        SpannableString spannableString = new SpannableString("Yatra");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(spannableString);

        settingUpDayList();


        settingUpRecyclerView();

    }

    private void settingUpDayList() {

        daylist = new ArrayList<>();

        daylist.add(new String("Day 1"));
        daylist.add(new String("Day 2"));
        daylist.add(new String("Day 3"));
        daylist.add(new String("Day 4"));
        daylist.add(new String("Day 5"));
        daylist.add(new String("Day 6"));
        daylist.add(new String("Create Custom Yatra"));


    }

    private void settingUpRecyclerView() {

        YatraAdaptor yatraAdaptor = new YatraAdaptor(daylist, context, this);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        if (getResources().getConfiguration().orientation == 2) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        }

        binding.recyclerView.setAdapter(yatraAdaptor);

    }

    @Override
    public void onClick(int position) {
        Snackbar.make(binding.recyclerView, "Showing " + daylist.get(position) + " Plans", Snackbar.LENGTH_SHORT).show();

        String selectedDay = daylist.get(position);

        if (selectedDay.equals("Day 1")) {
            Intent intent = new Intent(YatraActivity.this, YatraRouteActivity.class);
            intent.putExtra("day", "Day 1");
            startActivity(intent);

        } else if (selectedDay.equals("Day 2")) {

            Intent intent = new Intent(YatraActivity.this,YatraRouteActivity.class);
            intent.putExtra("day","Day 2");
            startActivity(intent);


        } else if (selectedDay.equals("Day 3")) {
            startActivity(new Intent(YatraActivity.this, YatraRouteActivity.class));
        } else if (selectedDay.equals("Day 4")) {
            startActivity(new Intent(YatraActivity.this, YatraRouteActivity.class));
        } else if (selectedDay.equals("Day 5")) {
            startActivity(new Intent(YatraActivity.this, YatraRouteActivity.class));
        } else {

            Intent intent = new Intent(YatraActivity.this, CustomYatraActivity.class);
            startActivity(intent);
        }

    }




    @Override
    public void onLongClick(int position) {

    }
}