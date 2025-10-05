package com.chat.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // You can add logic here to determine which error page to show
        return "error"; // points to resources/templates/error.html if using Thymeleaf
    }

    public String getErrorPath() {
        return "/error";
    }
}