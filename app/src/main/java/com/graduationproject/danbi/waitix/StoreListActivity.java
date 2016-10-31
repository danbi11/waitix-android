package com.graduationproject.danbi.waitix;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class StoreListActivity extends Activity {
    ArrayList<WaitingListData> datas= new ArrayList<WaitingListData>();
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

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
    }
}
