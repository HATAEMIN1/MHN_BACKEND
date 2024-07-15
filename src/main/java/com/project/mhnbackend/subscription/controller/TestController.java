package com.project.mhnbackend.subscription.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class TestController {

    @PostMapping("/api/users")
    public String addUser() {
        log.info("add user");log.info("들어옴");
        return "success";
    }
}
