package com.assignment.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class ApiUtility {
	//GET Request
	public ResponseEntity<String> getAPI(String baseuri){
		try {

			RestTemplate rt = new RestTemplate();
			rt.setErrorHandler(new DefaultResponseErrorHandler() {
				@Override
				public boolean hasError(HttpStatus statusCode) {
					return false;
				}
			});
			HttpHeaders headers = new HttpHeaders();
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0;");
			HttpEntity<String> entity = new HttpEntity<String>(null, headers);

			ResponseEntity<String> res = rt.exchange(baseuri, HttpMethod.GET, entity, String.class);
			return res;
		}		
		catch(IllegalArgumentException | HttpClientErrorException e){
			return null;
		}
		catch(Exception e) {
			System.out.println("Invalid request");
			System.out.println(e);
			return null;
		}
	}



	//POST Request
	public ResponseEntity<String> postAPI(String baseuri, MultiValueMap<String, String> params , String header){

		RestTemplate rt = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseuri)
				.queryParams(params);
		HttpHeaders httpHeaders = new HttpHeaders();

		/* Converting headers to map*/
		JSONObject headers = new JSONObject(header);
		HashMap<String, Object> map = (HashMap<String, Object>) headers.toMap();
		Iterator itr =  map.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) itr.next();
			httpHeaders.add(entry.getKey(),entry.getValue());
		}
		HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
		ResponseEntity<String> res = rt.exchange(baseuri, HttpMethod.POST, entity, String.class);

		return res;
	}

	//PUT Request
	public ResponseEntity<String> putAPI(String baseuri,MultiValueMap<String, String> params ,String header){
		RestTemplate rt = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseuri)
				.queryParams(params);
		HttpHeaders httpHeaders = new HttpHeaders();

		/* Converting headers to map*/
		JSONObject headers = new JSONObject(header);
		HashMap<String, Object> map = (HashMap<String, Object>) headers.toMap();
		Iterator itr =  map.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) itr.next();
			httpHeaders.add(entry.getKey(),entry.getValue());
		}
		HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
		ResponseEntity<String> res = rt.exchange(baseuri, HttpMethod.PUT, entity, String.class);

		return res;
	}

	//Delete request
	public ResponseEntity<String> deleteAPI(String baseuri, MultiValueMap<String, String> params, String header){
		RestTemplate rt = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseuri)
				.queryParams(params);
		HttpHeaders httpHeaders = new HttpHeaders();

		/* Converting headers to map*/
		JSONObject headers = new JSONObject(header);
		HashMap<String, Object> map = (HashMap<String, Object>) headers.toMap();
		Iterator itr =  map.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) itr.next();
			httpHeaders.add(entry.getKey(),entry.getValue());
		}
		HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
		ResponseEntity<String> res = rt.exchange(baseuri, HttpMethod.PUT, entity, String.class);

		return res;
	}

	public static void main(String[] args) throws URISyntaxException {

		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.add("user-agent", "Mozilla/5.0");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		String url = "https://reqres.in/api/users/3";
		ResponseEntity<String> res1 = rt.exchange(url, HttpMethod.GET, entity, String.class);
		System.out.println(res1);

		headers.add("user-agent", "Mozilla/5.0");
		entity = new HttpEntity<String>("parameters", headers);
		url = "https://reqres.in/api/users/2";
		ResponseEntity<String> res2 = rt.exchange(url, HttpMethod.GET, entity, String.class);
		System.out.println(res2);

		JsonParser parser = new JsonParser();
		JsonElement o1 = parser.parse("{a : {a : 2}, b : 2}");
		JsonElement o2 = parser.parse("{b : 2, a : {a : 2}}");
		System.out.println(o1.equals(o2));

	}
}
