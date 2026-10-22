package org.frekost.micro.services.grpc.greeter;

import org.frekost.micro.grpc.exception.InvalidRequestException;
import org.frekost.micro.services.greeter.GreeterGrpc;
import org.frekost.micro.services.greeter.HelloReply;
import org.frekost.micro.services.greeter.HelloRequest;

import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;

@GrpcService
public class HelloServiceImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(
            HelloRequest request,
            StreamObserver<HelloReply> responseObserver) {

        String name = request.getName();

        if (name == null || name.isBlank()) {
            throw new InvalidRequestException(
                    "The name must not be empty"
            );
        }

        String message = "Hello, " + name + "!";

        HelloReply reply = HelloReply.newBuilder()
                .setMessage(message)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}