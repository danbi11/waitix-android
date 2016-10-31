package com.graduationproject.danbi.waitix;

/**
 * Created by danbi_000 on 2016-10-30.
 */

//////WaitingListData와 같아서 필요없을것같음///////

public class StoreListData {
    String name, location, time;
    String num;

    public StoreListData(String name, String location, String time, String num) {
        // TODO Auto-generated constructor stub
        //생성자함수로 전달받은 정보를 멤버변수에 저장..
        this.name= name;
        this.location=location;
        this.time=time;
        this.num=num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNum(String num){
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
    public String getNum(){
        return num;
    }
}
