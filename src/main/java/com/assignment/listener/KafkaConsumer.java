package com.assignment.listener;

import com.assignment.model.Requests;
import com.assignment.utils.CompareUtility;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

;

@Service
public class KafkaConsumer {

    public ThreadPoolExecutor getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ThreadPoolExecutor executorService) {
        this.executorService = executorService;
    }

    private ThreadPoolExecutor executorService = null;

    @KafkaListener(topics = "Kafka_Example", group = "group_id")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }


    @KafkaListener(topics = "Kafka_json", group = "group_json",
            containerFactory = "userKafkaListenerFactory")
    public void consumeJson(Requests requests) {
        //System.out.println("Consumed JSON Message: " + requests);
        CompareUtility compareUtility = new CompareUtility(requests.getRequest1(), requests.getRequest2());
        executorService.execute(compareUtility);
        //System.out.println("SIZE - " + executorService.getPoolSize());

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

}
