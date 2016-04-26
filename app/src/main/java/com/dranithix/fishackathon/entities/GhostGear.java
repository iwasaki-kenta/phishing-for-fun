package com.dranithix.fishackathon.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class GhostGear implements Parcelable {
    private long id;
    private String name;
    private String imagePath;
    private LatLng position;

    public GhostGear() {

    }

    public GhostGear(long id, String name, LatLng position, String imagePath) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.imagePath = imagePath;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getImagePath() {
        return imagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected GhostGear(Parcel in) {
        id = in.readLong();
        name = in.readString();
        imagePath = in.readString();
        position = (LatLng) in.readValue(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(imagePath);
        dest.writeValue(position);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GhostGear> CREATOR = new Parcelable.Creator<GhostGear>() {
        @Override
        public GhostGear createFromParcel(Parcel in) {
            return new GhostGear(in);
        }

        @Override
        public GhostGear[] newArray(int size) {
            return new GhostGear[size];
        }
    };
}