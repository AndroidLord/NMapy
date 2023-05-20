package com.example.mapy.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapy.R;

import java.util.List;

public class YatraAdaptor extends RecyclerView.Adapter<YatraAdaptor.ViewHolder> {

    private List<String> daylist;
    private Context context;
    private AdaptorOnClickListener adaptorOnClickListener;

    public YatraAdaptor(List<String> daylist, Context context, AdaptorOnClickListener adaptorOnClickListener) {
        this.daylist = daylist;
        this.context = context;
        this.adaptorOnClickListener = adaptorOnClickListener;
    }

    @NonNull
    @Override
    public YatraAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_item,parent,false);
        return new YatraAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YatraAdaptor.ViewHolder holder, int position) {

        String title = daylist.get(position);

        holder.title.setText(title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptorOnClickListener.onClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return daylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.name_item);

        }
    }
}
