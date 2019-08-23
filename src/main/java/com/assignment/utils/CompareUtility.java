package com.assignment.utils;

import com.assignment.reports.LoggerWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class CompareUtility implements Runnable{
    private LoggerWrapper loggerWrapper = LoggerWrapper.getInstance();
    private String request1;
    private String request2;

    public CompareUtility(String request1, String request2){
        this.request1 = request1;
        this.request2 = request2;
    }

    public void compareRequests(String request1, String request2){
        final ApiUtility ae= new ApiUtility();
        final CompareJSON c = new CompareJSON();
        final CompareXML xc = new CompareXML();
        ResponseEntity<String> res1 =ae.getAPI(request1);
        String content1 = null;
        try {
            Set<Map.Entry<String, List<String>>> headers1 = res1.getHeaders().entrySet();

            for (Map.Entry<String, List<String>> header : headers1) {
                if (header.getKey().equals("Content-Type"))
                    content1 = header.getValue().get(0);
            }
        }
        catch (NullPointerException npe){
        }
        String content2 = null;
        ResponseEntity<String> res2 =ae.getAPI(request2);

        try{
            Set<Map.Entry<String, List<String>>> headers2 = res2.getHeaders().entrySet();

            for (Map.Entry<String, List<String>> header : headers2) {
                if(header.getKey().equals("Content-Type"))
                    content2 = header.getValue().get(0);
            }
        }
        catch (NullPointerException npe){
        }

        if((res1 == null && res2 != null) || (res1 !=null && res2 == null) || (res1 == null && res2 == null)){
            loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
            return;
        }
        if(res1.getStatusCode() != res2.getStatusCode()){
            loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
            return;
        }
        if(content1==null || content2== null){
            if(c.compare(res1.getBody(), res2.getBody()) == true) {
                loggerWrapper.onlyLogsInfo(request1 + " equals " + request2);
            }
            else
                loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
        }
        else if(content1.equals(content2) && content1.equals("application/json; charset=utf-8")) {
            if(c.compare(res1.getBody(), res2.getBody()) == true) {
                loggerWrapper.onlyLogsInfo(request1 +" equals "+ request2 );
            }
            else
                loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
        }
        else if(content1.equals(content2) && content1.equals("application/xml; charset=utf-8")) {
            if(xc.compare(res1.getBody(), res2.getBody())==true) {
                loggerWrapper.onlyLogsInfo(request1 +" equals "+ request1 );
            }
            else
                loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
        }
        else {
            if(res1.getBody().equals(res2.getBody()))
                loggerWrapper.onlyLogsInfo(request1 +" equals "+ request1 );
            else
                loggerWrapper.onlyLogsInfo(request1 + " not equals " + request2);
        }
        System.gc();
    }

    @Override
    public void run() {
        final ApiUtility ae= new ApiUtility();
        final CompareJSON c = new CompareJSON();
        final CompareXML xc = new CompareXML();
        ResponseEntity<String> res1 =ae.getAPI(request1);
        String content1 = null;
        try {
            Set<Map.Entry<String, List<String>>> headers1 = res1.getHeaders().entrySet();

            for (Map.Entry<String, List<String>> header : headers1) {
                if (header.getKey().equals("Content-Type"))
                    content1 = header.getValue().get(0);
            }
        }
        catch (NullPointerException npe){
        }
        String content2 = null;
        ResponseEntity<String> res2 =ae.getAPI(request2);

        try{
            Set<Map.Entry<String, List<String>>> headers2 = res2.getHeaders().entrySet();

            for (Map.Entry<String, List<String>> header : headers2) {
                if(header.getKey().equals("Content-Type"))
                    content2 = header.getValue().get(0);
            }
        }
        catch (NullPointerException npe){
        }

        if((res1 == null && res2 != null) || (res1 !=null && res2 == null) || (res1 == null && res2 == null)){
            loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
            return;
        }
        if(res1.getStatusCode() != res2.getStatusCode()){
            loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
            return;
        }
        if(content1==null || content2== null){
            if(c.compare(res1.getBody(), res2.getBody()) == true) {
                loggerWrapper.onlyLogsInfo(request1 + " equals " + request2);
            }
            else
                loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
        }
        else if(content1.equals(content2) && content1.equals("application/json; charset=utf-8")) {
            if(c.compare(res1.getBody(), res2.getBody()) == true) {
                loggerWrapper.onlyLogsInfo(request1 +" equals "+ request2 );
            }
            else
                loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
        }
        else if(content1.equals(content2) && content1.equals("application/xml; charset=utf-8")) {
            if(xc.compare(res1.getBody(), res2.getBody())==true) {
                loggerWrapper.onlyLogsInfo(request1 +" equals "+ request1 );
            }
            else
                loggerWrapper.onlyLogsInfo(request1 +" not equals "+ request2 );
        }
        else {
            if(res1.getBody().equals(res2.getBody()))
                loggerWrapper.onlyLogsInfo(request1 +" equals "+ request1 );
            else
                loggerWrapper.onlyLogsInfo(request1 + " not equals " + request2);
        }
        System.gc();
    }


    public static void main(String[] args) {
        CompareUtility compareUtility = new CompareUtility("http://reqres.in/api/users/3","http://reqres.in/api/users/3");
        compareUtility.compareRequests("https://reqres.in/ref3d3/344/342", "https://reqres.in/ref3d3/344/342");

    }
}
