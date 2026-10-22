package org.frekost.micro.grpc.error;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.micronaut.core.order.Ordered;
import jakarta.inject.Singleton;

@Singleton
public class GrpcErrorHandlingInterceptor implements ServerInterceptor, Ordered {

    private static final Logger LOG =
            LoggerFactory.getLogger(GrpcErrorHandlingInterceptor.class);

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        AtomicBoolean closed = new AtomicBoolean(false);

        ServerCall<ReqT, RespT> forwardingCall =
                new ForwardingServerCall.SimpleForwardingServerCall<>(call) {

                    @Override
                    public void close(Status status, Metadata trailers) {
                        if (closed.compareAndSet(false, true)) {
                            super.close(status, trailers);
                        }
                    }
                };

        final ServerCall.Listener<ReqT> delegate;

        try {
            delegate = next.startCall(forwardingCall, headers);
        } catch (RuntimeException exception) {
            handleException(forwardingCall, exception);
            return new ServerCall.Listener<>() {
            };
        }

        return new ServerCall.Listener<>() {

            private void executeSafely(Runnable action) {
                if (closed.get()) {
                    return;
                }

                try {
                    action.run();
                } catch (RuntimeException exception) {
                    handleException(forwardingCall, exception);
                }
            }

            @Override
            public void onMessage(ReqT message) {
                executeSafely(() -> delegate.onMessage(message));
            }

            @Override
            public void onHalfClose() {
                executeSafely(delegate::onHalfClose);
            }

            @Override
            public void onReady() {
                executeSafely(delegate::onReady);
            }

            @Override
            public void onCancel() {
                executeSafely(delegate::onCancel);
            }

            @Override
            public void onComplete() {
                executeSafely(delegate::onComplete);
            }
        };
    }

    private <ReqT, RespT> void handleException(
            ServerCall<ReqT, RespT> call,
            RuntimeException exception) {

        StatusRuntimeException grpcException =
                GrpcErrorMapper.toGrpcError(exception);

        Status status = grpcException.getStatus();

        if (status.getCode() == Status.Code.INTERNAL) {
            LOG.error(
                    "Unexpected exception while processing gRPC request",
                    exception
            );
        } else {
            LOG.warn(
                    "gRPC request failed with status {}: {}",
                    status.getCode(),
                    status.getDescription()
            );
        }

        Metadata trailers = grpcException.getTrailers() != null
                ? grpcException.getTrailers()
                : new Metadata();

        call.close(status, trailers);
    }
}