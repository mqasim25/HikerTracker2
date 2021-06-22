package com.example.hikertracker.Adapter.ExpandRecycler;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hikertracker.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class LocationViewHolder extends GroupViewHolder {

    private TextView timeStampTV;
    private ImageView arrow;
    private ImageView icon;

    public LocationViewHolder(View itemView) {
        super(itemView);
        timeStampTV = (TextView) itemView.findViewById(R.id.timeStampTVID);
        arrow = (ImageView) itemView.findViewById(R.id.list_item_genre_arrow);
        icon = (ImageView) itemView.findViewById(R.id.list_item_locations_icon);
    }

    public void setLocationTitle(ExpandableGroup genre) {
        if (genre instanceof LocaIcon) {
            timeStampTV.setText(genre.getTitle());
            icon.setBackgroundResource(((LocaIcon) genre).getIconResId());
        }
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}