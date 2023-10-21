package com.springboot.springfirstapp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HOGController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
// can i make changes to the spring-first-app and use it as the foundation for the microservice