package com.yucl.learndemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.Mapping;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@SpringBootApplication
public class LearnDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnDemoApplication.class, args);
	}


	/*
	curl -H "Content-Type: text/plain" localhost:8080/uppercase -d Hello
	 */
	@Bean
	public Function<String,String> uppercase(){
		return String::toUpperCase;
	}

	@Bean
	public Function<Flux<String>, Flux<String>> uppercase1() {
		return flux -> flux.map(value -> value.toUpperCase());
	}





}
