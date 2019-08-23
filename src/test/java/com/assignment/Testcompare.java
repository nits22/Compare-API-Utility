package com.assignment;

import com.assignment.reports.LoggerWrapper;
import com.assignment.reports.Testlistener;
import com.assignment.utils.ApiUtility;
import com.assignment.utils.CompareJSON;
import com.assignment.utils.CompareUtility;
import com.assignment.utils.CompareXML;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Listeners(Testlistener.class)
public class Testcompare {


	Properties prop;
	CompareJSON compareJSON = new CompareJSON();
	CompareXML compareXML = new CompareXML();
	private static LoggerWrapper loggerWrapper;

	@BeforeClass
	public void onceBeforeClass(){
		loggerWrapper = LoggerWrapper.getInstance();
		prop = new Properties();
		try {
			prop.load(new FileInputStream("src/test/resources/test.properties"));
		} catch (IOException e) {
			String stacktrace = ExceptionUtils.getStackTrace(e);
			loggerWrapper.myLogger.error("Unable to load properties file  --> " + stacktrace);
		}

	}

	@Test(description = "All Positive comparisons cases of JSON")
	public void checkJsonPositive() {
		String response4 = prop.getProperty("res4");
		String response2 = prop.getProperty("res2");
		String response1 = prop.getProperty("res1");
		String response5 = prop.getProperty("res5");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(compareJSON.compare(response4, response5),true);
		// check for different order of key value pairs
		softAssert.assertEquals(compareJSON.compare(response1, response2),true);
		//compare response when both Response is not JSON
		softAssert.assertEquals(compareJSON.compare("Test String", "Test String"),true);
		loggerWrapper.myLogger.info("Unable to load properties file  --> " );
		softAssert.assertAll();
	}

	@Test(description = "check when response body is null")
	public void checkWhenResponseIsNull() {
		Assert.assertEquals(compareJSON.compare(null, null),true, "Response either not null or not equal");
	}


	@Test(description = "Test run method of CompareUtility.class")
	public void runMethodTest() {
		CompareUtility compareUtility = new CompareUtility("https://reqres.in/api/users/3", "https://reqres.in/api/users/3");
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(compareUtility);

		service.shutdown();
		try {
			service.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test(description = "test for blank response with 301 status code")
	public void checkResponsefor301() {
		ApiUtility ap = new ApiUtility();
		Assert.assertEquals(compareJSON.compare(ap.getAPI("http://reqres.in/api/users/3").getBody(), ap.getAPI("http://reqres.in/api/users/2").getBody()),true);
	}


	@Test(description = "Test for empty response with 4xx status code")
	public void checkResponseWithEmptyBody() {
		ApiUtility ap = new ApiUtility();
		Assert.assertEquals(compareJSON.compare(ap.getAPI("https://reqres.in/api/unknown/23").getBody(), ap.getAPI("https://reqres.in/api/unknown/23").getBody()),true);
	}


	@Test(description = "Test for same error response with same status code")
	public void checkForSameErrorResponse() {
		ApiUtility ap = new ApiUtility();
		Assert.assertEquals(compareJSON.compare(ap.getAPI("http://www.mocky.io/v2/5d5e6d162f00005e0092f93d").getBody(), ap.getAPI("http://www.mocky.io/v2/5d5e79d22f00005e0092f9ed").getBody()),true);
	}

	@Test(description = "Test for same error response but different status code")
	public void checkForSameErrorResponseDifferentStatusCode() {
		ApiUtility ap = new ApiUtility();
		Assert.assertEquals(compareJSON.compare(ap.getAPI("http://www.mocky.io/v2/5d5e6d162f00005e0092f93d").getBody(), ap.getAPI("http://www.mocky.io/v2/5d5e7be42f00004a0092f9ff").getBody()),true);
	}


	@Test(description = "Test response is returned for API with Large body")
	public void checkRequestWithLargeBody() {
		ApiUtility ap = new ApiUtility();
		Assert.assertEquals(ap.getAPI("http://dummy.restapiexample.com/api/v1/employees").getStatusCode(), HttpStatus.OK);
	}


	@Test(description = "Test for deplayed response timeout")
	public void checkRequestWithDelayedResponse() {
		ApiUtility ap = new ApiUtility();
		Assert.assertEquals(ap.getAPI("http://www.mocky.io/v2/5d5e6dbd2f00005c0092f955?mocky-delay=20s").getStatusCode(), HttpStatus.GATEWAY_TIMEOUT);
	}

	@Test(description = "Negative JSON comparison test cases")
	public void negativeJsonTest() {
		ApiUtility ap = new ApiUtility();
		String response1 = prop.getProperty("res1");
		String response3 = prop.getProperty("res3");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(compareJSON.compare(response1, response3),false);

		//compare response when one response is not JSON
		softAssert.assertEquals(compareJSON.compare(response1, "Test String"),false);

		//check blank response is not equal Empty "{}" response
		softAssert.assertEquals(compareJSON.compare(ap.getAPI("https://reqres.in/api/unknown/23").getBody(), ap.getAPI("http://reqres.in/api/users/2").getBody()),false);
		softAssert.assertAll();

	}

	// 400 - http://www.mocky.io/v2/5d5e6d162f00005e0092f93d
	// 400 - http://www.mocky.io/v2/5d5e79d22f00005e0092f9ed
	// 504 - http://www.mocky.io/v2/5d5e6dbd2f00005c0092f955?mocky-delay=20s
	// 203 with error message - http://www.mocky.io/v2/5d5e7be42f00004a0092f9ff

	// 200 - http://www.mocky.io/v2/5d5e732d2f00004a0092f9a1
	// 203 with error message - http://www.mocky.io/v2/5d5e7d1f2f00005c0092fa11
	// 400 - http://www.mocky.io/v2/5d5e7d322f00005f0092fa14
	// 400 -  http://www.mocky.io/v2/5d5e7d572f00004e0092fa18
	// 504 - http://www.mocky.io/v2/5d5e7ec92f00005c0092fa27?mocky-delay=20s

	//200 - http://www.mocky.io/v2/5d5e84002f00004e0092fa5d
	//200 - http://www.mocky.io/v2/5d5e84262f00004e0092fa62

	@Test(description = "Test for when both requests returned plain/text response")
	public void checkForPlainTextResponse(){
		CompareUtility compareUtility = new CompareUtility("http://www.mocky.io/v2/5d5e84002f00004e0092fa5d", "http://www.mocky.io/v2/5d5e84262f00004e0092fa62");
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(compareUtility);

		service.shutdown();
		try {
			service.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test(description = "Positive compareXML test cases")
	public void xmlTest() {
		String xml1 = prop.getProperty("xml1");
		String xml2 = prop.getProperty("xml2");
		String xml3 = prop.getProperty("xml3");
		String xml4 = prop.getProperty("xml4");
		String xml5 = prop.getProperty("xml5");
		String xml6 = prop.getProperty("xml6");

		SoftAssert softAssert = new SoftAssert();
		// test for xml with extra whitespaces
		softAssert.assertEquals(compareXML.compare(xml4, xml5), true);

		softAssert.assertEquals(compareXML.compare(xml4, xml6), false);

		// test for xml with different attribute order
		softAssert.assertEquals(compareXML.compare(xml1, xml2), true);

		// test for xml with different tags order
		softAssert.assertEquals(compareXML.compare(xml1, xml3), true);
		softAssert.assertAll();
	}


	@Test(description = "Test same error response with same status code for XML response")
	public void xmlCheckForSameErrorResponse() {
		ApiUtility ap = new ApiUtility();
		Assert.assertEquals(compareXML.compare(ap.getAPI("http://www.mocky.io/v2/5d5e7d322f00005f0092fa14").getBody(), ap.getAPI("http://www.mocky.io/v2/5d5e7d572f00004e0092fa18").getBody()),true);
	}


	@Test(description = "Test same error response but different status code for XML response")
	public void xmlCheckForSameErrorResponseDifferentStatusCode() {
		ApiUtility ap = new ApiUtility();
		Assert.assertEquals(compareXML.compare(ap.getAPI("http://www.mocky.io/v2/5d5e7d1f2f00005c0092fa11").getBody(), ap.getAPI("http://www.mocky.io/v2/5d5e7d322f00005f0092fa14").getBody()),true);
	}


	@Test(description = "Text when response body is null for xml")
	public void xmlCheckWhenResponseIsNull() {
		Assert.assertEquals(compareXML.compare(null, null),true);
	}

	@AfterMethod
	public void close() {
		Testlistener.extent.flush();
	}
}
