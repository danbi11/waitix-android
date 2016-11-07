package com.graduationproject.danbi.waitix;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by danbi_000 on 2016-10-28.
 */

public class IssueCancelDialog extends Dialog implements View.OnClickListener {
    private Button btnNo, btnYes;

    public IssueCancelDialog(Context context) {
        super(context);

//        LayoutInflater inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_main, null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_issue_cancel);

        btnNo = (Button)findViewById(R.id.btnNo);
        btnNo.setOnClickListener(this);
        btnYes = (Button)findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNo:
                cancel();
                break;
            case R.id.btnYes:
                Toast.makeText(getContext(),"발급이 취소되었습니다.",Toast.LENGTH_SHORT).show();
//
//                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View view = inflater.inflate(R.layout.activity_main, null);
////                Button btn = (Button)view.findViewById(R.id.tv_storeName);
//                TextView tv_storeName = (TextView)view.findViewById(R.id.tv_storeName);

//                tv_storeName.setText("발급받은 대기번호가 없습니다.");

                dismiss();
                break;
        }
    }
}
