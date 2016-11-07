package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.graduationproject.danbi.waitix.anim.CloseAnimation;
import com.graduationproject.danbi.waitix.anim.ExpandAnimation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StoreListActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {

    /* slide menu */
    private DisplayMetrics metrics;
    private LinearLayout ll_mainLayout;
    private LinearLayout ll_menuLayout;
    private FrameLayout.LayoutParams mainLayoutPrams;
    private FrameLayout.LayoutParams leftMenuLayoutPrams;
    private int leftMenuWidth;
    private static boolean isLeftExpanded;
    private RelativeLayout btn_menu, btnInfo, btn_waiting, btn_pastWaitingList,btn_storeList, btn_menual, btn_intro;

    /* 뒤로가기 두번눌러종료 */
    private BackPressCloseHandler backPressCloseHandler;

    StoreInfoDialog storeInfoDialog;


    ArrayList<WaitingListData> datas= new ArrayList<WaitingListData>();
    ListView listview;

    int store_length = 0;
    String[] name, location, time, waitingNum;

    //UpdatePartnerShopInfo API
    String SNUM="", NAME="", LOCATION="", TIME="", WAITINGNUM="";
    String UPDATE_RESULT="", UPDATE_ERROR="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        /* 슬라이딩 메뉴 */
        initSildeMenu();

        /* 뒤로가기 두번눌러종료 */
        backPressCloseHandler = new BackPressCloseHandler(this);



        ServerNetworkManager serverNetworkManager = ServerNetworkManager.newInstance();

        List<Pair<String, ?>> parameters = new ArrayList<Pair<String, ?>>();
        parameters.add(Pair.create("snum", "38"));
        parameters.add(Pair.create("snum", "38"));
//
//        Toast.makeText(StoreListActivity.this, snum, Toast.LENGTH_SHORT).show(); //임시.

        serverNetworkManager.get("store_list.php", parameters, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("error","오류 발생 ");

//                Toast.makeText(StoreListActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show(); //이게 없으면 죽지않음
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String body = response.body().string();
                            Log.d("response", body);

                            Gson gson = new Gson();
                            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
                            JsonArray storearray = jsonObject.getAsJsonArray("stores");
                            store_length = storearray.size();
                            Log.e("size","array 갯수 : " + String.valueOf(store_length));

//                            JsonObject jsonObject2 = jsonObject.getAsJsonObject("store");
//                            String storeName = storearray.get("store_name").getAsString();
//                            String totalPon = storearray.get("total_pon").getAsString();
//                            String totalWaittime = storearray.get("total_waittime").getAsString();


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(StoreListActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        } finally {
                            response.close();
                        }
                    }
                });
            }
        });




        datas.add( new WaitingListData("알베르토 트로트로", "대한민국", "26", "006"));
        datas.add( new WaitingListData("샘오취리", "대한민국", "65", "016"));
        datas.add( new WaitingListData("알베르토 트로트로", "대한민국", "26", "006"));
        datas.add( new WaitingListData("샘오취리", "대한민국", "65", "016"));
        datas.add( new WaitingListData("알베르토 트로트로", "대한민국", "26", "006"));
        datas.add( new WaitingListData("샘오취리", "대한민국", "65", "016"));

        //ListView 객체 찾아와서 참조
        listview= (ListView)findViewById(R.id.listView_storelist);

        StoreListDataAdapter adapter= new StoreListDataAdapter( getLayoutInflater() , datas);

        //위에 만든 Adapter 객체를 AdapterView의 일종인 ListView에 설정.
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);


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

    /* left menu toggle */
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
    public void onBackPressed() {
        if (isLeftExpanded){
            menuLeftSlideAnimationToggle();
        }else {
            backPressCloseHandler.onBackPressed();
        }
    }

    /* 메뉴 클릭 처리 */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu:
                menuLeftSlideAnimationToggle();
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


    /* 리스트 항목 클릭했을 때 처리 */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        storeInfoDialog = new StoreInfoDialog(StoreListActivity.this);
        storeInfoDialog.show();

        TextView name = (TextView)view.findViewById(R.id.tv_storeName);
        String toastMessage = "매장이름 : " + name.getText()+ ". position : " + position;   //테스트
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }
}
