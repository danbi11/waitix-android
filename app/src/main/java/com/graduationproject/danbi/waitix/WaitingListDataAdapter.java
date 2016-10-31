package com.graduationproject.danbi.waitix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by danbi_000 on 2016-10-30.
 */

public class WaitingListDataAdapter extends BaseAdapter {

    ArrayList<WaitingListData> datas;
    LayoutInflater inflater;

    public WaitingListDataAdapter(LayoutInflater inflater, ArrayList<WaitingListData> datas) {
        // TODO Auto-generated constructor stub
        this.datas= datas;

        //list_row.xml 파일을 View 객체로 생성(inflating) 해주는 역할의 객체
        //이 MemberDataAdapter 클래스를 객체로 만들어내는 클래스에서 LayoutInflater 객체 전달해주야 함.
        //이번 예제어세는 MainActivity.java에서 전달.
        //xml 종이의 글씨를 부풀려서 객체로 메모리에 만들어 낸다고 해서 '부풀리다(inflate)'라는 표현 사용
        this.inflater= inflater;
    }

    @Override
    public int getCount() {
        return datas.size(); //datas의 개수를 리턴
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);//datas의 특정 인덱스 위치 객체 리턴.
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView==null){
            //null이라면 재활용할 View가 없으므로 새로운 객체 생성
            //View의 모양은 res폴더>>layout폴더>>list.xml 파일로 객체를 생성
            //xml로 선언된 레이아웃(layout)파일을 객체로 부풀려주는 LayoutInflater 객체 활용.
            convertView= inflater.inflate(R.layout.item_waitinglist, null);
        }


        //2. Bind View
        //재활용을 하든 새로 만들었든 이제 converView는 View객체 상태임.
        //이 convertView로부터 데이터를 입력할 위젯들 참조하기.
        //이름을 표시하는 TextView, 국적을 표시하는 TextView, 국기이미지를 표시하는 ImageView
        //convertView로 부터 findViewById()를 하시는 것에 주의하세요.
        TextView tv_storeName = (TextView)convertView.findViewById(R.id.tv_storeName);
        TextView tv_location= (TextView)convertView.findViewById(R.id.tv_location);
        TextView tv_time= (TextView)convertView.findViewById(R.id.tv_time);
        TextView tv_num= (TextView)convertView.findViewById(R.id.tv_waitingNum);

        //현재 position( getView()메소드의 첫번재 파라미터 )번째의 Data를 위 해당 View들에 연결..
        tv_storeName.setText( datas.get(position).getName() );
        tv_location.setText( datas.get(position).getLocation() );
        tv_time.setText( datas.get(position).getTime() );
        tv_num.setText( datas.get(position).getNum() );

        //설정이 끝난 convertView객체 리턴(ListView에서 목록 하나.)
        return convertView;
    }
}
