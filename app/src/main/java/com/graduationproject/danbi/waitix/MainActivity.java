package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.graduationproject.danbi.waitix.anim.CloseAnimation;
import com.graduationproject.danbi.waitix.anim.ExpandAnimation;
import com.graduationproject.danbi.waitix.nfc.AfterNfcRead;
import com.graduationproject.danbi.waitix.nfc.NdefRead;
import com.graduationproject.danbi.waitix.nfc.Tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends Activity implements View.OnClickListener {

    /* slide menu */
    private DisplayMetrics metrics;
    private LinearLayout ll_mainLayout;
    private LinearLayout ll_menuLayout;
    private FrameLayout.LayoutParams mainLayoutPrams;
    private FrameLayout.LayoutParams leftMenuLayoutPrams;
    private int leftMenuWidth;
    private static boolean isLeftExpanded;
    private RelativeLayout btn_menu, btnInfo, btn_waiting, btn_pastWaitingList,btn_storeList, btn_menual, btn_intro;

    /* NFC */
    private static final String MIMETYPE = "text/plain";
    private NfcAdapter nfcAdapter;



    /* etc */
    public TextView tv_waitingNum, tv_storeName, tv_time, tv_waitingTeam, tv_waitingTime;
    ImageView iv_cancel;
    private BackPressCloseHandler backPressCloseHandler; //뒤로가기 두번눌러종료
    public static boolean isFirstEnter = true;  //처음만 splash 화면 나오기
//    final String snum = null;

    IssueCancelDialog issueCancelDialog;
    StoreInfoDialog storeInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* splash 화면 띄우기 */
        if (isFirstEnter){
            startActivity(new Intent(this, SplashActivity.class));
            isFirstEnter = false;
        }


        /* 슬라이딩 메뉴 */
        initSildeMenu();


        /* 뒤로가기 두번눌러종료 */
        backPressCloseHandler = new BackPressCloseHandler(this);


        /* uuid 생성 */
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        String unum = sharedPreferences.getString("unum", null);
        if (unum == null) {
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String deviceId = deviceUuid.toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("unum", deviceId);
            editor.commit();

            ServerNetworkManager serverRequest = ServerNetworkManager.newInstance();
            List<Pair<String, ?>> parameters = new ArrayList<Pair<String, ?>>();
            parameters.add(Pair.create("unum", deviceId));
            serverRequest.get("user_register.php", parameters, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String body = response.body().string();
                                Log.d("response", body);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                response.close();
                            }
                        }
                    });
                }
            });
        }


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(Tools.checkNFC(nfcAdapter)) {
            intentHandler(getIntent());
        } else {
            Tools.displayToast(this, "대기표를 발급받기 위해서는\nNFC 연결이 필요합니다.");
        }


        tv_storeName = (TextView)findViewById(R.id.tv_storeName);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_waitingTime = (TextView) findViewById(R.id.tv_waitingTime);
        tv_waitingTeam = (TextView) findViewById(R.id.tv_waitingTeam);

/*
        Bundle bundle = getIntent().getExtras();
        String storeName = bundle.getString("storeName", null);
        String pon = bundle.getString("pon", null);
        String date = bundle.getString("date", null);
        String waitpon = bundle.getString("waitpon", null);
        String waittime = bundle.getString("waittime",null);

        tv_storeName.setText(storeName);
        tv_time.setText(date);
        tv_waitingTeam.setText(waitpon);
        tv_waitingTime.setText(waittime);
*/



        tv_waitingNum = (TextView)findViewById(R.id.tv_waitingNum);
        iv_cancel = (ImageView)findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(this);
        btnInfo = (RelativeLayout)findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(this);
    }

    private void initSildeMenu() {

        // init left menu width
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        leftMenuWidth = (int) ((metrics.widthPixels) * 0.85);

        // init main view
        ll_mainLayout = (LinearLayout) findViewById(R.id.ll_mainlayout);
        mainLayoutPrams = (FrameLayout.LayoutParams) ll_mainLayout
                .getLayoutParams();
        mainLayoutPrams.width = metrics.widthPixels;
        ll_mainLayout.setLayoutParams(mainLayoutPrams);

        // init left menu
        ll_menuLayout = (LinearLayout) findViewById(R.id.ll_menuLayout);
        leftMenuLayoutPrams = (FrameLayout.LayoutParams) ll_menuLayout
                .getLayoutParams();
        leftMenuLayoutPrams.width = leftMenuWidth;
        ll_menuLayout.setLayoutParams(leftMenuLayoutPrams);

        // init ui
        btn_menu = (RelativeLayout) findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);

        btn_waiting = (RelativeLayout) findViewById(R.id.btn_waiting);
        btn_waiting.setOnClickListener(this);
        btn_pastWaitingList = (RelativeLayout) findViewById(R.id.btn_pastWaitingList);
        btn_pastWaitingList.setOnClickListener(this);
        btn_storeList = (RelativeLayout) findViewById(R.id.btn_storeList);
        btn_storeList.setOnClickListener(this);
        btn_menual = (RelativeLayout) findViewById(R.id.btn_menual);
        btn_menual.setOnClickListener(this);
        btn_intro = (RelativeLayout) findViewById(R.id.btn_intro);
        btn_intro.setOnClickListener(this);

    }

    /**
     * left menu toggle
     */
    private void menuLeftSlideAnimationToggle() {

        if (!isLeftExpanded) {

            // networkRequestTimeLineGetNewCnt();

            isLeftExpanded = true;
            ll_menuLayout.setVisibility(View.VISIBLE);

            // Expand
            new ExpandAnimation(ll_mainLayout, leftMenuWidth, "left",
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.85f, 0, 0.0f, 0, 0.0f);

            // disable all of main view
            FrameLayout viewGroup = (FrameLayout) findViewById(R.id.ll_fragment)
                    .getParent();
            enableDisableViewGroup(viewGroup, false);

            // enable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.VISIBLE);

            findViewById(R.id.ll_empty).setEnabled(true);
            findViewById(R.id.ll_empty).setOnTouchListener(
                    new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            menuLeftSlideAnimationToggle();
                            return true;
                        }
                    });

        } else {
            isLeftExpanded = false;

            // close
            new CloseAnimation(ll_mainLayout, leftMenuWidth,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.85f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);

            // enable all of main view
            FrameLayout viewGroup = (FrameLayout) findViewById(R.id.ll_fragment)
                    .getParent();
            enableDisableViewGroup(viewGroup, true);

            // disable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.GONE);
            findViewById(R.id.ll_empty).setEnabled(false);

        }
    }

    /**
     * 뷰의 동작을 제어한다. 하위 모든 뷰들이 enable 값으로 설정된다.
     *
     * @param viewGroup
     * @param enabled
     */
    public static void enableDisableViewGroup(ViewGroup viewGroup,
                                              boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view.getId() != R.id.btn_menu) {
                view.setEnabled(enabled);
                if (view instanceof ViewGroup) {
                    enableDisableViewGroup((ViewGroup) view, enabled);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.foregroundDispatchSetup(this, nfcAdapter, MIMETYPE, new String[][]{});
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        intentHandler(intent);
    }

    private void intentHandler(Intent intent) {
        String intentAction = intent.getAction();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intentAction) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentAction)) {
            if (MIMETYPE.equals(intent.getType())) {
                Tag nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefRead ndefRead = new NdefRead(getApplicationContext(), new AfterNfcRead() {
                    @Override
                    public void afterRead(final String snum) {

                        ServerNetworkManager serverNetworkManager = ServerNetworkManager.newInstance();

                        List<Pair<String, ?>> parameters = new ArrayList<Pair<String, ?>>();
                        parameters.add(Pair.create("snum", snum));

                        Toast.makeText(MainActivity.this, snum, Toast.LENGTH_SHORT).show(); //임시

                        serverNetworkManager.get("store_current.php", parameters, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
//                                Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String body = response.body().string();
                                            Log.d("response", body);

                                            body = body.replaceAll("\\s", "");
                                            body = body.trim();
                                            body = body.replaceAll("\uFEFF", "");

                                            Gson gson = new Gson();
                                            JsonReader reader = new JsonReader(new StringReader(body));
                                            reader.setLenient(true);
                                            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                                            JsonObject jsonObject2 = jsonObject.get("store").getAsJsonObject();
                                            String storeName = jsonObject2.get("store_name").getAsString();
                                            String totalPon = jsonObject2.get("total_pon").getAsString();
                                            String totalWaittime = jsonObject2.get("total_waittime").getAsString();

                                            Intent intentToNFC = new Intent(getApplicationContext(), NfcInputActivity.class);
                                            intentToNFC.putExtra("storeName", storeName);
                                            intentToNFC.putExtra("totalPon", totalPon);
                                            intentToNFC.putExtra("totalWaittime", totalWaittime);
                                            intentToNFC.putExtra("snum", snum);
                                            startActivity(intentToNFC);



                                            tv_storeName.setText(storeName);
                                            tv_waitingTeam.setText(String.format("%03d", Integer.parseInt(totalPon)) + "번");
                                            tv_waitingTime.setText(String.format("%03d", Integer.parseInt(totalPon)*3) + "분");
                                            tv_waitingNum.setText(String.format("%03d", Integer.parseInt(totalPon)));


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                        } finally {
                                            response.close();
                                        }
                                    }
                                });
                            }
                        });


                    }
                });
                ndefRead.execute(Ndef.get(nfcTag));
//                tv_waitingNum.setText("");

            } else {
                Tools.displayToast(getApplicationContext(), "Mime type error.");
            }
        }
    }
    public void textviewintent(String tv){
        tv_waitingNum.setText(tv);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_menu:
                menuLeftSlideAnimationToggle();
                break;
            case  R.id.iv_cancel:
                issueCancelDialog = new IssueCancelDialog(MainActivity.this);
                issueCancelDialog.show();

                tv_storeName.setText("발급받은 대기번호가 없습니다");
                tv_waitingNum.setText("000");
                tv_waitingTime.setText("000분");
                tv_waitingTeam.setText("000번");
                tv_time.setText("2000.00.00 00:00:00");


                /*
                new AlertDialog.Builder(this)
//                      .setTitle("히든목록") //팝업창 타이틀바
                        .setMessage("정말 취소하시겠습니까?")  //팝업창 내용
                        .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                //닫기 버튼을 누르면 아무것도 안하고 닫기 때문에 그냥 비움
                            }
                        }).show(); // 팝업창 보여줌 */
                break;

            case R.id.btnInfo:
                storeInfoDialog = new StoreInfoDialog(MainActivity.this);
                storeInfoDialog.show();
                break;

            case R.id.btn_waiting:
                Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
                isLeftExpanded = false;
                finish();
                startActivity(intentToMain);
                break;

            case R.id.btn_pastWaitingList:
                Intent intentToWaitingList = new Intent(getApplicationContext(), WaitingListActivity.class);
                isLeftExpanded = false;
                finish();
                startActivity(intentToWaitingList);
                break;
            case R.id.btn_storeList:
                Intent intentToStoreList = new Intent(getApplicationContext(), StoreListActivity.class);
                isLeftExpanded = false;
                finish();
                startActivity(intentToStoreList);
                break;

            case R.id.btn_menual:
                Intent intentToManual = new Intent(getApplicationContext(), ManualActivity.class);
                isLeftExpanded = false;
                startActivity(intentToManual);
                break;

            case R.id.btn_intro:
                Intent intentToIntro = new Intent(getApplicationContext(), IntroActivity.class);
                menuLeftSlideAnimationToggle();
                startActivity(intentToIntro);
                break;



        }
    }
    @Override
    public void onBackPressed() {
        if (isLeftExpanded){
            menuLeftSlideAnimationToggle();
        }else {
//            super.onBackPressed();
            backPressCloseHandler.onBackPressed();
        }
    }
}
