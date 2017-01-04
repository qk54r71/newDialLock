package com.diallock.diallock.diallock.Activity.ParkSDK.Util;

import android.content.Context;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.ParkSDK.Data.DialCircleInfo_Location;
import com.diallock.diallock.diallock.Activity.ParkSDK.Debug.Loging;

import java.util.ArrayList;

/**
 * Created by park on 2017-01-03.
 * <p>
 * 원에 관련된 함수 모음
 */

public class Circle {

    private static final String LOG_NAME = "ParkSDK.Circle";

    /**
     * touch 된 좌표가 circle 안에 있는지 체크
     *
     * @param _CircleCenterX circle 의 중앙 X 값
     * @param _CircleCenterY circle 의 중앙 Y 값
     * @param _CircleRadius  circle 의 반지름
     * @param _TouchX        touch 된 X 값
     * @param _TouchY        touch 된 Y 값
     * @return touch 된 좌표 값이 내부이면 true, 아니면 false
     */
    public static Boolean _IsInnerCircleCheck(
            float _CircleCenterX,
            float _CircleCenterY,
            int _CircleRadius,
            float _TouchX,
            float _TouchY
    ) {
        Boolean resultBl = false;

        if (Math.pow((_CircleCenterX - _TouchX), 2) + Math.pow((_CircleCenterY - _TouchY), 2) < Math.pow(_CircleRadius, 2)) {
            resultBl = true;
        } else {
            resultBl = false;
        }

        return resultBl;
    }

    /**
     * touch 된 좌표 값이 주어진 child circle 중에서 어느 위치에 가까운지 찾아주는 함수
     *
     * @param _ChildList 거리를 비교할 child circle 의 List
     * @param _TouchX    touch 된 X 값
     * @param _TouchY    touch 된 Y 값
     * @return 가장 가까운 인덱스 값
     */
    public static Integer _TouchedIndex(
            ArrayList<DialCircleInfo_Location> _ChildList,
            float _TouchX,
            float _TouchY
    ) {
        Integer resultIndex = null;
        double currentDistance = 0;

        for (DialCircleInfo_Location dialCircleInfo_location : _ChildList) {
            float _CircleCenterX = dialCircleInfo_location.get_xPosition();
            float _CircleCenterY = dialCircleInfo_location.get_yPosition();
            //Loging.i(LOG_NAME, "_TouchedIndex  _CircleCenterX : " + _CircleCenterX + " _CircleCenterY : " + _CircleCenterY);

            double distance = Math.pow(Math.abs(_CircleCenterX - _TouchX), 2) + Math.pow(Math.abs(_CircleCenterY - _TouchY), 2);
            //Loging.i(LOG_NAME, "_TouchedIndex  distance : " + distance);
            if (currentDistance == 0) {
                currentDistance = distance;
                resultIndex = dialCircleInfo_location.getIndex();
            } else if (distance < currentDistance) {
                currentDistance = distance;
                resultIndex = dialCircleInfo_location.getIndex();
            }
        }

        return resultIndex;
    }

    /**
     * 허수 체크 함수
     *
     * @param context
     * @param strPassword 현재까지 입력된 password 값
     * @return 허수 적용이면 true , 아니면 false
     */
    public static Boolean _IsImaginaryCheck(Context context, String strPassword) {

        String loadPassword = CommonJava.loadSharedPreferences(context, "password");
        String strPass = strPassword;
        int minimumPass = 2;
        int maximumPass = loadPassword.length() * 2;

        if (strPassword.length() >= minimumPass && strPassword.length() <= maximumPass) {

            if (strPassword.contains(loadPassword)) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

}
