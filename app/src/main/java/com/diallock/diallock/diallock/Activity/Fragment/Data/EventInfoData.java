package com.diallock.diallock.diallock.Activity.Fragment.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by park on 2016-11-21.
 */
public class EventInfoData implements Parcelable {

    private String code;
    private String eventCategory;
    private String eventSido;
    private String eventGungu;
    private String eventTitle;
    private String eventDate;
    private String eventTime;
    private String eventLocal;
    private String eventProgress;
    private String eventRemainDay;
    private String eventImgURL;
    private ArrayList<String> eventSlideImage;

    public EventInfoData(Parcel parcel) {
        readFromParcel(parcel);
    }

    public EventInfoData(
            String code,
            String eventCategory,
            String eventLocal,
            String eventSido,
            String eventGungu,
            String eventTitle,
            String eventDate,
            String eventTime,
            String eventProgress,
            String eventRemainDay,
            String eventImgURL,
            ArrayList<String> eventSlideImage
    ) {
        this.code = code;
        this.eventCategory = eventCategory;
        this.eventDate = eventDate;
        this.eventGungu = eventGungu;
        this.eventLocal = eventLocal;
        this.eventProgress = eventProgress;
        this.eventSido = eventSido;
        this.eventTime = eventTime;
        this.eventTitle = eventTitle;
        this.eventImgURL = eventImgURL;
        this.eventSlideImage = eventSlideImage;
        this.eventRemainDay = eventRemainDay;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventGungu() {
        return eventGungu;
    }

    public void setEventGungu(String eventGungu) {
        this.eventGungu = eventGungu;
    }

    public String getEventLocal() {
        return eventLocal;
    }

    public void setEventLocal(String eventLocal) {
        this.eventLocal = eventLocal;
    }

    public String getEventProgress() {
        return eventProgress;
    }

    public void setEventProgress(String eventProgress) {
        this.eventProgress = eventProgress;
    }

    public String getEventSido() {
        return eventSido;
    }

    public void setEventSido(String eventSido) {
        this.eventSido = eventSido;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventImgURL() {
        return eventImgURL;
    }

    public void setEventImgURL(String eventImgURL) {
        this.eventImgURL = eventImgURL;
    }

    public ArrayList<String> getEventSlideImage() {
        return eventSlideImage;
    }

    public void setEventSlideImage(ArrayList<String> eventSlideImage) {
        this.eventSlideImage = eventSlideImage;
    }

    public String getEventRemainDay() {
        return eventRemainDay;
    }

    public void setEventRemainDay(String eventRemainDay) {
        this.eventRemainDay = eventRemainDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(eventCategory);
        parcel.writeString(eventSido);
        parcel.writeString(eventGungu);
        parcel.writeString(eventTitle);
        parcel.writeString(eventDate);
        parcel.writeString(eventTime);
        parcel.writeString(eventLocal);
        parcel.writeString(eventProgress);
        parcel.writeString(eventRemainDay);
        parcel.writeString(eventImgURL);
        parcel.writeStringList(eventSlideImage);
    }

    private void readFromParcel(Parcel parcel) {
        code = parcel.readString();
        eventCategory = parcel.readString();
        eventSido = parcel.readString();
        eventGungu = parcel.readString();
        eventTitle = parcel.readString();
        eventDate = parcel.readString();
        eventTime = parcel.readString();
        eventLocal = parcel.readString();
        eventProgress = parcel.readString();
        eventRemainDay = parcel.readString();
        eventImgURL = parcel.readString();
        //eventSlideImage = parcel.createStringArrayList();
        parcel.writeStringList(eventSlideImage);
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public EventInfoData createFromParcel(Parcel parcel) {
            return new EventInfoData(parcel);
        }

        @Override
        public EventInfoData[] newArray(int size) {
            return new EventInfoData[size];
        }
    };
}
