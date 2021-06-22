package com.example.hikertracker.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hikertracker.Models.Locations;
import com.example.hikertracker.R;

import java.util.ArrayList;

public class MyRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Locations> listItemArrayList;

    public MyRecyclerviewAdapter(Context context, ArrayList<Locations> listItemArrayList) {

        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listItemArrayList = listItemArrayList;
    }

    @Override
    public int getItemCount() {

        return listItemArrayList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        View view = inflater.inflate(R.layout.row_recyclerview, parent, false);
        holder = new MyViewHolderHeader(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        MyViewHolderHeader itemHolder = (MyViewHolderHeader) holder;
        itemHolder.timeStampTV.setText(listItemArrayList.get(position).getTimestamp());
        itemHolder.latitudeTV.setText(String.valueOf(listItemArrayList.get(position).getLatitude()));
        itemHolder.longitudeTV.setText(String.valueOf(listItemArrayList.get(position).getLongitude()));
        itemHolder.contactTV.setText(String.valueOf(listItemArrayList.get(position).getContact()));
        itemHolder.typeTV.setText(listItemArrayList.get(position).getType());
    }

    class MyViewHolderHeader extends RecyclerView.ViewHolder{

        TextView timeStampTV, latitudeTV, longitudeTV, contactTV, typeTV;

        MyViewHolderHeader(View itemView) {
            super(itemView);

            timeStampTV = itemView.findViewById(R.id.timeStampTVID);
            latitudeTV = itemView.findViewById(R.id.latitudeTVID);
            longitudeTV = itemView.findViewById(R.id.longitudeTVID);
            contactTV = itemView.findViewById(R.id.contactTVID);
            typeTV = itemView.findViewById(R.id.typeTVID);
        }
    }
}
