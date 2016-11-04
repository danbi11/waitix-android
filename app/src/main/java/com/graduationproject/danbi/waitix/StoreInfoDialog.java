package com.graduationproject.danbi.waitix;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by danbi_000 on 2016-11-04.
 */

public class StoreInfoDialog extends Dialog{
    private RelativeLayout btnExit;

    public StoreInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_store_info);

        btnExit = (RelativeLayout)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
}
