package com.graduationproject.danbi.waitix;

/**
 * Created by danbi_000 on 2016-10-30.
 */

public class WaitingListData {
    String name, location, time;
    String num;

    public WaitingListData(String name, String location, String time, String num) {
        // TODO Auto-generated constructor stub
        //생성자함수로 전달받은 정보를 멤버변수에 저장..
        this.name= name;
        this.location=location;
        this.time=time;
        this.num=num;
    }

    //이 아래는 getter , setter 메소드듭입니다.
    //OOP(객체 지향 프로그래밍)은 클래스 객체 외부에서 직접 엠머변수에 접근하는 것을 지양합니다.
    //객체의 멤버변수 제어는 객체 스스로 하도록 해서 재사용성을 높인 방법이죠.
    //getter, setter 멤버 메소드들은 그 목적으로 만들어 진 것이죠.

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
