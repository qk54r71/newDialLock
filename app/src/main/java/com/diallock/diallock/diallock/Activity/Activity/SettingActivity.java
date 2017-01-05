package com.diallock.diallock.diallock.Activity.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.taskAction.ScreenService;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class SettingActivity extends AppCompatActivity {

    private SimpleDraweeView image_lock;
    private SimpleDraweeView image_unlock;
    private SimpleDraweeView image_password_change;
    private SimpleDraweeView image_image_change;
    private SimpleDraweeView image_email_change;
    private SimpleDraweeView image_ad;
    private SimpleDraweeView image_call;
    private SimpleDraweeView image_app_more;

    private Boolean lockCheck;
    private Boolean backFlag;

    final int REQ_CODE_SELECT_IMAGE = 100;
    private final static int PERMISSIONS_REQ_NUM = 6242;

    //private DBManageMent dbManageMent;

    private static final String LOG_NAME = "SettingActivty";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(SettingActivity.this);
        setContentView(R.layout.activity_setting);

        Boolean isAuthorityCheck = isAuthorityCheck();
        if (isAuthorityCheck) {
            CommonJava.Loging.i(LOG_NAME, "isAuthorityCheck : " + isAuthorityCheck);
            loadPassword();
        }

        setFindView();
        init();
        setOnClick();

        //networkConnect();//네트워크 테스트
        //copyExcelDataToDatabase();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 저장된 패스워드가 없으면 패드워드 설정 창으로 이동
     */
    private void loadPassword() {
        String password = CommonJava.loadSharedPreferences(SettingActivity.this, "password");
        CommonJava.Loging.i(LOG_NAME, "password : " + password);
        if (password.isEmpty()) {

            Intent intentSetPassword = new Intent(SettingActivity.this, PasswordChangeActivity.class);
            intentSetPassword.putExtra("strSwitch", "veryfirst");
            startActivity(intentSetPassword);
            finish();
        }

    }

    /**
     * 마시멜로우 이상 버전에서 사용되는 권한 체크 하기
     */
    private Boolean isAuthorityCheck() {
        /**
         * 주소록 가져오기
         */
        //Boolean get_accounts_bl = ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.GET_ACCOUNTS);
        //CommonJava.Loging.i("SettingActivity", "get_accounts_bl : " + get_accounts_bl);
        int checkGetAccounts = 99;
        int checkReadExternalStorage = 99;
        int checkReadPhoneState = 99;
        Boolean checkOverlays = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkGetAccounts = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            checkReadExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            checkReadPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            checkOverlays = Settings.canDrawOverlays(this);

        }
        if (checkGetAccounts == -1) {
            CommonJava.Loging.i(LOG_NAME, "checkGetAccounts : " + checkGetAccounts);
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, 0);
            return false;
        } else if (checkReadExternalStorage == -1) {
            CommonJava.Loging.i(LOG_NAME, "checkReadExternalStorage : " + checkReadExternalStorage);
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return false;
        } else if (checkReadPhoneState == -1) {
            CommonJava.Loging.i(LOG_NAME, "checkReadPhoneState : " + checkReadPhoneState);
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);

        } else if (!checkOverlays) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CommonJava.Loging.i(LOG_NAME, "checkOverlays : " + checkOverlays);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 6242);
            }
        }


        return true;

    }

    /**
     * 레이아웃과 연결
     */
    private void setFindView() {

        image_lock = (SimpleDraweeView) findViewById(R.id.image_lock);
        image_unlock = (SimpleDraweeView) findViewById(R.id.image_unlock);
        image_password_change = (SimpleDraweeView) findViewById(R.id.image_password_change);
        image_image_change = (SimpleDraweeView) findViewById(R.id.image_image_change);
        image_email_change = (SimpleDraweeView) findViewById(R.id.image_email_change);
        image_ad = (SimpleDraweeView) findViewById(R.id.image_ad);
        image_call = (SimpleDraweeView) findViewById(R.id.image_call);
        image_app_more = (SimpleDraweeView) findViewById(R.id.image_app_more);

    }

    /**
     * 상태값 초기화
     */
    private void init() {
        CommonJava.Loging.i(LOG_NAME, "init()");

        String strLockCheck = CommonJava.loadSharedPreferences(SettingActivity.this, "lockCheck");
        CommonJava.Loging.i(LOG_NAME, "strLockCheck : " + strLockCheck);

        switch (strLockCheck) {
            case "true":
                lockCheck = true;

                image_lock.setSelected(true);
                image_unlock.setSelected(false);
                break;
            case "false":
                lockCheck = false;

                image_lock.setSelected(false);
                image_unlock.setSelected(true);
                break;
            default:
                lockCheck = false;
                image_lock.setSelected(false);
                image_unlock.setSelected(true);
        }

        backFlag = false;

        image_lock.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_lock);
        image_unlock.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_unlock);
        image_password_change.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_password_change);
        image_image_change.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_image_change);
        image_email_change.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_email_change);
        image_ad.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_ad);
        image_call.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_call);
        image_app_more.getHierarchy().setPlaceholderImage(R.drawable.selector_btn_app_more);

    }

    /**
     * 클릭 이벤트 연결
     */
    private void setOnClick() {
        image_lock.setOnClickListener(onClickListener);
        image_unlock.setOnClickListener(onClickListener);
        image_password_change.setOnClickListener(onClickListener);
        image_image_change.setOnClickListener(onClickListener);
        image_email_change.setOnClickListener(onClickListener);
        image_ad.setOnClickListener(onClickListener);
        image_call.setOnClickListener(onClickListener);
        image_app_more.setOnClickListener(onClickListener);
    }

    /**
     * 클릭 이벤트 설정
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.image_lock:
                    String password = CommonJava.loadSharedPreferences(SettingActivity.this, "password");
                    if (password.isEmpty()) {
                        loadPassword();
                    } else if (lockCheck == false) {

                        image_lock.setSelected(true);
                        image_unlock.setSelected(false);

                        Intent intentLockScreen = new Intent(SettingActivity.this, LockScreenActivity.class);
                        intentLockScreen.putExtra("strSwitch", "SettingActivity");
                        startActivity(intentLockScreen);
                        /*Intent intentLockScreenView = new Intent(SettingActivity.this, LockScreenViewActivity.class);
                        intentLockScreenView.putExtra("strSwitch", "SettingActivity");
                        startActivity(intentLockScreenView);*/
                        //lockCheck = true;
                    }

                    break;
                case R.id.image_unlock:

                    if (lockCheck == true) {
                        CommonJava.saveSharedPreferences(SettingActivity.this, "lockCheck", "false");
                        image_lock.setSelected(false);
                        image_unlock.setSelected(true);
                        lockCheck = false;

                        Intent intentStopService = new Intent(SettingActivity.this, ScreenService.class);
                        stopService(intentStopService);

                    }

                    break;
                case R.id.image_password_change:

                    Intent intentPassChange = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                    intentPassChange.putExtra("strSwitch", "first");
                    startActivity(intentPassChange);

                    break;
                case R.id.image_image_change:

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

                    break;
                case R.id.image_email_change:

                    Intent intentEmailChange = new Intent(SettingActivity.this, EmailChangeActivity.class);
                    startActivity(intentEmailChange);

                    break;
                case R.id.image_ad:

                    Intent intentAdRequest = new Intent(SettingActivity.this, AdRequestActivity.class);
                    startActivity(intentAdRequest);

                    break;
                case R.id.image_call:

                    Intent intentTestDialLayout = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(intentTestDialLayout);
                    break;
                case R.id.image_app_more:
                    break;
            }

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        CommonJava.Loging.i(LOG_NAME, "requestCode : " + requestCode);
        CommonJava.Loging.i(LOG_NAME, "permissions : " + permissions[0]);
        CommonJava.Loging.i(LOG_NAME, "grantResults : " + grantResults[0]);

        if (grantResults[0] != -1) {

            int checkReadExternalStorage = 99;
            int checkReadPhoneState = 99;
            Boolean checkOverlays = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkReadExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                checkReadPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
                checkOverlays = Settings.canDrawOverlays(this);
            }
            if (checkReadExternalStorage == -1) {
                CommonJava.Loging.i(LOG_NAME, "checkReadExternalStorage : " + checkReadExternalStorage);
                ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else if (checkReadPhoneState == -1) {
                CommonJava.Loging.i(LOG_NAME, "checkReadPhoneState : " + checkReadPhoneState);
                ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            } else if (!checkOverlays) {
                CommonJava.Loging.i(LOG_NAME, "checkOverlays : " + checkOverlays);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSIONS_REQ_NUM);
            }

        } else {
            finish();
        }


    }

    /**
     * 뒤로가기 버튼 클릭 시 종료
     */
    @Override
    public void onBackPressed() {

        CommonJava.Loging.i(LOG_NAME, "onBackPressed()");

        if (backFlag) {

            CommonJava.Loging.i(LOG_NAME, "onBackPressed() : 종료");

            finish();
        } else {
            backFlag = true;

            Toast.makeText(SettingActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Handler han = new Handler();
            han.postDelayed(new Runnable() {

                @Override
                public void run() {
                    backFlag = false;
                }
            }, 2000);
        }

    }

    /**
     * 갤러리에서 선택된 이미지 가져오기
     * 출처 {Link :http://ankyu.entersoft.kr/Lecture/android/gallery_01.asp}
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        //Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();
        CommonJava.Loging.i(LOG_NAME, "onActivityResult resultCode :" + resultCode);

        switch (requestCode) {
            case REQ_CODE_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                /* // 원본
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.imageView1);

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();



                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                    String strUrl = String.valueOf(data.getData());
                    CommonJava.saveSharedPreferences(SettingActivity.this, "imgUrl", strUrl);
                    CommonJava.Loging.i(LOG_NAME, "onActivityResult strUrl : " + strUrl);


                } else if (resultCode == Activity.RESULT_CANCELED) {

                    CommonJava.saveSharedPreferences(SettingActivity.this, "imgUrl", null);
                    CommonJava.Loging.i(LOG_NAME, "onActivityResult strUrl null ");
                }
                break;
            case PERMISSIONS_REQ_NUM:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        finish();
                    } else {
                        loadPassword();
                    }
                }
                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CommonJava.Loging.i(LOG_NAME, "onRestart()");

        /*lockCheck = Boolean.valueOf(CommonJava.loadSharedPreferences(SettingActivity.this, "lockCheck"));
        CommonJava.Loging.i(getLocalClassName(), "lockCheck : " + lockCheck);

        if (lockCheck) {
            linear_lock.setBackgroundResource(R.drawable.btn_click);
            linear_unlock.setBackgroundResource(R.drawable.btn_bg);

            Intent intentStopService = new Intent(SettingActivity.this, ScreenService.class);
            stopService(intentStopService);
            System.exit(0);
        } else {
            linear_lock.setBackgroundResource(R.drawable.btn_bg);
            linear_unlock.setBackgroundResource(R.drawable.btn_click);
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonJava.Loging.i(LOG_NAME, "onResume()");

        lockCheck = Boolean.valueOf(CommonJava.loadSharedPreferences(SettingActivity.this, "lockCheck"));
        CommonJava.Loging.i(LOG_NAME, "lockCheck : " + lockCheck);

        if (lockCheck) {
            image_lock.setSelected(true);
            image_unlock.setSelected(false);
        } else {
            image_lock.setSelected(false);
            image_unlock.setSelected(true);
        }


    }

  /*  */

    /**
     * assets 폴더에 존재하는 엑셀 파일을 db 에 넣음
     *//*
    private void copyExcelDataToDatabase() {

        dbManageMent = new DBManageMent(SettingActivity.this);
        dbManageMent.delete();

        CommonJava.Loging.i(LOG_NAME, "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("festival2.xls");

            try {
                workbook = workbook.getWorkbook(is);

                if (workbook != null) {
                    sheet = workbook.getSheet(0);

                    if (sheet != null) {

                        int nMaxColumn = 7;
                        int nRowStartIndex = 1;
                        int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                        int nColumnStartIndex = 0;
                        int nColumnEndIndex = sheet.getRow(2).length - 1;
                        dbManageMent.open();

                        for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {

                            //String no = sheet.getCell(nColumnStartIndex, nRow).getContents();
                            String si = null;
                            String gu = null;
                            String title = null;
                            String day_start = null;
                            String day_end = null;
                            String local = null;


                            for (int nColumn = nColumnStartIndex; nColumn <= nColumnEndIndex; nColumn++) {
                                si = sheet.getCell(1, nRow).getContents();
                                gu = sheet.getCell(2, nRow).getContents();
                                title = sheet.getCell(3, nRow).getContents();
                                String strDayStart = sheet.getCell(4, nRow).getContents();
                                day_start = strDayStart.replace(".", "-");
                                String strDayEnd = sheet.getCell(5, nRow).getContents();
                                day_end = strDayEnd.replace(".", "-");
                                local = sheet.getCell(6, nRow).getContents();

                            }

                            CommonJava.Loging.i(LOG_NAME, "si : " + si + " gu : " + gu + " title : " + title + " day_start : " + day_start + " day_end : " + day_end + " local : " + local);

                            dbManageMent.createNote(si, gu, title, day_start, day_end, local);
                        }

                    } else {
                        CommonJava.Loging.e(LOG_NAME, "sheet is null");
                    }
                } else {
                    CommonJava.Loging.e(LOG_NAME, "workbook is null");
                }

            } catch (BiffException e) {
                e.printStackTrace();
                CommonJava.Loging.e(LOG_NAME, "Error : " + e.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            CommonJava.Loging.e(LOG_NAME, "Error : " + e.toString());
        }

        if (workbook != null) {
            workbook.close();
        }

    }*/
    @Override
    protected void onDestroy() {
        //dbManageMent.close();
        super.onDestroy();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Setting Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
