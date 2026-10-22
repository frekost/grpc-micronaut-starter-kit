package org.frekost.micro.logging;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.micronaut.core.order.Ordered;

import jakarta.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GrpcLoggingInterceptor implements ServerInterceptor, Ordered {

    private static final Logger LOG =
            LoggerFactory.getLogger(GrpcLoggingInterceptor.class);

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String method = call.getMethodDescriptor().getFullMethodName();
        long startTime = System.nanoTime();

        LOG.info("gRPC call started: method={}", method);

        ServerCall<ReqT, RespT> loggingCall =
                new ForwardingServerCall.SimpleForwardingServerCall<>(call) {

                    @Override
                    public void close(Status status, Metadata trailers) {
                        long durationMs =
                                (System.nanoTime() - startTime) / 1_000_000;

                        if (status.isOk()) {
                            LOG.info(
                                    "gRPC call completed: method={}, status={}, durationMs={}",
                                    method,
                                    status.getCode(),
                                    durationMs);
                        } else {
                            LOG.warn(
                                    "gRPC call completed: method={}, status={}, durationMs={}",
                                    method,
                                    status.getCode(),
                                    durationMs);
                        }

                        super.close(status, trailers);
                    }
                };

        try {
            return next.startCall(loggingCall, headers);
        } catch (RuntimeException e) {
            long durationMs =
                    (System.nanoTime() - startTime) / 1_000_000;

            LOG.error(
                    "gRPC call failed during initialization: method={}, durationMs={}",
                    method,
                    durationMs,
                    e);

            throw e;
        }
    }
}