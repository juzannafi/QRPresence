package com.example.retrofittest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter  extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<String> rvDate, rvClass;

    public Adapter(ArrayList<String> inputDate) {
        rvDate = inputDate;
        //rvClass = inputClass;
    }




    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        final String name = rvDate.get(position);
        holder.tvDate.setText("Tanggal : " + rvDate.get(position));
        //holder.tvClassId.setText("ID Kelas : " + rvClass.get(position));
    }

    @Override
    public int getItemCount()
    {
        return rvDate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView tvDate;
        //public TextView tvClassId;

        public ViewHolder(View v) {
            super(v);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            //tvClassId = (TextView) v.findViewById(R.id.tv_classid);
        }
    }
}

