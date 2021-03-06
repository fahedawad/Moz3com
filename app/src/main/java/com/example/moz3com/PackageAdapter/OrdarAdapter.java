package com.example.moz3com.PackageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.PackageData.OrdarData;
import com.example.moz3com.R;

import java.util.List;


public class OrdarAdapter extends RecyclerView.Adapter<OrdarAdapter.ViewHolder> {
    @NonNull

    Context context ;
    List<OrdarData> ordarData;
    public OrdarAdapter(List<OrdarData> ordarData , Context context){
        this.context=context;
        this.ordarData=ordarData;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.price.setText(ordarData.get(position).getPrice());
        holder.count.setText(ordarData.get(position).getConter());
        if(ordarData.get(position).getType().equals("كيلو")){
            holder.total.setText("بانتظار المعاينة");
        }
        else { holder.total.setText(ordarData.get(position).getTotal());}

        holder.name.setText(ordarData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return ordarData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView name,total,count,price;
        public ViewHolder(View view){
            super(view);
           name =view.findViewById(R.id.item);
            total =view.findViewById(R.id.total);
            count =view.findViewById(R.id.count);
            price =view.findViewById(R.id.price);
        }
    }
}
