package com.example.moz3com.PackageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.PackageData.itmeList;
import com.example.moz3com.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterSubList extends RecyclerView.Adapter<AdapterSubList.ViewHolder> {
    Context context;
    List<itmeList> list;
    public AdapterSubList (Context context, List<itmeList>itmeLists){
        this.context=context;
        this.list=itmeLists;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order,parent,false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.items.setText(list.get(position).getName());
        holder.pricetxt.setText(list.get(position).getPrice());
        holder.counter.setText(list.get(position).getI()+"");
        holder.total.setText(list.get(position).getTotal()+"");
        holder.wieght.setText(list.get(position).getWieght());
        final String type =list.get(position).getType();
        if (type.equals("فرط")){
            holder.pases.setVisibility(View.VISIBLE);
        }
        else if ((list.get(position).getTotal()+"").equals("0.0")){
            holder.editText.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.VISIBLE);

        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.editText.setVisibility(View.GONE);
                Double newtotal = Double.parseDouble(holder.editText.getText().toString()) * Double.parseDouble(list.get(position).getPrice()) ;
                System.out.println(newtotal);
                holder.button.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference("order").child(list.get(position).getUid()).child(list.get(position).getDate())
                        .child(list.get(position).getName()).child("المجموع").setValue(newtotal+"");
                FirebaseDatabase.getInstance().getReference("order").child(list.get(position).getUid()).child(list.get(position).getDate())
                        .child(list.get(position).getName()).child("الوزن").setValue(holder.editText.getText().toString()+"");
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pricetxt,pases;
        TextView items;
        TextView wieght;
        TextView counter;
        TextView total;
        EditText editText;
        Button button;
        public ViewHolder(@NonNull View item) {
            super(item);
            pricetxt =item.findViewById(R.id.price);
            items =item.findViewById(R.id.item);
            counter=item.findViewById(R.id.count);
            total = item.findViewById(R.id.total);
            wieght = item.findViewById(R.id.weight);
            pases =item.findViewById(R.id.pases);
            editText =item.findViewById(R.id.priceedit);
            button =item.findViewById(R.id.updateprice);
        }
    }
}
