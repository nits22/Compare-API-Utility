package com.assignment.utils;

import com.assignment.model.Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
public class ProducerDataClass {

	@Autowired
	private KafkaTemplate<String, Requests> kafkaTemplate;

	@Value("${request_topic}")
	private String TOPIC;

	@EventListener(ApplicationReadyEvent.class)
	public void startapp () throws Exception {
		Thread.sleep(10000);

		try {
			File file1 = new File("src/main/resources/File3");
			File file2 = new File("src/main/resources/File4");

			BufferedReader br1 = new BufferedReader(new FileReader(file1));
			BufferedReader br2 = new BufferedReader(new FileReader(file2));
			String line1 = br1.readLine();
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
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			//awaitTerminationAfterShutdown(kafkaConsumer.getExecutorService());
		}
	}

	public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
		threadPool.shutdown();
		try {
			if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();
			}
		} catch (InterruptedException ex) {
			threadPool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	public void sendMessage(Requests message) {

		ListenableFuture<SendResult<String, Requests>> future = kafkaTemplate.send(TOPIC, message);
		future.addCallback(new ListenableFutureCallback<SendResult<String, Requests>>() {

			@Override
			public void onSuccess(SendResult<String, Requests> result) {
				System.out.println("Sent message=[" + message +
						"] with offset=[" + result.getRecordMetadata().offset() + "]");
			}
			@Override
			public void onFailure(Throwable ex) {
				System.out.println("Unable to send message=["
						+ message + "] due to : " + ex.getMessage());
			}
		});
	}
}
