package com.example.circuitbreakerdemoclient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MyController {

    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

    @RequestMapping("/to-read")
    public Mono<String> toRead() {
        return myService.readingList();
    }
}
