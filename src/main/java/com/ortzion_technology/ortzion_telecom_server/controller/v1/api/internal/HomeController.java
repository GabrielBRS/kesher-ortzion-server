package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "EC2! v1.0.3";
    }

}
