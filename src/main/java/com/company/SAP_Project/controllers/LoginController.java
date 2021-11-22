package com.company.SAP_Project.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping("/login")
    public ModelAndView showLogin() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

}