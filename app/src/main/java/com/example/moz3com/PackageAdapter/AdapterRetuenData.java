package com.example.moz3com.PackageAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.Change;
import com.example.moz3com.PackageData.Data;
import com.example.moz3com.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class AdapterRetuenData extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  List<Data>dates;
  Context context;
    ImageView imageView;
    TextView textView;
    Data data;

    public AdapterRetuenData(List<Data>dates,Context context){
        this.dates =dates;
        this.context=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            textView.setText(dates.get(position).getName());
            Picasso.get().load(dates.get(position).getUri()).resize(1080,1080).into(imageView);
            final String itme =dates.get(position).getName();
            imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, Change.class);
                intent.putExtra("itme",itme);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View view){
            super(view);

            textView =view.findViewById(R.id.name);
            imageView =view.findViewById(R.id.img);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return(position);
    }
}
