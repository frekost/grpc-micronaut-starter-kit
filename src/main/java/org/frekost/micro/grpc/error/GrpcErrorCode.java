package org.frekost.micro.grpc.error;

import io.grpc.Status;

public enum GrpcErrorCode {

    INVALID_ARGUMENT(Status.Code.INVALID_ARGUMENT),
    NOT_FOUND(Status.Code.NOT_FOUND),
    FAILED_PRECONDITION(Status.Code.FAILED_PRECONDITION),
    INTERNAL(Status.Code.INTERNAL);

    private final Status.Code statusCode;

    GrpcErrorCode(Status.Code statusCode) {
        this.statusCode = statusCode;
    }

    public Status.Code getStatusCode() {
        return statusCode;
    }
}