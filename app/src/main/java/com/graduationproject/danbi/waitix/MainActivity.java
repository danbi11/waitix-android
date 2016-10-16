package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.graduationproject.danbi.waitix.anim.CloseAnimation;
import com.graduationproject.danbi.waitix.anim.ExpandAnimation;

public class MainActivity extends Activity implements View.OnClickListener {
    /* slide menu */
    private DisplayMetrics metrics;
    private LinearLayout ll_mainLayout;
    private LinearLayout ll_menuLayout;
    private FrameLayout.LayoutParams mainLayoutPrams;
    private FrameLayout.LayoutParams leftMenuLayoutPrams;
    private int leftMenuWidth;
    private static boolean isLeftExpanded;
    private Button bt_left, btn1, btn2, btn3, btn4;
    private ImageView btn_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSildeMenu();
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
        btn_menu = (ImageView) findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);

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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_menu:
                menuLeftSlideAnimationToggle();
                break;

        }
    }
}