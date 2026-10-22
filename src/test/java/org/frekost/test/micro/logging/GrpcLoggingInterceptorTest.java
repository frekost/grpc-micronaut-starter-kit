package org.frekost.test.micro.logging;

import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.Status;

import org.frekost.micro.logging.GrpcLoggingInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class GrpcLoggingInterceptorTest {

    private GrpcLoggingInterceptor interceptor;

    private static final MethodDescriptor.Marshaller<String> STRING_MARSHALLER =
            new MethodDescriptor.Marshaller<>() {

                @Override
                public InputStream stream(String value) {
                    return new ByteArrayInputStream(
                            value.getBytes(StandardCharsets.UTF_8));
                }

                @Override
                public String parse(InputStream stream) {
                    try {
                        return new String(
                                stream.readAllBytes(),
                                StandardCharsets.UTF_8);
                    } catch (java.io.IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

    private static final MethodDescriptor<String, String> METHOD =
            MethodDescriptor.<String, String>newBuilder()
                    .setType(MethodDescriptor.MethodType.UNARY)
                    .setFullMethodName("helloworld.Greeter/SayHello")
                    .setRequestMarshaller(STRING_MARSHALLER)
                    .setResponseMarshaller(STRING_MARSHALLER)
                    .build();

    @BeforeEach
    void setUp() {
        interceptor = new GrpcLoggingInterceptor();
    }

    @Test
    void shouldHandleSuccessfulCall() {
        TestServerCall call = new TestServerCall();

        ServerCallHandler<String, String> handler =
                (serverCall, headers) -> {
                    serverCall.close(Status.OK, new Metadata());
                    return new ServerCall.Listener<>() {};
                };

        ServerCall.Listener<String> listener =
                interceptor.interceptCall(
                        call,
                        new Metadata(),
                        handler);

        assertNotNull(listener);
        assertNotNull(call.status);
        assertEquals(Status.Code.OK, call.status.getCode());
    }

    @Test
    void shouldHandleFailedCall() {
        TestServerCall call = new TestServerCall();

        ServerCallHandler<String, String> handler =
                (serverCall, headers) -> {
                    serverCall.close(
                            Status.INVALID_ARGUMENT
                                    .withDescription("Invalid request"),
                            new Metadata());

                    return new ServerCall.Listener<>() {};
                };

        ServerCall.Listener<String> listener =
                interceptor.interceptCall(
                        call,
                        new Metadata(),
                        handler);

        assertNotNull(listener);
        assertNotNull(call.status);
        assertEquals(
                Status.Code.INVALID_ARGUMENT,
                call.status.getCode());
        assertEquals(
                "Invalid request",
                call.status.getDescription());
    }

    @Test
    void shouldRethrowExceptionWhenStartingCall() {
        TestServerCall call = new TestServerCall();

        RuntimeException expected =
                new RuntimeException("Simulated startup failure");

        ServerCallHandler<String, String> handler =
                (serverCall, headers) -> {
                    throw expected;
                };

        RuntimeException actual = assertThrows(
                RuntimeException.class,
                () -> interceptor.interceptCall(
                        call,
                        new Metadata(),
                        handler));

        assertSame(expected, actual);
    }

    private static class TestServerCall
            extends ServerCall<String, String> {

        private Status status;

        @Override
        public void request(int numMessages) {
        }

        @Override
        public void sendHeaders(Metadata headers) {
        }

        @Override
        public void sendMessage(String message) {
        }

        @Override
        public void close(Status status, Metadata trailers) {
            this.status = status;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public MethodDescriptor<String, String> getMethodDescriptor() {
            return METHOD;
        }
    }
}