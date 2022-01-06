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
public class CategoriesController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ModelAndView getCategories()
    {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("categories");
        mav.addObject("isAuthenticated", authService.isAuthenticated());
        mav.addObject("isAdmin", authService.isAdmin());
        mav.addObject("isOwner", authService.isOwner());

        if(authService.isAuthenticated())
        {
            mav.addObject("username", authService.getAuthUsername());
        }

        List<Category> categories = categoryService.getCategories();
        if(categories.isEmpty())
        {
            mav.addObject("serverInfo", "No categories have been added to the list yet");
            return mav;
        }
        mav.addObject("categories", categories);

        return mav;
    }

    @PostMapping(value = "/categories")
    public ModelAndView postCategory(
            @RequestParam(value = "deleteName", required = false) String deleteName,
            @Valid @ModelAttribute(value = "category") CategoryDto categoryDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/categories");

        if(!authService.isAdmin())
        {
            redirectAttributes.addFlashAttribute("serverError",
                    "You don't have permission to manage categories");
            return mav;
        }

        if(deleteName != null)
        {
            try {
                categoryService.deleteCategory(deleteName);
                redirectAttributes.addFlashAttribute("serverSuccess",
                        String.format("You deleted the category %s", deleteName));
            } catch (CustomServiceException ignored) {
                redirectAttributes.addFlashAttribute("serverError",
                        String.format("The category %s doesn't exist!", categoryDto.getName()));
            }
            return mav;
        }

        try {
            if(result.hasErrors())
            {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                redirectAttributes.addFlashAttribute("formErrors", errorMessages)
                        .addFlashAttribute("name", categoryDto.getName())
                        .addFlashAttribute("description", categoryDto.getDescription());
                return mav;
            }

            categoryService.addNewCategory(categoryDto);

            redirectAttributes.addFlashAttribute("serverSuccess",
                    String.format("You have added the category %s", categoryDto.getName()));

            return mav;

        }
        catch (CustomServiceException e)
        {
            categoryService.updateCategory(categoryDto);

            redirectAttributes.addFlashAttribute("serverSuccess",
                    String.format("You have updated the category %s", categoryDto.getName()));
        }
        return mav;
    }
}