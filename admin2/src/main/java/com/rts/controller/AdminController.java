package com.rts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/index")
    String index() {
        return "旭旭宝宝index222222222";
    }
}
