package com.diallock.diallock.diallock.Activity.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.VolleyNetwork;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailChangeActivity extends AppCompatActivity {

    private TextView txt_load_email;
    private EditText edt_new_email;
    private EditText edt_re_email;
    private Button btn_email_cancle;
    private Button btn_email_change;
    private Boolean backFlag;
    private static final String LOG_NAME = "EmailChangeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(EmailChangeActivity.this);
        setContentView(R.layout.activity_email_change);

        setFindView();
        init();
        setOnClick();
    }

    private void setFindView() {
        txt_load_email = (TextView) findViewById(R.id.txt_load_email);
        edt_new_email = (EditText) findViewById(R.id.edt_new_email);
        edt_re_email = (EditText) findViewById(R.id.edt_re_email);
        btn_email_cancle = (Button) findViewById(R.id.btn_email_cancle);
        btn_email_change = (Button) findViewById(R.id.btn_email_change);
    }

    private void init() {

        String email = CommonJava.loadSharedPreferences(EmailChangeActivity.this, "email");
        if (email == null || email.isEmpty()) {
            email = CommonJava.getGmail(EmailChangeActivity.this);
        }
        txt_load_email.setText(email);
        backFlag = false;

    }

    private void setOnClick() {
        btn_email_cancle.setOnClickListener(onClickListener);
        btn_email_change.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_email_cancle:
                    finish();
                    break;
                case R.id.btn_email_change:
                    if (isEmailSame()) {
                        networkConnect();
                    } else {
                        new Toast(EmailChangeActivity.this).makeText(EmailChangeActivity.this, "이메일 확인이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * 백업 이메일과 새 백업 이메일이 같은지 확인
     *
     * @return
     */
    private Boolean isEmailSame() {

        String strEdtNew = String.valueOf(edt_new_email.getText());
        String strEdtRe = String.valueOf(edt_re_email.getText());

        if (strEdtNew.equals(strEdtRe)) {
            return true;
        }

        return false;
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

            Toast.makeText(EmailChangeActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Handler han = new Handler();
            han.postDelayed(new Runnable() {

                @Override
                public void run() {
                    backFlag = false;
                }
            }, 2000);
        }

    }

    private void networkConnect() {
        CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest networkConnect");

        JSONObject jsonObject = new JSONObject();
        try {

            String strMemberIdx = CommonJava.loadSharedPreferences(EmailChangeActivity.this, "memberIdx");
            String strChangeEmail = String.valueOf(edt_new_email.getText());

            CommonJava.Loging.i(LOG_NAME, "networkConnect strMemberIdx : " + strMemberIdx);
            CommonJava.Loging.i(LOG_NAME, "networkConnect strChangeEmail : " + strChangeEmail);

            jsonObject.put("member_idx", strMemberIdx);
            jsonObject.put("changeEmail", strChangeEmail);
            jsonObject.put("code", "1000");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest jsonObject : " + jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://molppangmy.cafe24.com/APPAPI/emailChange", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse :" + response);

                String strCode = null;
                String strEmailChange = null;

                try {
                    CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse code :" + response.get("code"));
                    CommonJava.Loging.i(LOG_NAME, "jsonObjectRequest onResponse password :" + response.get("changeEmail"));

                    strCode = response.getString("code");
                    strEmailChange = response.getString("changeEmail");

                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonJava.Loging.e(LOG_NAME, "jsonObjectRequest JSONException :" + e.toString());
                }

                switch (strCode) {
                    case "1000":

                        CommonJava.saveSharedPreferences(EmailChangeActivity.this, "email", strEmailChange);

                        Intent intentSettingSecond = new Intent(EmailChangeActivity.this, SettingActivity.class);
                        startActivity(intentSettingSecond);
                        finish();

                        Toast.makeText(getApplicationContext(), "이메일 설정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonJava.Loging.e(LOG_NAME, "jsonObjectRequest VolleyError :" + error);
            }
        });
        VolleyNetwork.CustomVolleyRequestQueue.getInstance(EmailChangeActivity.this).getRequestQueue().add(jsonObjectRequest);
    }
}
