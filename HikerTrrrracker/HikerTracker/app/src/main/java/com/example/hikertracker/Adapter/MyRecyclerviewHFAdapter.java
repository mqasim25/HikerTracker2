package com.example.hikertracker.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hikertracker.Models.Locations;
import com.example.hikertracker.R;

import java.util.List;

public class MyRecyclerviewHFAdapter extends RecyclerView.Adapter<MyRecyclerviewHFAdapter.LocationsViewHolder> {

    Boolean truefalse = false;
    Boolean trueArrow = false;

    private List<Locations> locationsList;
    private Context context;

    private static int currentPosition = 0;

    public MyRecyclerviewHFAdapter(List<Locations> locationsList, Context context) {
        this.locationsList = locationsList;
        this.context = context;
    }

    @Override
    public LocationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recyclerview, parent, false);
        return new LocationsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LocationsViewHolder holder, final int position) {


        //LocationsViewHolder locationsViewHolder = (LocationsViewHolder) holder;

        holder.timeStampTV.setText(locationsList.get(position).getTimestamp());
        holder.latitudeTV.setText(String.valueOf(locationsList.get(position).getLatitude()));
        holder.longitudeTV.setText(String.valueOf(locationsList.get(position).getLongitude()));
        holder.contactTV.setText(String.valueOf(locationsList.get(position).getContact()));
        holder.typeTV.setText(locationsList.get(position).getType());

        //holder.arrowImg.setImageResource(R.drawable.ic_arrow_down);

        //holder.linearLayout.setVisibility(View.GONE);



        holder.timeStampTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                truefalse = true;

                Log.e("truefalseRecyclerview", truefalse.toString());
                //Log.e("truefalseRecPos", String.valueOf(holder.getAdapterPosition()));

                //reloding the list
                notifyDataSetChanged();
            }
        });


        //if the position is equals to the item position which is to be expanded
        if (truefalse) {
            if (currentPosition == holder.getAdapterPosition()) {

                Log.e("truefalseRecPos", String.valueOf(holder.getAdapterPosition()));

                if (trueArrow) {

                    //creating an animation
                    //Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

                    //toggling visibility
                    holder.linearLayout.setVisibility(View.GONE);

                    //adding sliding effect
                    //holder.linearLayout.startAnimation(slideDown);

                    RotateAnimation rotateAnim = new RotateAnimation(90, 0.0f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnim.setDuration(200);
                    rotateAnim.setFillAfter(true);
                    holder.arrowImg.startAnimation(rotateAnim);

                    Log.e("truefalsePos1", truefalse.toString());

                    trueArrow = false;
                } else {

                    //creating an animation
                    Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

                    //toggling visibility
                    holder.linearLayout.setVisibility(View.VISIBLE);

                    //adding sliding effect
                    holder.linearLayout.startAnimation(slideDown);

                    RotateAnimation rotateAnim = new RotateAnimation(0.0f, 90,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnim.setDuration(200);
                    rotateAnim.setFillAfter(true);
                    holder.arrowImg.startAnimation(rotateAnim);

                    Log.e("truefalsePos2", truefalse.toString());


                    trueArrow = true;

                    /*if (currentPosition == position) {


                    }*/
                }
            } else {

                Log.e("truearrow", trueArrow.toString());

                trueArrow = false;
                holder.linearLayout.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public int getItemCount() {
        return locationsList.size();
    }

    class LocationsViewHolder extends RecyclerView.ViewHolder {

        TextView timeStampTV, latitudeTV, longitudeTV, contactTV, typeTV;
        LinearLayout linearLayout;
        ImageView arrowImg;

        LocationsViewHolder(View itemView) {
            super(itemView);

            timeStampTV = itemView.findViewById(R.id.timeStampTVID);
            latitudeTV = itemView.findViewById(R.id.latitudeTVID);
            longitudeTV = itemView.findViewById(R.id.longitudeTVID);
            contactTV = itemView.findViewById(R.id.contactTVID);
            typeTV = itemView.findViewById(R.id.typeTVID);

            linearLayout = itemView.findViewById(R.id.linearlayoutID);
            arrowImg = itemView.findViewById(R.id.arrowIVRID);
        }
    }
}