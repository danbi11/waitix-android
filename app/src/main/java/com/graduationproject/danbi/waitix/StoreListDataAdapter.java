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

public class StoreListDataAdapter extends BaseAdapter {
    ArrayList<WaitingListData> datas;
    LayoutInflater inflater;

    public StoreListDataAdapter(LayoutInflater inflater, ArrayList<WaitingListData> datas) {
        // TODO Auto-generated constructor stub
        this.datas= datas;
        this.inflater= inflater;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView==null){
            convertView= inflater.inflate(R.layout.item_storelist, null);
        }

        TextView tv_storeName = (TextView)convertView.findViewById(R.id.tv_storeName);
        TextView tv_location= (TextView)convertView.findViewById(R.id.tv_location);
        TextView tv_time= (TextView)convertView.findViewById(R.id.tv_time);
        TextView tv_num= (TextView)convertView.findViewById(R.id.tv_waitingNum);

        tv_storeName.setText( datas.get(position).getName() );
        tv_location.setText( datas.get(position).getLocation() );
        tv_time.setText( datas.get(position).getTime() );
        tv_num.setText( datas.get(position).getNum() );

        return convertView;
    }
}
