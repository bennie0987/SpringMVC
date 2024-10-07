package com.app.springmvc.module.geo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/geo")
public class GeoController {
	@GetMapping({ "", "/" })
	public String showGeo(Model model) {
		
	    return "geo/index";
	}
}
