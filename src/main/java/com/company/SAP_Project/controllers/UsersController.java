package com.company.SAP_Project.controllers;
import com.company.SAP_Project.dtoObjects.CategoryDto;
import com.company.SAP_Project.models.Category;
import com.company.SAP_Project.models.User;
import com.company.SAP_Project.services.AuthService;
import com.company.SAP_Project.services.CategoryService;
import com.company.SAP_Project.services.MyUserDetailsService;
import com.company.SAP_Project.services.CustomServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.List;

@Controller
public class UsersController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/users")
    public ModelAndView getUsers()
    {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("users");
        mav.addObject("isAuthenticated", authService.isAuthenticated());
        mav.addObject("isAdmin", authService.isAdmin());
        mav.addObject("isOwner", authService.isOwner());
        if(authService.isAuthenticated())
        {
            mav.addObject("username", authService.getAuthUsername());
        }

        List<User> users = userService.getUsers();
        if(users.isEmpty())
        {
            mav.addObject("serverInfo", "No registered users in the table");
            return mav;
        }
        mav.addObject("users", users);

        return mav;
    }

    @PostMapping(value = "/users")
    public ModelAndView promoteOrDemoteUser(
            @RequestParam(value = "promotedName", required = false) String promotedName,
            @RequestParam(value = "demotedName", required = false) String demotedName,
            RedirectAttributes redirectAttributes)
    {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/users");
        if(!authService.isOwner())
        {
            redirectAttributes.addFlashAttribute("serverError",
                    "You do not have permission to manage users");
            return mav;
        }

        User managedUser = userService.getUserByUsername(promotedName);

        if (managedUser != null)
        {
            userService.addAdmin(promotedName);
            redirectAttributes.addFlashAttribute("serverSuccess",
                    String.format("You have successfully promoted %s", promotedName));
            return mav;
        }

        managedUser = userService.getUserByUsername(demotedName);

        if (managedUser != null)
        {
            userService.removeAdmin(demotedName);
            redirectAttributes.addFlashAttribute("serverSuccess",
                    String.format("You have successfully demoted %s", demotedName));
            return mav;
        }

        redirectAttributes.addFlashAttribute("serverError",
                "You cannot find specified user");
        return mav;
    }
}