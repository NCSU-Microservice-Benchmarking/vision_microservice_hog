package com.springboot.springfirstapp;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class HOGController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/model-hog-people")
    @Operation(summary = "Returns the detected image", 
                description = "Receives an image and returns the detected result")
    @RequestBody(description = "Details of request")
    @ApiResponse(responseCode = "200", description = "image in PNG format with people surrounded by bounding boxes", content = @Content(schema = @Schema (type = "string", format = "binary")))
    public String HogMethod() {
        return "something idk";
        

    }
}
// can i make changes to the spring-first-app and use it as the foundation for the microservice