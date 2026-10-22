package org.frekost.micro.client;

import org.frekost.micro.services.greeter.GreeterGrpc;
import org.frekost.micro.services.greeter.HelloReply;
import org.frekost.micro.services.greeter.HelloRequest;

import io.grpc.ManagedChannel;
import io.micronaut.grpc.annotation.GrpcChannel;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class HelloClient implements AutoCloseable {

	private final GreeterGrpc.GreeterBlockingStub blockingStub;
	private ManagedChannel channel;
	
//    //costruttore per i test manuali
//    public HelloClient(String host, int port) {
//        ManagedChannel channel = ManagedChannelBuilder
//            .forAddress(host, port)
//            .usePlaintext() // se non usi TLS
//            .build();
//        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
//        this.channel = channel;
//    }

	@Inject
	public HelloClient(@GrpcChannel("greeter") ManagedChannel channel) {
		this.blockingStub = GreeterGrpc.newBlockingStub(channel);
		this.channel = channel;
	}

	public String sayHello(String name) {
		HelloRequest request = HelloRequest.newBuilder().setName(name).build();
		HelloReply reply = blockingStub.sayHello(request);
		return reply.getMessage();
	}
	
    @Override
    public void close() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}
