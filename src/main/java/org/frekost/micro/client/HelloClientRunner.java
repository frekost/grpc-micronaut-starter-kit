package org.frekost.micro.client;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

@Singleton
public class HelloClientRunner {

    private final HelloClient helloClient;

    public HelloClientRunner(HelloClient helloClient) {
        this.helloClient = helloClient;
    }

    @Scheduled(initialDelay = "3s")
    void run() {
        try {
            String response = helloClient.sayHello("Micronaut");
            System.out.println("Response from gRPC server: " + response);
        } catch (Exception e) {
            System.err.println("gRPC call failed: " + e.getMessage());
        }
    }
}