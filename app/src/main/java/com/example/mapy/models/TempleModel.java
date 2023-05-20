package com.example.mapy.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class TempleModel implements Parcelable {

    String name,id;

    double latitude,longitude;
    int image;

    public TempleModel() {
    }

    public TempleModel(double latitude, double longitude, String id,String name,  int image) {
        this.name = name;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    protected TempleModel(Parcel in) {
        name = in.readString();
        id = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        image = in.readInt();
    }

    public static final Creator<TempleModel> CREATOR = new Creator<TempleModel>() {
        @Override
        public TempleModel createFromParcel(Parcel in) {
            return new TempleModel(in);
        }

        @Override
        public TempleModel[] newArray(int size) {
            return new TempleModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(image);
    }
}
