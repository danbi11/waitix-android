package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import com.graduationproject.danbi.waitix.anim.CloseAnimation;
import com.graduationproject.danbi.waitix.anim.ExpandAnimation;
import com.graduationproject.danbi.waitix.nfc.AfterNfcRead;
import com.graduationproject.danbi.waitix.nfc.NdefRead;
import com.graduationproject.danbi.waitix.nfc.Tools;

public class MainActivity extends Activity implements View.OnClickListener {

    /* slide menu */
    private DisplayMetrics metrics;
    private LinearLayout ll_mainLayout;
    private LinearLayout ll_menuLayout;
    private FrameLayout.LayoutParams mainLayoutPrams;
    private FrameLayout.LayoutParams leftMenuLayoutPrams;
    private int leftMenuWidth;
    private static boolean isLeftExpanded;
    private RelativeLayout btn_menu;
    private TextView btn_pastWaitingList;

    /* NFC */
    private static final String MIMETYPE = "text/plain";
    private NfcAdapter nfcAdapter;

    /*  */
    private TextView tv_waitingNum;
    ImageView iv_cancel;

    IssueCancelDialog issueCancelDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSildeMenu();


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(Tools.checkNFC(nfcAdapter)) {
            intentHandler(getIntent());
        } else {
            Tools.displayToast(this, "대기표를 발급받기 위해서는\nNFC 연결이 필요합니다.");
        }

        tv_waitingNum = (TextView)findViewById(R.id.tv_waitingNum) ;
        iv_cancel = (ImageView)findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(this);
    }

    private void initSildeMenu() {

        // init left menu width
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        leftMenuWidth = (int) ((metrics.widthPixels) * 0.75);

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

        btn_pastWaitingList = (TextView) findViewById(R.id.btn_pastWaitingList);
        btn_pastWaitingList.setOnClickListener(this);


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
                    Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);

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
                    TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
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
                    public void afterRead() {
                        Intent intentToMain = new Intent(getApplicationContext(), NfcInputActivity.class);
                        startActivity(intentToMain);
                    }
                });
                ndefRead.execute(Ndef.get(nfcTag));
                tv_waitingNum.setText("");

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

                /*
                new AlertDialog.Builder(this)
//                        .setTitle("히든목록") //팝업창 타이틀바
                        .setMessage("정말 취소하시겠습니까?")  //팝업창 내용
                        .setNeutralButton("닫기",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                //닫기 버튼을 누르면 아무것도 안하고 닫기 때문에 그냥 비움
                            }
                        }).show(); // 팝업창 보여줌*/
                break;

            case R.id.btn_pastWaitingList:
                Intent intentToWaitingList = new Intent(getApplicationContext(), WaitingListActivity.class);
                isLeftExpanded = false;
                finish();
                startActivity(intentToWaitingList);
                break;


        }
    }
    @Override
    public void onBackPressed() {
        if (isLeftExpanded){
            menuLeftSlideAnimationToggle();
        }else {
            Toast.makeText(this, "WAITIX를 종료합니다.", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }
}
