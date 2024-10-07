package com.app.springmvc.module.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login(Model model) {
		return "home";
	}
	
	@GetMapping({"/", "/home"})
    public String home(Model model) {
		System.out.println("home");
        return "home";
    }

    @GetMapping("/secure")
    public String securePage(Model model) {
        return "secure";
    }
}
