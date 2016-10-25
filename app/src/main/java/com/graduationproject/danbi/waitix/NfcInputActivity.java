package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by danbi_000 on 2016-10-18.
 */

public class NfcInputActivity extends Activity {
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcinput);
    }

}
