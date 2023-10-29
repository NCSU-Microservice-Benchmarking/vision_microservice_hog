package com.springboot.springfirstapp;

import org.opencv.core.Mat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.springfirstapp.service.HogService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

class PostRequestBody {
    String key1;
    String key2;
}

@RestController
public class HOGController {
    
    @Autowired
    private HogService myHogService;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/model-hog-people")
    @Operation(summary = "Returns the detected image", 
                description = "Receives an image and returns the detected result")
    public String HogMethod(@RequestBody PostRequestBody requestBody) {
        System.out.println("Hello this is the requestbody: " + requestBody.key1);
        //Mat m = new Mat();
        return myHogService.ProcessImage("hello");
        //return requestBody.key1;
    }
}