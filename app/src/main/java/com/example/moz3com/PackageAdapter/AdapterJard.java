package com.example.moz3com.PackageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.PackageData.DataJard;
import com.example.moz3com.R;

import java.util.List;

public class AdapterJard extends RecyclerView.Adapter<AdapterJard.ViewHolder> {
    List<DataJard>jards;
    Context context;
  public AdapterJard( List<DataJard>jards,Context context){
      this.context=context;
      this.jards=jards;
  }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.jard,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(jards.get(position).getDate());
        holder.name.setText(jards.get(position).getName());
        holder.sale.setText(jards.get(position).getSale()+"");
        holder.rb7.setText(jards.get(position).getMrb7()+"");
        holder.cont.setText(jards.get(position).getCount()+"");
        holder.countaval.setText(jards.get(position).getContaval()+"");
        holder.buy.setText(jards.get(position).getBay()+"");
        holder.total.setText(jards.get(position).getTotal()+"");
    }

    @Override
    public int getItemCount() {
        return jards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      TextView name,total,cont,countaval,sale,buy,rb7,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.item);
            total =itemView.findViewById(R.id.total);
            cont =itemView.findViewById(R.id.count);
            countaval =itemView.findViewById(R.id.contaval);
            sale =itemView.findViewById(R.id.ba3);
            buy =itemView.findViewById(R.id.price);
            rb7 =itemView.findViewById(R.id.rb6);
            date =itemView.findViewById(R.id.datejard);
        }
    }
}
