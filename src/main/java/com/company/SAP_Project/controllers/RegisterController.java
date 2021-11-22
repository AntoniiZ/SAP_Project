package com.company.SAP_Project.controllers;
import com.company.SAP_Project.events.OnRegistrationCompleteEvent;
import com.company.SAP_Project.models.UserModel;
import com.company.SAP_Project.repositories.tables.User;
import com.company.SAP_Project.services.MyUserDetailsService;
import com.company.SAP_Project.services.UserServiceException;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.List;

@Controller
public class RegisterController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/register")
    public ModelAndView showRegister(HttpServletRequest request, Model model) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("register");
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView registerUserAccount(
            @Valid @ModelAttribute(value = "user") UserModel userModel, BindingResult result, HttpServletRequest request)
    {
        ModelAndView mav = new ModelAndView();
        try {
            if(result.hasErrors())
            {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                errorMessages.forEach(System.out::println);
                return mav.addObject("messages", errorMessages)
                        .addObject("email", userModel.getEmail())
                        .addObject("username", userModel.getUsername());
            }
            User user = userService.registerNewUserAccount(userModel);

            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                    request.getLocale(), request.getContextPath()));

            mav.addObject("emailSent", true).setViewName("redirect:/");

        }
        catch (UserServiceException e)
        {
            mav.addObject("message", e.getMessage());
        }
        return mav;
    }
}