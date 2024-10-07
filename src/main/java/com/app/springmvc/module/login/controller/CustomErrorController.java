package com.app.springmvc.module.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError() {
        // Log the error occurrence
        logger.error("An error occurred.");
        // Return the error view
        return "error";  // Ensure this view exists
    }

    public String getErrorPath() {
        return "/error";
    }
}
