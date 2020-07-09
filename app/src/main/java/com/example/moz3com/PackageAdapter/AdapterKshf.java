package com.example.moz3com.PackageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.PackageData.Kshf;
import com.example.moz3com.R;

import java.util.List;

public class AdapterKshf extends RecyclerView.Adapter<AdapterKshf.ViewHolder> {
    List<Kshf>kshfs;
    Context context;
    public AdapterKshf(Context context,List<Kshf>kshfs){
        this.kshfs=kshfs;
        this.context=context;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layoutkshf,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.type.setText(kshfs.get(position).getType());
        holder.total.setText(kshfs.get(position).getTotal());
        holder.date.setText(kshfs.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return kshfs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,total,type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date =itemView.findViewById(R.id.item);
            total =itemView.findViewById(R.id.total);
            type =itemView.findViewById(R.id.price);
        }
    }
}
