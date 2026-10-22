package org.frekost.micro;

import io.micronaut.context.env.Environment;
import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {
        var ctx = Micronaut.run(Application.class, args);
        var env = ctx.getBean(Environment.class);
        System.out.println("📘 grpc.server.port letto: " + env.getProperty("grpc.server.port", Integer.class).orElse(-1));
    }
}
