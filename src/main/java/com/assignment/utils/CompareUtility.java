package com.assignment.utils;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class CompareUtility implements Runnable{
//	private  final ThreadLocal<ApiUtility> apiExecute =
//			new ThreadLocal<ApiUtility>()
//			{
//				@Override
//				protected ApiUtility  initialValue()
//				{
//					return new ApiUtility();
//				}
//			};
//
//	private  final ThreadLocal<CompareJSON> compareJSON =
//			new ThreadLocal<CompareJSON>()
//			{
//				@Override
//				protected CompareJSON  initialValue()
//				{
//					return new CompareJSON();
//				}
//			};
//	private  final ThreadLocal<CompareXML> compareXML =
//			new ThreadLocal<CompareXML>()
//			{
//				@Override
//				protected CompareXML  initialValue()
//				{
//					return new CompareXML();
//				}
//			};

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
        Set<Map.Entry<String, List<String>>> headers1 = res1.getHeaders().entrySet();

        for (Map.Entry<String, List<String>> header : headers1) {
            if(header.getKey().equals("Content-Type"))
                content1 = header.getValue().get(0);
        }
        String content2 = null;
        ResponseEntity<String> res2 =ae.getAPI(request2);

        Set<Map.Entry<String, List<String>>> headers2 = res2.getHeaders().entrySet();

        for (Map.Entry<String, List<String>> header : headers2) {
            if(header.getKey().equals("Content-Type"))
                content2 = header.getValue().get(0);
        }

        if(content1==null || content2== null){
            if(c.compare(res1.getBody(), res2.getBody()) == 0) {
                System.out.println(request1 + " equals " + request2);
            }
            else
                System.out.println(request1 +" not equals "+ request2 );
        }
        else if(content1.equals(content2) && content1.equals("application/json; charset=utf-8")) {
            if(c.compare(res1.getBody(), res2.getBody()) == 0) {
                System.out.println(request1 +" equals "+ request2 );
            }
            else
                System.out.println(request1 +" not equals "+ request2 );
        }
        else if(content1.equals(content2) && content1.equals("application/xml; charset=utf-8")) {
            if(xc.compare(res1.getBody(), res2.getBody())==true) {
                System.out.println(request1 +" equals "+ request1 );
            }
            else
                System.out.println(request1 +" not equals "+ request2 );
        }
        else {
            if(res1.getBody().equals(res2.getBody()))
                System.out.println(request1 +" equals "+ request1 );
            else
                System.out.println(request1 + " not equals " + request2);
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
        Set<Map.Entry<String, List<String>>> headers1 = res1.getHeaders().entrySet();

        for (Map.Entry<String, List<String>> header : headers1) {
            if(header.getKey().equals("Content-Type"))
                content1 = header.getValue().get(0);
        }
        String content2 = null;
        ResponseEntity<String> res2 =ae.getAPI(request2);

        Set<Map.Entry<String, List<String>>> headers2 = res2.getHeaders().entrySet();

        for (Map.Entry<String, List<String>> header : headers2) {
            if(header.getKey().equals("Content-Type"))
                content2 = header.getValue().get(0);
        }

        if(content1==null && content2== null){
            if(c.compare(res1.getBody(), res2.getBody()) == 0) {
                System.out.println(request1 + " equals " + request2);
            }
            else
                System.out.println(request1 +" not equals "+ request2 );
        }
        else if(content1.equals(content2) && content1.equals("application/json; charset=utf-8")) {
            if(c.compare(res1.getBody(), res2.getBody()) == 0) {
                System.out.println(request1 +" equals "+ request2 );
            }
            else
                System.out.println(request1 +" not equals "+ request2 );
        }
        else if(content1.equals(content2) && content1.equals("application/xml; charset=utf-8")) {
            if(xc.compare(res1.getBody(), res2.getBody())==true) {
                System.out.println(request1 +" equals "+ request1 );
            }
            else
                System.out.println(request1 +" not equals "+ request2 );
        }
        else {
            if(res1.equals(res2))
                System.out.println(request1 +" equals "+ request1 );
            else
                System.out.println(request1 + " not equals " + request2);
        }
        System.gc();
    }


    public static void main(String[] args) {
        CompareUtility compareUtility = new CompareUtility("http://reqres.in/api/users/3","http://reqres.in/api/users/3");
        compareUtility.compareRequests("https://reqres.in/api/users/3", "http://reqres.in/api/users/3");

    }

}
