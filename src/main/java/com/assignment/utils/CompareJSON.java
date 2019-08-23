package com.assignment.utils;

import com.assignment.reports.LoggerWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class CompareJSON {
	private LoggerWrapper loggerWrapper = LoggerWrapper.getInstance();
	public boolean compare(String res1, String res2) {
		JsonParser parser = new JsonParser();
		try {
			JsonElement j1 = parser.parse(res1);
			JsonElement j2 = parser.parse(res2);
			return j1.equals(j2);
		} catch (JsonParseException jpe) {
			loggerWrapper.myLogger.info("Incorrect JSON format - " + jpe.getStackTrace());
			try {
				if (res1.equals(res2))        // for handling when response is just a plain text
					return true;
			} catch (Exception e) {
				loggerWrapper.myLogger.info(loggerWrapper.getStackTrace(e.fillInStackTrace()));
				return false;
			}
		} catch (NullPointerException ne) {
			loggerWrapper.myLogger.info("Response is null " + ne.getStackTrace());
			if (res1 == null && res2 == null) // for handling when both response are empty
				return true;
			return false;
		} catch (Exception e) {
			loggerWrapper.myLogger.info(loggerWrapper.getStackTrace(e.fillInStackTrace()));
			return false;
		}
		return false;
	}
}

