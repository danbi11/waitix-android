package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class IntroActivity extends Activity {

    /* 뒤로가기 두번눌러종료 */
    private BackPressCloseHandler backPressCloseHandler;

    RelativeLayout btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        /* 뒤로가기 두번눌러종료 */
        backPressCloseHandler = new BackPressCloseHandler(this);

        btnExit = (RelativeLayout)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
