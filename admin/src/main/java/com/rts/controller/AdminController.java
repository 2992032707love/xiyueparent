package com.rts.controller;

import com.rts.common.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class AdminController {
    @GetMapping("/index123")
    ResultJson index() {
        log.info("这里是admin");
        return ResultJson.success("旭旭宝宝index111111111");
    }
}
