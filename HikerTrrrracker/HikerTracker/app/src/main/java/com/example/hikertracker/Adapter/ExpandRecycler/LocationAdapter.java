package com.example.hikertracker.Adapter.ExpandRecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hikertracker.Models.Locations;
import com.example.hikertracker.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class LocationAdapter extends ExpandableRecyclerViewAdapter<LocationViewHolder, LocationBodyViewHolder> {

    public LocationAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public LocationViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_recyclerview, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public LocationBodyViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.body_recyclerview, parent, false);
        return new LocationBodyViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(LocationBodyViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        //final Locations locations = ((LocaIcon) group).getItems().get(childIndex);
        /*holder.setLocationName(locations.getLatitude().toString(),
                locations.getLongitude().toString(),
                locations.getContact().toString(),
                locations.getType());*/
    }

    @Override
    public void onBindGroupViewHolder(LocationViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {

        //final Locations locations = ((LocaIcon) group).getItems().get(0);

        holder.setLocationTitle(group);
    }
}