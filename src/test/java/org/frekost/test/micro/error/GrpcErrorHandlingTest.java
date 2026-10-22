package org.frekost.test.micro.error;

import org.frekost.micro.client.HelloClient;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
class GrpcErrorHandlingTest {

    @Inject
    HelloClient client;

    @Test
    void testSayHelloWithValidName() {
        String response = client.sayHello("JUnit");

        Assertions.assertEquals("Hello, JUnit!", response);
    }

    @Test
    void testSayHelloWithEmptyName() {
        StatusRuntimeException exception = Assertions.assertThrows(
                StatusRuntimeException.class,
                () -> client.sayHello("")
        );

        Assertions.assertEquals(
                Status.Code.INVALID_ARGUMENT,
                exception.getStatus().getCode()
        );

        Assertions.assertEquals(
                "The name must not be empty",
                exception.getStatus().getDescription()
        );
    }

    @Test
    void testSayHelloWithWhitespaceName() {
        StatusRuntimeException exception = Assertions.assertThrows(
                StatusRuntimeException.class,
                () -> client.sayHello("   ")
        );

        Assertions.assertEquals(
                Status.Code.INVALID_ARGUMENT,
                exception.getStatus().getCode()
        );
    }

    @Test
    void testSayHelloWithNameContainingSpaces() {
        String response = client.sayHello("Mario Rossi");

        Assertions.assertEquals("Hello, Mario Rossi!", response);
    }

    @Test
    void testServiceWorksAfterInvalidRequest() {
        Assertions.assertThrows(
                StatusRuntimeException.class,
                () -> client.sayHello("")
        );

        String response = client.sayHello("JUnit");

        Assertions.assertEquals("Hello, JUnit!", response);
    }
}