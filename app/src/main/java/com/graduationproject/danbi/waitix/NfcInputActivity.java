package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by danbi_000 on 2016-10-18.
 */

public class NfcInputActivity extends Activity implements View.OnClickListener{
    RelativeLayout btnExit, btnMinus, btnPlus;
    TextView tv_storeName,tv_pon, tv_waittingNum, tv_waittingTime;
    ImageView btnOk;

    String snum;


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
        snum = bundle.getString("snum", null);
        String storeName = bundle.getString("storeName", null);
        String totalWaittime = bundle.getString("totalWaittime", null);

        tv_waittingNum.setText(totalPon + "팀");
        tv_storeName.setText(storeName);
//        tv_waittingTime.setText("약 " + totalWaittime + "분");

        tv_waittingTime.setText("약 " + Integer.parseInt(totalPon) * 3 + "분");

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
                ServerNetworkManager serverNetworkManager = ServerNetworkManager.newInstance();

                SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                String unum = sharedPreferences.getString("unum", null);

                List<Pair<String, ?>> parameters = new ArrayList<Pair<String, ?>>();
                parameters.add(Pair.create("unum", String.valueOf(unum)));
                parameters.add(Pair.create("snum", Integer.parseInt(snum)));
                parameters.add(Pair.create("pon", Integer.parseInt(tv_pon.getText().toString())));

                Toast.makeText(NfcInputActivity.this, snum, Toast.LENGTH_SHORT).show(); //임시

                serverNetworkManager.get("user_request.php", parameters, new Callback() {
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

                                    if (jsonObject.get("success").getAsBoolean()) {
                                        Toast.makeText(getApplicationContext(),"대기표가 발급되었습니다.",Toast.LENGTH_SHORT).show();

/*
                                        //현재대기상황 api받아서 메인에 전달
                                        ServerNetworkManager serverNetworkManager = ServerNetworkManager.newInstance();

                                        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                                        String unum = sharedPreferences.getString("unum", null);

                                        List<Pair<String, ?>> parameters = new ArrayList<Pair<String, ?>>();
                                        parameters.add(Pair.create("unum", String.valueOf(unum)));

                                        serverNetworkManager.get("user_verify.php", parameters, new Callback() {
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

                                                            JsonObject jsonObject2 = jsonObject.get("current").getAsJsonObject();
                                                            String pon = jsonObject2.get("pon").getAsString();
                                                            String date = jsonObject2.get("date").getAsString();
                                                            String waitpon = jsonObject2.get("waitpon").getAsString();
                                                            String waittime = jsonObject2.get("waittime").getAsString();
                                                            String storeName = jsonObject2.get("storeName").getAsString();



                                                            Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
                                                            intentToMain.putExtra("storeName", storeName);
                                                            intentToMain.putExtra("pon", pon);
                                                            intentToMain.putExtra("date", date);
                                                            intentToMain.putExtra("waitpon", waitpon);
                                                            intentToMain.putExtra("waittime", waittime);
                                                            startActivity(intentToMain);



                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(NfcInputActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                                        } finally {
                                                            response.close();
                                                        }
                                                    }
                                                });
                                            }
                                        });

*/

                                        finish();
                                    } else {
                                        Toast.makeText(NfcInputActivity.this, jsonObject.get("desc").getAsString(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(NfcInputActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                } finally {
                                    response.close();
                                }
                            }
                        });
                    }
                });




                break;
        }
    }
}
