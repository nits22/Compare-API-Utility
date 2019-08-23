package com.assignment.utils;

import com.assignment.listener.KafkaConsumer;
import com.assignment.model.Requests;
import com.assignment.reports.LoggerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ProducerDataClass {

	@Autowired
	private KafkaTemplate<String, Requests> kafkaTemplate;

	@Autowired
	KafkaConsumer kafkaConsumer;

	@Value("${request_topic}")
	private String TOPIC;

	private LoggerWrapper loggerWrapper = LoggerWrapper.getInstance();

	@EventListener(ApplicationReadyEvent.class)
	public void startapp () {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		File file1 = new File("src/main/resources/File3");
		File file2 = new File("src/main/resources/File4");
		int THREAD_COUNT = getThreads(file1,file2);
		kafkaConsumer.setExecutorService(new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 0L,TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy()));

		BufferedReader br1 = null;
		BufferedReader br2 = null;
		try {
			br1 = new BufferedReader(new FileReader(file1));
			br2 = new BufferedReader(new FileReader(file2));
		} catch (FileNotFoundException fnfe) {
			loggerWrapper.myLogger.error("Incorrect File path " + fnfe.getStackTrace());
			return;
		}

		try {
			String line1 = null;
			line1 = br1.readLine();
			String line2 = br2.readLine();
			while(line1 != null && line2 != null){
				Requests req = new Requests();
				req.setRequest1(line1);
				req.setRequest2(line2);
                kafkaTemplate.send(TOPIC,req);
				line1 = br1.readLine();
				line2 = br2.readLine();
			}
			br1.close();
			br2.close();
		} catch (IOException ioe) {
			loggerWrapper.myLogger.error("Error: " + ioe.getStackTrace());
		}

	}

	public static int getThreads(File file1, File file2) {
		long count;
		int factor = 20000;
		int MAX_SIZE = 100;
		if(getFileSizeBytes(file1) > getFileSizeBytes(file2))
			count = getFileSizeBytes(file1)/factor;
		else
			count = getFileSizeBytes(file2)/factor;
		if(count < 1) {
			return 20;
		}
		else {
			if((count * 10) > MAX_SIZE)
				return 100;
			else
				return (int) (count * 10)+20;
		}
	}

	public static void main(String[] args) {
		getThreads(new File("src/main/resources/File3"), new File("src/main/resources/File4"));
	}

	private static long getFileSizeBytes(File file) {
		return (long) file.length();
	}
}
