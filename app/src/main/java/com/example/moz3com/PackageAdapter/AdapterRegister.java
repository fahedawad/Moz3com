package com.example.moz3com.PackageAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.AllBillsForUser;
import com.example.moz3com.ListScreen;
import com.example.moz3com.PackageData.User;
import com.example.moz3com.R;

import java.util.List;

public class AdapterRegister extends RecyclerView.Adapter<AdapterRegister.ViewHolder> {
    Context context;
    List<User> users;
    public AdapterRegister(Context context,List<User>users){
        this.users=users;
        this.context=context;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(users.get(position).getUser());
        final String id =users.get(position).getId();
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, ListScreen.class);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.user);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

