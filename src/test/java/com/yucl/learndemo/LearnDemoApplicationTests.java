package com.yucl.learndemo;

import org.assertj.core.api.AbstractBooleanArrayAssert;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.locks.LockSupport;


class LearnDemoApplicationTests {

    @Test
    void contextLoads() {
        Flux<String> ids = Flux.just("asdf", "asdfasd", "asd", "dfdf", "sdfd");
        System.out.println(Thread.currentThread().getId());
        ParallelFlux<String> combinations = ids.parallel(2).runOn(Schedulers.parallel()).flatMap(id -> {
            Mono<String> nameTask = ifhrName(id);
            Mono<Integer> statTask = ifhrStat(id);

            return nameTask.zipWith(statTask,
                    (name, stat) -> "Name " + name + " has stats " + stat + "::" + Thread.currentThread().getId());
        });

        combinations.subscribe(System.out::println);

        LockSupport.parkNanos(1000000);
/*
        Mono<List<String>> result = combinations.collectList();

        List<String> results = result.block();
        System.out.println(results);*/


        Flux.range(1, 10)
                .parallel(2)
                .runOn(Schedulers.parallel())
                .map(i -> i + 1)
                .map(i -> i * 2)
                .map(i -> i + 1)
                .subscribe(System.out::println);
    }

    private Mono<Integer> ifhrStat(String id) {
        LockSupport.parkNanos(20000);
        System.out.println(Thread.currentThread().getName());
        return Mono.just(1);
    }

    private Mono<String> ifhrName(String id) {
        LockSupport.parkNanos(10000);
        System.out.println(Thread.currentThread().getId());
        return Mono.just(id.toUpperCase());
    }

}
