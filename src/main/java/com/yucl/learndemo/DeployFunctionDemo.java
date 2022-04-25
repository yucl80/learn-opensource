package com.yucl.learndemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@SpringBootApplication
public class DeployFunctionDemo {

    public static void mainx(String[] args) {
        ApplicationContext context = SpringApplication.run(DeployFunctionDemo.class,
                "--spring.cloud.function.location=..../target/learn-demo-0.0.1-SNAPSHOT.jar",
                "--spring.cloud.function.definition=uppercase");

        FunctionCatalog catalog = context.getBean(FunctionCatalog.class);
        Function<Flux<String>, Flux<String>> function = catalog.lookup("uppercase1");
        function.apply(Flux.just("sdf","sdfsd")).toStream().forEach(s-> System.out.println(s));
    }

    public static void main(String[] argss) {
        String[] args = new String[] {
                "--spring.cloud.function.location=maven://com.yucl:learn-demo:0.0.1-SNAPSHOT",
                "--spring.cloud.function.function-class=com.yucl.learndemo.LearnDemoApplication" };

        ApplicationContext context = SpringApplication.run(DeployFunctionDemo.class, args);
        FunctionCatalog catalog = context.getBean(FunctionCatalog.class);
        Function<String, String> function = catalog.lookup("uppercase");
        System.out.println(function.apply("bob"));

    }
}