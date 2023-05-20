package com.example.mapy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mapy.databinding.ActivityCustomYatraBinding;
import com.example.mapy.models.TempleModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomYatraActivity extends AppCompatActivity {

    Spinner daySpinner;
    MaterialCardView templeSpinner;

    List<TempleModel> templeModelList;
    boolean[] selectedItems;

    String[] category;
    //String daySpinnerSelectedItem;
    ActivityCustomYatraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomYatraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        SpannableString spannableString = new SpannableString("Custom Yatra");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(spannableString);

        //daySpinner = binding.daySpinner;
        templeSpinner = binding.templeSpinner;

        settingTempleModelList();


        //settingUpDaySpinner();
        settingUpTempleSpinner();

//        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                daySpinnerSelectedItem = selectedItem;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Do nothing
//            }
//        });
//
        binding.templeSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTempleDialog(category);
            }
        });

        binding.showYatraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!binding.dayET.getText().toString().isEmpty()){

                    ArrayList<Integer> selectedTemple = new ArrayList<>();
                    ArrayList<TempleModel> selectedTem = new ArrayList<>();

                    boolean hasTemple = false;
                    int count = 0;

                    for(int i=0;i<selectedItems.length;i++){
                        if(selectedItems[i]){
                            hasTemple = true;
                            count++;
                            selectedTemple.add(i);
                            selectedTem.add(templeModelList.get(i));
                        }
                    }
                    if(hasTemple && count>1){

                        Log.d("custom", "Final Output\nDays = "+binding.dayET.getText().toString() +" \nSelected Temples: ");
                        Log.d("custom", "List Size: "+templeModelList.size());
                        for(TempleModel templeModel: selectedTem){
                            Log.d("custom", "Name: "+templeModel.getName());
                        }

                        Intent intent = new Intent(CustomYatraActivity.this,YatraRouteActivity.class);
                        //intent.putIntegerArrayListExtra("positions",selectedTemple);
                        intent.putExtra("day","Custom");
                        intent.putParcelableArrayListExtra("temples", selectedTem);
                        //intent.putExtra("temples", (Parcelable) selectedTem);
                        startActivity(intent);

                    }
                    else{
                        if(count==0)
                        Toast.makeText(CustomYatraActivity.this, "Select a Temple", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(CustomYatraActivity.this, "Select Two atleast Two Temple To Show Route", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                else
                    Toast.makeText(CustomYatraActivity.this, "Please Enter a Date", Toast.LENGTH_SHORT).show();
                    binding.dayET.requestFocus();
            }
        });

    }


    private void settingUpDaySpinner() {


        String[] category = {"1 Day",
                "1 Day",
                "2 Day",
                "3 Day",
                "4 Day",
                "5 Day",
                "6 Day",
                "7 Day"
        };

        // Initialize the Spinner with an adapter

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, category);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);


    }

    private void settingUpTempleSpinner() {


        category = new String[templeModelList.size()];
        selectedItems = new boolean[templeModelList.size()];

        for (int i = 0; i < templeModelList.size(); i++) {

            TempleModel templeModel = templeModelList.get(i);
            category[i] = templeModel.getName();

        }

//
//
//
//        // Initialize the Spinner with an adapter
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, category);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        templeSpinner.setAdapter(adapter);


    }

    private void showTempleDialog(String[] category) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Temple");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(category, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {


                if (isChecked)
                    selectedItems[which] = true;
                else
                    selectedItems[which] = false;

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < templeModelList.size(); i++) {

                    if (selectedItems[i]) {
                        if (stringBuilder.length()!=0)
                            stringBuilder.append(", ");

                        stringBuilder.append(templeModelList.get(i).getName());
                    }

                }
                binding.templeTV.setText(stringBuilder);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItems = null;
                selectedItems = new boolean[templeModelList.size()];
                binding.templeTV.setText("");
            }
        });
        builder.show();

    }

    private void settingTempleModelList() {

        templeModelList = new ArrayList<>();

        templeModelList.add(new TempleModel(25.3124, 83.0109, "id1", "Kashi Vishwanath Temple", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.3011, 83.0063, "id3", "Tulsi Manas Temple", R.drawable.tulsimanastemple));
        templeModelList.add(new TempleModel(25.2755, 82.9994, "id2", "Sankat Mochan Hanuman Temple", R.drawable.sankatmochanmandir));
        templeModelList.add(new TempleModel(25.3081, 83.0079, "id2", "Durga Temple", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.2773, 82.9996, "id3", "New Vishwanath Temple (Birla Temple)", R.drawable.kashivishwanath));
        templeModelList.add(new TempleModel(25.3088, 83.0085, "id4", "Kaal Bhairav Temple", R.drawable.kashivishwanath));

    }

}