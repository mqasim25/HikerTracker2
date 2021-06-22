package com.example.hikertracker.Adapter.ExpandRecycler;

import android.view.View;
import android.widget.TextView;

import com.example.hikertracker.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class LocationBodyViewHolder extends ChildViewHolder {

    private TextView latitudeTV, longitudeTV, contactTV, typeTV;

    public LocationBodyViewHolder(View itemView) {
        super(itemView);
        latitudeTV = itemView.findViewById(R.id.latitudeTV);
        longitudeTV = itemView.findViewById(R.id.longitudeTV);
        contactTV = itemView.findViewById(R.id.contactTV);
        typeTV = itemView.findViewById(R.id.typeTV);
    }

    public void setLocationName(String latitude, String longitude, String contact, String type) {
        latitudeTV.setText(latitude);
        longitudeTV.setText(longitude);
        contactTV.setText(contact);
        typeTV.setText(type);
    }
}
