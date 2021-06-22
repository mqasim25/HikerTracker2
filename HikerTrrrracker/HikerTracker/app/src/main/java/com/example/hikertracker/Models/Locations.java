package com.example.hikertracker.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Locations implements Parcelable {

    private Double latitude;
    private Double longitude;
    private String timestamp;
    private Boolean contact;
    private String type;

    public Locations() {

    }

    public Locations(Double latitude, Double longitude, String timestamp, Boolean contact, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.contact = contact;
        this.type = type;
    }


    protected Locations(Parcel in) {
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        timestamp = in.readString();
        byte tmpContact = in.readByte();
        contact = tmpContact == 0 ? null : tmpContact == 1;
        type = in.readString();
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {
        @Override
        public Locations createFromParcel(Parcel in) {
            return new Locations(in);
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[size];
        }
    };

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getContact() {
        return contact;
    }

    public void setContact(Boolean contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(timestamp);
        dest.writeByte((byte) (contact == null ? 0 : contact ? 1 : 2));
        dest.writeString(type);*/

        dest.writeString(latitude.toString());
        dest.writeString(longitude.toString());
        dest.writeString(timestamp);
        dest.writeString(contact.toString());
        dest.writeString(type);
    }
}
