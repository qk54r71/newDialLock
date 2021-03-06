package com.diallock.diallock.diallock.Activity.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.VolleyNetwork;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayoutPassword;
import com.diallock.diallock.diallock.Activity.Layout.DialLayout;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ogaclejapan.arclayout.ArcLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordChangeActivity extends AppCompatActivity {

    private ArcLayout dial_password_change;

    private String passNumber;
    private SimpleDraweeView pass_btn_cancle;
    private SimpleDraweeView pass_btn_ok;
    private TextView pass_txt_lock;
    private Boolean passProgress;
    private String strSwitch;
    private Boolean backFlag;

    private SimpleDraweeView pass_change_btn_index_00;
    private SimpleDraweeView pass_change_btn_index_01;
    private SimpleDraweeView pass_change_btn_index_02;
    private SimpleDraweeView pass_change_btn_index_03;
    private SimpleDraweeView pass_change_btn_index_04;
    private SimpleDraweeView pass_change_btn_index_05;
    private SimpleDraweeView pass_change_btn_index_06;
    private SimpleDraweeView pass_change_btn_index_07;
    private SimpleDraweeView pass_change_btn_index_08;
    private SimpleDraweeView pass_change_btn_index_09;

    private final static String LOG_NAME = "PasswordChangeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(PasswordChangeActivity.this);
        setContentView(R.layout.activity_password_change);

        CommonJava.Loging.i(getLocalClassName(), "onCreate()");

        setFindView();
        init();
        setOnClick();

    }

    private void setFindView() {
        dial_password_change = (ArcLayout) findViewById(R.id.dial_password_change);
        pass_btn_cancle = (SimpleDraweeView) findViewById(R.id.pass_btn_cancle);
        pass_btn_ok = (SimpleDraweeView) findViewById(R.id.pass_btn_ok);
        pass_txt_lock = (TextView) findViewById(R.id.pass_txt_lock);

        pass_change_btn_index_00 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_00);
        pass_change_btn_index_01 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_01);
        pass_change_btn_index_02 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_02);
        pass_change_btn_index_03 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_03);
        pass_change_btn_index_04 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_04);
        pass_change_btn_index_05 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_05);
        pass_change_btn_index_06 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_06);
        pass_change_btn_index_07 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_07);
        pass_change_btn_index_08 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_08);
        pass_change_btn_index_09 = (SimpleDraweeView) findViewById(R.id.pass_change_btn_index_09);
    }

    private void init() {
        passNumber = null;
        passProgress = false;
        backFlag = false;
        strSwitch = getIntent().getStringExtra("strSwitch");

        switch (strSwitch) {
            case "veryfirst":
                pass_btn_ok.setEnabled(false);
                break;
            case "verySecond":
                strSwitch = "verySecond";
                break;
            case "first":
                strSwitch = "first";
                break;
            case "second":
                strSwitch = "second";
                break;
        }

        pass_change_btn_index_00.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_0);
        pass_change_btn_index_01.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_1);
        pass_change_btn_index_02.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_2);
        pass_change_btn_index_03.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_3);
        pass_change_btn_index_04.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_4);
        pass_change_btn_index_05.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_5);
        pass_change_btn_index_06.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_6);
        pass_change_btn_index_07.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_7);
        pass_change_btn_index_08.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_8);
        pass_change_btn_index_09.getHierarchy().setPlaceholderImage(R.drawable.pw_change_selector_btn_num_9);

        pass_btn_cancle.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_cancle);
        pass_btn_ok.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_confirm);

    }

    private void setOnClick() {
        pass_btn_cancle.setOnClickListener(onClickListener);
        pass_btn_ok.setOnClickListener(onClickListener);

        pass_change_btn_index_00.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_01.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_02.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_03.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_04.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_05.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_06.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_07.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_08.setOnClickListener(onClickListenerBtnPasswordNumber);
        pass_change_btn_index_09.setOnClickListener(onClickListenerBtnPasswordNumber);
    }

    private View.OnClickListener onClickListenerBtnPasswordNumber = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            CommonJava.Loging.i(LOG_NAME, "onClickListenerBtnPasswordNumber()");
            CommonJava.Loging.i(LOG_NAME, "view.getId() : " + view.getId());

            String btnNumberStr = null;
            switch (view.getId()) {
                case R.id.pass_change_btn_index_00:
                    btnNumberStr = "0";
                    break;
                case R.id.pass_change_btn_index_01:
                    btnNumberStr = "1";
                    break;
                case R.id.pass_change_btn_index_02:
                    btnNumberStr = "2";
                    break;
                case R.id.pass_change_btn_index_03:
                    btnNumberStr = "3";
                    break;
                case R.id.pass_change_btn_index_04:
                    btnNumberStr = "4";
                    break;
                case R.id.pass_change_btn_index_05:
                    btnNumberStr = "5";
                    break;
                case R.id.pass_change_btn_index_06:
                    btnNumberStr = "6";
                    break;
                case R.id.pass_change_btn_index_07:
                    btnNumberStr = "7";
                    break;
                case R.id.pass_change_btn_index_08:
                    btnNumberStr = "8";
                    break;
                case R.id.pass_change_btn_index_09:
                    btnNumberStr = "9";
                    break;
            }
            onBtnClick(btnNumberStr);
            isVibrator();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            CommonJava.Loging.i(LOG_NAME, "onClick()");
            CommonJava.Loging.i(LOG_NAME, "view.getId() : " + view.getId());

            switch (view.getId()) {
                case R.id.pass_btn_cancle:

                    if (passProgress) {

                        passProgress = false;

                        passNumber = null;
                        pass_txt_lock.setText(passNumber);
                        pass_btn_cancle.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_cancle);
                        pass_btn_ok.setEnabled(false);
                    } else {
                        finish();
                    }

                    break;
                case R.id.pass_btn_ok:

                    CommonJava.Loging.i(getLocalClassName(), "pass_btn_ok()");
                    CommonJava.Loging.i(getLocalClassName(), "passProgress : " + passProgress);
                    CommonJava.Loging.i(getLocalClassName(), "isPasswordLangth() : " + isPasswordLangth());

                    if (passProgress && isPasswordLangth()) {
                        switch (strSwitch) {
                            case "veryfirst":
                                startSecondActivty("verySecond");
                                break;
                            case "first":
                                startSecondActivty("second");
                                break;
                            case "verySecond":
                                if (isPasswordSame()) {
                                    networkConnect("verySecond");
                                } else {
                                    Toast.makeText(getApplicationContext(), "전 단계의 패스워드와 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "second":
                                if (isPasswordSame()) {
                                    networkConnect("second");
                                } else {
                                    Toast.makeText(getApplicationContext(), "전 단계의 패스워드와 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }

                    break;
            }
        }
    };

    private void startSecondActivty(String strSwitch) {

        Intent intentSettingFirst = new Intent(PasswordChangeActivity.this, PasswordChangeActivity.class);
        intentSettingFirst.putExtra("strSwitch", strSwitch);

        String strPasswordFirst = (String) pass_txt_lock.getText();
        intentSettingFirst.putExtra("password", strPasswordFirst);

        startActivity(intentSettingFirst);

        Toast.makeText(getApplicationContext(), "한번 더 패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();

        finish();
    }

    /**
     * 패스워드 길이 조절
     *
     * @return
     */
    private Boolean isPasswordLangth() {

        String strPassword = (String) pass_txt_lock.getText();
        int passwordLangth = strPassword.length();

        int minimumPass = 2;
        int maximumPass = 4;

        if (passwordLangth >= minimumPass && passwordLangth <= maximumPass) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "비밀번호는 2~4자리로 만들어주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    /**
     * 전 단계의 패스워드와 현재 패스워드가 맞는지 확인하는 함수
     */
    private Boolean isPasswordSame() {

        String strPasswordFirst = getIntent().getStringExtra("password");
        String strPasswordSecond = (String) pass_txt_lock.getText();

        if (strPasswordFirst.equals(strPasswordSecond)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 눌린 값을 받는 함수
     *
     * @param btnValue : 현재 눌린 값
     */
    public void onBtnClick(String btnValue) {
        CommonJava.Loging.i("PasswordChange", "btnValue : " + btnValue);
        passProgress = true;
        if (passNumber != null) {
            passNumber += btnValue;
        } else {
            passNumber = btnValue;
        }
        pass_txt_lock.setText(passNumber);

        pass_btn_cancle.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_retry);
        pass_btn_ok.setEnabled(true);

    }


    /**
     * 뒤로가기 버튼 클릭 시 종료
     */
    @Override
    public void onBackPressed() {

        CommonJava.Loging.i("MainActivity", "onBackPressed()");


        if (backFlag) {

            CommonJava.Loging.i("MainActivity", "onBackPressed() : 종료");

            ActivityCompat.finishAffinity(this);
            System.exit(0);
            finish();
        } else {
            backFlag = true;

            Toast.makeText(PasswordChangeActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Handler han = new Handler();
            han.postDelayed(new Runnable() {

                @Override
                public void run() {
                    backFlag = false;
                }
            }, 2000);
        }

    }

    private void networkConnect(String strSwitch) {
        CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest networkConnect");

        JSONObject jsonObject = new JSONObject();
        String strURL = null;
        try {
            if (strSwitch.equals("verySecond")) {
                String strGoogleEmail = CommonJava.getGmail(PasswordChangeActivity.this);
                String strPhoneNumber = CommonJava.getPhoneNumber(PasswordChangeActivity.this);
                String strIMEI = CommonJava.getIMEI(PasswordChangeActivity.this);
                String strPasswordSecond = (String) pass_txt_lock.getText();
                CommonJava.Loging.i(LOG_NAME, "networkConnect strGoogleEmail : " + strGoogleEmail);
                CommonJava.Loging.i(LOG_NAME, "networkConnect strPhoneNumber : " + strPhoneNumber);
                CommonJava.Loging.i(LOG_NAME, "networkConnect strIMEI : " + strIMEI);

                jsonObject.put("googleEmail", strGoogleEmail);
                jsonObject.put("phoneNumber", strPhoneNumber);
                jsonObject.put("IMEI_IDX", strIMEI);
                jsonObject.put("password", strPasswordSecond);
                jsonObject.put("code", "1000");

                strURL = "http://molppangmy.cafe24.com/APPAPI/memberAccession";

            } else if (strSwitch.equals("second")) {
                String strMemberIdx = CommonJava.loadSharedPreferences(PasswordChangeActivity.this, "memberIdx");
                String strPasswordSecond = (String) pass_txt_lock.getText();

                CommonJava.Loging.i(LOG_NAME, "networkConnect strMemberIdx : " + strMemberIdx);
                CommonJava.Loging.i(LOG_NAME, "networkConnect strPasswordSecond : " + strPasswordSecond);

                jsonObject.put("member_idx", strMemberIdx);
                jsonObject.put("changePassword", strPasswordSecond);
                jsonObject.put("code", "1000");

                strURL = "http://molppangmy.cafe24.com/APPAPI/passwordChange";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest jsonObject : " + jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, strURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse :" + response);

                String strMemberIdx = null;
                String strCode = null;
                String strPassword = null;

                try {
                    CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse code :" + response.get("code"));

                    strCode = response.getString("code");

                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonJava.Loging.e(LOG_NAME, "jsonObjectRequest JSONException :" + e.toString());
                }

                switch (strCode) {
                    case "1000":

                        try {

                            CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse password :" + response.get("password"));
                            CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse member_idx :" + response.get("member_idx"));

                            strMemberIdx = response.getString("member_idx");
                            strPassword = response.getString("password");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonJava.Loging.e(LOG_NAME, "jsonObjectRequest JSONException :" + e.toString());
                        }


                        CommonJava.saveSharedPreferences(PasswordChangeActivity.this, "password", strPassword);

                        String email = CommonJava.getGmail(PasswordChangeActivity.this);
                        CommonJava.saveSharedPreferences(PasswordChangeActivity.this, "email", email);

                        CommonJava.saveSharedPreferences(PasswordChangeActivity.this, "memberIdx", strMemberIdx);

                        Intent intentSettingSecond = new Intent(PasswordChangeActivity.this, SettingActivity.class);
                        startActivity(intentSettingSecond);
                        finish();

                        Toast.makeText(getApplicationContext(), "패스워드 설정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case "4000":
                        try {

                            CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse changePassword :" + response.get("changePassword"));
                            CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse member_idx :" + response.get("member_idx"));

                            strMemberIdx = response.getString("member_idx");
                            strPassword = response.getString("changePassword");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonJava.Loging.e(LOG_NAME, "jsonObjectRequest JSONException :" + e.toString());
                        }


                        CommonJava.saveSharedPreferences(PasswordChangeActivity.this, "password", strPassword);
                        CommonJava.saveSharedPreferences(PasswordChangeActivity.this, "memberIdx", strMemberIdx);

                        Intent intentPasswordChangeSettingSecond = new Intent(PasswordChangeActivity.this, SettingActivity.class);
                        startActivity(intentPasswordChangeSettingSecond);
                        finish();

                        Toast.makeText(getApplicationContext(), "패스워드 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonJava.Loging.e(LOG_NAME, "jsonObjectRequest VolleyError :" + error);
            }
        });
        VolleyNetwork.CustomVolleyRequestQueue.getInstance(PasswordChangeActivity.this).getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * 진동 함수
     */
    private void isVibrator() {
        Vibrator vibrator = (Vibrator) PasswordChangeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }
}
