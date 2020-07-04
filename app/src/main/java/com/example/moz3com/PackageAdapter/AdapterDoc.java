package com.example.moz3com.PackageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.AllDocuments;
import com.example.moz3com.PackageData.Doc;
import com.example.moz3com.R;

import java.util.List;

public class AdapterDoc extends RecyclerView.Adapter<AdapterDoc.ViewHolder> {
  List<Doc>docs;
  Context context;
    public AdapterDoc(List<Doc>docs, Context context){
        this.context=context;
        this.docs=docs;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_document,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bio.setEnabled(false);
        viewHolder.totaltxt.setEnabled(false);
        viewHolder.total.setEnabled(false);
        viewHolder.numshek.setEnabled(false);
        viewHolder.bank.setEnabled(false);
        viewHolder.bank.setText(docs.get(i).getBank());
        viewHolder.total.setText(docs.get(i).getTotal());
        viewHolder.totaltxt.setText(docs.get(i).getTotaltxt());
        viewHolder.numshek.setText(docs.get(i).getSheknum());
        viewHolder.bio.setText(docs.get(i).getBio());
        viewHolder.name.setText(docs.get(i).getName());
        viewHolder.date.setText(docs.get(i).getDate());
        String type =docs.get(i).getType();
        if (type.equals("شيك")){
            viewHolder.shek.setChecked(true);
        }
        if (type.equals("نقد")){
            viewHolder.cash.setChecked(true);
        }
        viewHolder.button.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,name;
        EditText total,totaltxt,numshek,bank,bio;
         RadioButton cash,shek;
         Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date =itemView.findViewById(R.id.daterep);
            name =itemView.findViewById(R.id.namecust);
            total =itemView.findViewById(R.id.many);
            totaltxt =itemView.findViewById(R.id.manytxt);
            numshek =itemView.findViewById(R.id.numshak);
            bank =itemView.findViewById(R.id.nameshak);
            bio =itemView.findViewById(R.id.bio);
            cash =itemView.findViewById(R.id.cashrep);
            shek =itemView.findViewById(R.id.shecrep);
            button =itemView.findViewById(R.id.ok);

        }
    }
}
