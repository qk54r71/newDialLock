package com.diallock.diallock.diallock.Activity.Fragment.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by park on 2016-11-30.
 */
public class TourismInfoData implements Parcelable {

    private String code;
    private String tourImgURL;
    private ArrayList<String> tourSlideImage;

    public TourismInfoData(Parcel parcel) {
        readFromParcel(parcel);
    }

    public TourismInfoData(
            String code,
            String tourImgURL,
            ArrayList<String> tourSlideImage
    ) {
        this.code = code;
        this.tourImgURL = tourImgURL;
        this.tourSlideImage = tourSlideImage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTourImgURL() {
        return tourImgURL;
    }

    public void setTourImgURL(String tourImgURL) {
        this.tourImgURL = tourImgURL;
    }

    public ArrayList<String> getTourSlideImage() {
        return tourSlideImage;
    }

    public void setTourSlideImage(ArrayList<String> tourSlideImage) {
        this.tourSlideImage = tourSlideImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(tourImgURL);
        parcel.writeStringList(tourSlideImage);
    }

    private void readFromParcel(Parcel parcel) {
        code = parcel.readString();
        tourImgURL = parcel.readString();
        parcel.writeStringList(tourSlideImage);
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public TourismInfoData createFromParcel(Parcel parcel) {
            return new TourismInfoData(parcel);
        }

        @Override
        public TourismInfoData[] newArray(int size) {
            return new TourismInfoData[size];
        }
    };
}
