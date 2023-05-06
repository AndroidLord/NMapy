package com.example.mapy.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapy.R;
import com.example.mapy.models.DashBoardModel;

import java.util.ArrayList;

public class DashboardAdaptor extends RecyclerView.Adapter<DashboardAdaptor.ViewHolder> {

    private ArrayList<DashBoardModel> list;
    private Context context;

    public DashboardAdaptor(ArrayList<DashBoardModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdaptor.ViewHolder holder, int position) {

        DashBoardModel dashBoardModel = list.get(position);

        holder.imageView.setImageResource(dashBoardModel.getImage());
        holder.nameTV.setText(dashBoardModel.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_item);
            nameTV = itemView.findViewById(R.id.name_item);

        }
    }
}
