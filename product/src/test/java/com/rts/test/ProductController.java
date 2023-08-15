//package com.rts.controller;
//
//import com.rts.serve.AdminService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//
//@RestController
//public class ProductController {
////    @Resource
////    RestTemplate restTemplate;
//    @Resource
//    AdminService adminService;
//    @GetMapping("index")
//    String index() {
////        RestTemplate restTemplate = new RestTemplate();
//        // admin 服务名
////        String str = restTemplate.getForObject("http://admin/index", String.class);
//        String str = adminService.adminIndex();
//        System.out.println("请求admin中的方法：" + str);
//        return str;
//    }
//}
