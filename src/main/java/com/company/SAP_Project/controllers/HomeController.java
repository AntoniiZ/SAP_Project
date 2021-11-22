package com.company.SAP_Project.controllers;
import com.company.SAP_Project.services.AuthService;
import com.company.SAP_Project.services.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController {
    @RequestMapping(value = {"", "/", "home"})
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        mav.addObject("username", AuthService.getAuthUsername());
        return mav;
    }


}