package com.diallock.diallock.diallock.Activity.ParkSDK.Data;

/**
 * Created by park on 2017-01-02.
 */

public class DialCircleInfo_Location {
    private int _xPosition;
    private int _yPosition;
    private int index;
    private int dialCircleRadius;

    public int get_xPosition() {
        return _xPosition;
    }

    public void set_xPosition(int _xPosition) {
        this._xPosition = _xPosition;
    }

    public int get_yPosition() {
        return _yPosition;
    }

    public void set_yPosition(int _yPosition) {
        this._yPosition = _yPosition;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getDialCircleRadius() {
        return dialCircleRadius;
    }

    public void setDialCircleRadius(int dialCircleRadius) {
        this.dialCircleRadius = dialCircleRadius;
    }
}
