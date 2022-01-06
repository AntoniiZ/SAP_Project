package com.company.SAP_Project.controllers;
import com.company.SAP_Project.events.OnRegistrationCompleteEvent;
import com.company.SAP_Project.dtoObjects.UserDto;
import com.company.SAP_Project.models.User;
import com.company.SAP_Project.services.AuthService;
import com.company.SAP_Project.services.MyUserDetailsService;
import com.company.SAP_Project.services.CustomServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.List;

@Controller
public class RegisterController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/register")
    public ModelAndView showRegister(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        if(authService.isAuthenticated())
        {
            mav.setViewName("redirect:/");
            redirectAttributes.addFlashAttribute("serverError",
                    "You are already logged in!");
            return mav;
        }
        mav.setViewName("register");
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView registerUserAccount(
            @Valid @ModelAttribute(value = "user") UserDto userDto,
            BindingResult result,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes)
    {
        ModelAndView mav = new ModelAndView();

        if(authService.isAuthenticated())
        {
            mav.setViewName("redirect:/");
            redirectAttributes.addFlashAttribute("serverError",
                    "You are already logged in!");
            return mav;
        }
        try {
            if(result.hasErrors())
            {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                //errorMessages.forEach(System.out::println);
                return mav.addObject("formErrors", errorMessages)
                        .addObject("email", userDto.getEmail())
                        .addObject("username", userDto.getUsername())
                        .addObject("password", userDto.getPassword());
            }
            User user = userService.registerNewUserAccount(userDto);

            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                    request.getLocale(), request.getContextPath()));

            redirectAttributes.addFlashAttribute("serverInfo",
                    "Success! Please check your email to finish registration!");

            mav.setViewName("redirect:/");

            return mav;

        }
        catch (CustomServiceException e)
        {
            mav.addObject("email", userDto.getEmail())
                    .addObject("username", userDto.getUsername())
                    .addObject("password", userDto.getPassword())
                    .addObject("serverError", e.getMessage());
        }
        return mav;
    }
}