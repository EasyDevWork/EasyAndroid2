package com.easy.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private String name;

    public Food(String name) {
        this.name = name;
    }

    public Food() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name='" + name;
    }

    public void readFromParcel(Parcel dest) {
        name = dest.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected Food(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
