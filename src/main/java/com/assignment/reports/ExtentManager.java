package com.assignment.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.Platform;
import org.testng.Reporter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {
	private static ExtentReports extent;
	private static Platform platform;
	private static String macPath = System.getProperty("user.dir")+ "/result-files/extent-reports";
	private static String windowsPath = System.getProperty("user.dir")+ "\\result-files\\extent-reports";
	private static String macReportFileLoc = macPath + "/" ;
	private static String winReportFileLoc = windowsPath + "\\";
	private static LoggerWrapper loggerWrapper = LoggerWrapper.getInstance();
	protected ExtentManager(){

	}


	public synchronized static ExtentReports getInstance() {

		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");
		Date date = new Date();

		if (extent == null) {
			synchronized (ExtentManager.class) {
				if(extent==null) {
					File resultDirectory = new File(System.getProperty("user.dir") + "/result-files/extent-reports");
					/*
					 * File resultDirectory = new
					 * File(outputDirectory.getParentFile(),"ExtentReports");
					 */
					platform = getPlatform();
					String fileName = getReportFileLocation(platform);
					ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName + File.separator + "Report" + "-" + dateFormat.format(date) + ".html");
					htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
					htmlReporter.config().setChartVisibilityOnOpen(true);
					htmlReporter.config().setTheme(Theme.STANDARD);
					htmlReporter.config().setDocumentTitle(fileName);
					htmlReporter.config().setEncoding("utf-8");
					htmlReporter.config().setReportName(fileName);

					extent = new ExtentReports();
					extent.attachReporter(htmlReporter);
					Reporter.log("Extent Report directory: " + resultDirectory, true);
				}
			}
		}

		return extent;
	}


	private static Platform getPlatform () {
		if (platform == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				platform = Platform.WINDOWS;
			} else if (operSys.contains("nix") || operSys.contains("nux")
					|| operSys.contains("aix")) {
				platform = Platform.LINUX;
			} else if (operSys.contains("mac")) {
				platform = Platform.MAC;
			}
		}
		return platform;
	}

	private static void createReportPath (String path) {
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			if (testDirectory.mkdir()) {
				loggerWrapper.onlyLogsInfo("Directory: " + path + " is created!" );
			} else {
				loggerWrapper.onlyLogsInfo("Failed to create directory: " + path);
			}
		} else {
			loggerWrapper.onlyLogsInfo("Directory already exists: " + path);
		}
	}

	private static String getReportFileLocation (Platform platform) {
		String reportFileLocation = null;
		switch (platform) {
		case MAC:
			reportFileLocation = macReportFileLoc;
			createReportPath(macPath);
			loggerWrapper.onlyLogsInfo("ExtentReport Path for MAC: " + macPath + "\n");
			break;
		case WINDOWS:
			reportFileLocation = winReportFileLoc;
			createReportPath(windowsPath);
			loggerWrapper.onlyLogsInfo("ExtentReport Path for WINDOWS: " + windowsPath + "\n");
			break;
		default:
			loggerWrapper.onlyLogsInfo("ExtentReport path has not been set");
			break;
		}
		return reportFileLocation;
	}
}
