package com.assignment.model;

public class Requests {

    String request1;
    String request2;

    public String getRequest1() {
        return request1;
    }

    public void setRequest1(String request1) {
        this.request1 = request1;
    }
    public String getRequest2() {
        return request2;
    }

    public void setRequest2(String request2) {
        this.request2 = request2;
    }

    public Requests(){

    }

    public Requests(String request1, String request2){
        this.request1 = request1;
        this.request2 = request2;
    }

    @Override
    public String toString() {
        return "{" +
                "request1='" + request1 + '\'' +
                ", request2='" + request2 + '\'' +
                '}';
    }
}
