package com.company.SAP_Project.controllers;
import com.company.SAP_Project.models.User;
import com.company.SAP_Project.models.VerificationToken;
import com.company.SAP_Project.services.AuthService;
import com.company.SAP_Project.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
public class HomeController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = {"", "/", "home"})
    public ModelAndView home(HttpServletRequest request,
                             Model model,
                             @RequestParam(value = "token", required = false) String token
    ) throws ServletException {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");

        mav.addObject("isAuthenticated", authService.isAuthenticated());
        mav.addObject("isAdmin", authService.isAdmin());
        mav.addObject("isOwner", authService.isOwner());

        if(token == null || authService.isAuthenticated())
        {
            return mav.addObject("username", authService.getAuthUsername());
        }

        VerificationToken verificationToken = userService.getVerificationToken(token);

        if (verificationToken == null) {
            return mav.addObject("serverError",
                    "This verification token is used or invalid!");
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
        {
            return mav.addObject("serverError",
                    "This verification token has already expired!");
        }
        userService.activate(user, verificationToken.getId());
        return mav.addObject("serverInfo",
                "You have activated your account and can now log in!");

    }
}