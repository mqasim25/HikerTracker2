package com.example.hikertracker.Adapter.ExpandRecycler;

import com.example.hikertracker.Models.Locations;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class LocaIcon extends ExpandableGroup<Locations> {

    private int iconResId;

    public LocaIcon(String title, List<Locations> items, int iconResId) {
        super(title, items);
        this.iconResId = iconResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocaIcon)) return false;

        LocaIcon locaIcon = (LocaIcon) o;

        return getIconResId() == locaIcon.getIconResId();

    }

    @Override
    public int hashCode() {
        return getIconResId();
    }
}