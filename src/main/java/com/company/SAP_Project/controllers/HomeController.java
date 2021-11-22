package com.company.SAP_Project.controllers;
import com.company.SAP_Project.repositories.tables.User;
import com.company.SAP_Project.repositories.tables.VerificationToken;
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

    @RequestMapping(value = {"", "/", "home"})
    public ModelAndView home(HttpServletRequest request, Model model, @RequestParam(value = "token", required = false) String token) throws ServletException {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        mav.addObject("activation", false)
                .addObject("isAuthenticated", AuthService.isAuthenticated());
        if(token == null || AuthService.isAuthenticated())
        {
            return mav.addObject("username", AuthService.getAuthUsername());
        }
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null)
        {
            return mav.addObject("tokenErr", "This verification token is not valid!");
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
        {
            return mav.addObject("tokenErr", "This verification token has expired!");
        }

        userService.activate(user);
        return mav.addObject("activation", true);

    }
}