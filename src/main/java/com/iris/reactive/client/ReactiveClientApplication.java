package com.iris.reactive.client;

import com.iris.reactive.model.Event;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.awt.*;
import java.util.Collections;

@SpringBootApplication
public class ReactiveClientApplication {

    @Bean
    WebClient client(){
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    CommandLineRunner demo (WebClient client){
        return args -> {
            client
                    .get()
                    .uri("/events")
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .exchange()
                    .flatMapMany(cr -> cr.bodyToFlux(Event.class))
                    .subscribe(evt -> System.out.println(evt));
        };
    }

    public static void main(String args[]) {
        new SpringApplicationBuilder(ReactiveClientApplication.class)
                .properties(Collections.singletonMap("server.port","8081"))
                .run(args);
    }
}
