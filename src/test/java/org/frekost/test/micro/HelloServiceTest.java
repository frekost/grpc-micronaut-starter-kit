package org.frekost.test.micro;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import org.frekost.micro.client.HelloClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class HelloServiceTest {

    @Inject
    HelloClient client;

    @Test
    public void testSayHello() {
        String resp = client.sayHello("JUnit");
        Assertions.assertEquals("Hello, JUnit!", resp);
    }
}