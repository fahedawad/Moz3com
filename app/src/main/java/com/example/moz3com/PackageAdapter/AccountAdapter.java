package com.example.moz3com.PackageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.PackageData.AccountData;
import com.example.moz3com.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    Context context;
    List<AccountData> accountDataList;
    public AccountAdapter( List<AccountData> accountDataList,Context context){
        this.accountDataList =accountDataList;
        this.context=context;

    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.account,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.total.setText(accountDataList.get(i).getTotal());
        viewHolder.date.setText(accountDataList.get(i).getDate());
        viewHolder.textView.setText(accountDataList.get(i).getType());
    }

    @Override
    public int getItemCount() {
        return accountDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,total,textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date =itemView.findViewById(R.id.accountdate);
            total =itemView.findViewById(R.id.accounttotal);
            textView =itemView.findViewById(R.id.accounttype);
        }
    }
}
