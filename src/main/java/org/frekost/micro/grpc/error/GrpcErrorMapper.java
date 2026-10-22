package org.frekost.micro.grpc.error;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import org.frekost.micro.grpc.exception.BusinessException;
import org.frekost.micro.grpc.exception.InvalidRequestException;
import org.frekost.micro.grpc.exception.ResourceNotFoundException;

import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;

public final class GrpcErrorMapper {

    private GrpcErrorMapper() {
        // Utility class
    }

    public static StatusRuntimeException toGrpcError(Throwable exception) {
        Throwable cause = unwrap(exception);

        // Preserve an existing gRPC status.
        if (cause instanceof StatusRuntimeException grpcException) {
            return grpcException;
        }

        if (cause instanceof StatusException grpcException) {
            return grpcException.getStatus()
                    .withCause(cause)
                    .asRuntimeException(grpcException.getTrailers());
        }

        if (cause instanceof InvalidRequestException) {
            return Status.INVALID_ARGUMENT
                    .withDescription(safeMessage(cause))
                    .withCause(cause)
                    .asRuntimeException();
        }

        if (cause instanceof ResourceNotFoundException) {
            return Status.NOT_FOUND
                    .withDescription(safeMessage(cause))
                    .withCause(cause)
                    .asRuntimeException();
        }

        if (cause instanceof BusinessException) {
            return Status.FAILED_PRECONDITION
                    .withDescription(safeMessage(cause))
                    .withCause(cause)
                    .asRuntimeException();
        }

        // Never expose internal exception details to the client.
        return Status.INTERNAL
                .withDescription("Internal server error")
                .withCause(cause)
                .asRuntimeException();
    }

    private static Throwable unwrap(Throwable exception) {
        Throwable current = exception;

        while ((current instanceof CompletionException
                || current instanceof ExecutionException)
                && current.getCause() != null) {
            current = current.getCause();
        }

        return current;
    }

    private static String safeMessage(Throwable exception) {
        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            return "Request could not be processed";
        }

        return message;
    }
}