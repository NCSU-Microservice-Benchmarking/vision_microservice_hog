package com.springboot.springfirstapp;

import org.opencv.core.Mat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.springfirstapp.service.HogService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

class MyRequest {
    private byte[] image;

    public MyRequest() {}
    public MyRequest(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
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
    public String HogMethod(@RequestBody MyRequest requestBody) {
        System.out.println("Hello this is the requestbody: ");
        //Mat m = new Mat();
        return myHogService.ProcessImage("hello");
        //return requestBody.key1;
    }
}