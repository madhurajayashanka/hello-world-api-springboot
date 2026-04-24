package com.typeb.hello_world_api.controller;

import com.typeb.hello_world_api.dto.HelloResponse;
import com.typeb.hello_world_api.service.HelloWorldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;

@RestController
@Validated
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping("/hello-world")
    public ResponseEntity<HelloResponse> helloWorld(
            @RequestParam(value = "name") @NotBlank String name) {

        String displayName = helloWorldService.greet(name);
        return ResponseEntity.ok(new HelloResponse("Hello " + displayName));
    }
}