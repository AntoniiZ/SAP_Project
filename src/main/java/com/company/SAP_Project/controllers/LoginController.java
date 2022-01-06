package com.company.SAP_Project.controllers;
import com.company.SAP_Project.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public ModelAndView loginIntoAccount(RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();

        if(authService.isAuthenticated())
        {
            mav.setViewName("redirect:/");
            redirectAttributes.addFlashAttribute("serverError",
                    "You are already logged in!");
            return mav;
        }
        mav.setViewName("login");
        return mav;
    }

}