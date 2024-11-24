package com.example.KS_63875_Library_SpringBoot.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String getMain(Model model){
        model.addAttribute("title", "Main page");
        return "main";
    }
}
