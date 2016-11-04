package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by danbi_000 on 2016-10-18.
 */

public class NfcInputActivity extends Activity implements View.OnClickListener{
    RelativeLayout btnExit, btnMinus, btnPlus;
    TextView tv_storeName,tv_pon, tv_waittingNum, tv_waittingTime;
    ImageView btnOk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcinput);

        btnExit = (RelativeLayout)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
        btnMinus = (RelativeLayout)findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(this);
        btnPlus = (RelativeLayout)findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(this);
        btnOk = (ImageView) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        tv_storeName = (TextView)findViewById(R.id.tv_storeName);
        tv_pon = (TextView)findViewById(R.id.tv_pon);
        tv_waittingNum = (TextView)findViewById(R.id.tv_waittingNum);
        tv_waittingTime = (TextView)findViewById(R.id.tv_waittingTime);

        Bundle bundle = getIntent().getExtras();
        String totalPon = bundle.getString("totalPon", null);
        String storeName = bundle.getString("storeName", null);
        String totalWaittime = bundle.getString("totalWaittime", null);

        tv_pon.setText(totalPon);
        tv_storeName.setText(storeName);
        tv_waittingTime.setText(totalWaittime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnExit:
                finish();
                Toast.makeText(getApplicationContext(),"취소되었습니다.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnMinus:
                int num1 = Integer.parseInt(tv_pon.getText().toString());
                if(num1 < 2){   //01보다 내려가는것 방지
                    Toast.makeText(getApplicationContext(),"더 이상 줄일 수 없습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    num1--;
                    String result_num1 = String.format("%02d", num1);
                    tv_pon.setText(result_num1);
                }
                break;
            case R.id.btnPlus:
                int num2 = Integer.parseInt(tv_pon.getText().toString());
                if(num2 > 98){  //99보다 올라가는것 방지
                    Toast.makeText(getApplicationContext(),"더 이상 줄일 수 없습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    num2++;
                    String result_num2 = String.format("%02d", num2);
                    tv_pon.setText(result_num2);
                }
                break;
            case R.id.btnOk:
                finish();
                Toast.makeText(getApplicationContext(),"대기표가 발급되었습니다.",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
