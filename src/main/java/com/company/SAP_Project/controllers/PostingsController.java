package com.company.SAP_Project.controllers;
import com.company.SAP_Project.dtoObjects.CategoryDto;
import com.company.SAP_Project.dtoObjects.PostingDto;
import com.company.SAP_Project.models.Category;
import com.company.SAP_Project.models.Posting;
import com.company.SAP_Project.models.PostingFavourite;
import com.company.SAP_Project.models.User;
import com.company.SAP_Project.services.*;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.List;

@Controller
public class PostingsController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostingService postingService;

    @Autowired
    private PostingFavouriteService postingFavouriteService;

    @GetMapping("/postings")
    public ModelAndView getPostings(
            @RequestParam(value = "updateId", required = false) Long updateId,
            @RequestParam(value = "postingFilter", required = false) String postingFilter)
    {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("postings");

        mav.addObject("isAuthenticated", authService.isAuthenticated());
        mav.addObject("isAdmin", authService.isAdmin());
        mav.addObject("categories", categoryService.getCategories());
        mav.addObject("favorites", postingFavouriteService);
        mav.addObject("isUpdating", updateId != null);

        if(authService.isAuthenticated())
        {
            mav.addObject("username", authService.getAuthUsername());
        }
        if(updateId != null)
        {
            Posting posting = postingService.getPostingById(updateId);
            if(posting == null) {
                mav.addObject("serverError", "Cannot find such posting with this id");
            } else {
                mav.addObject("updatingId", updateId)
                    .addObject("title", posting.getTitle())
                    .addObject("description", posting.getDescription())
                    .addObject("categoryName", posting.getCategory().getName());
            }
        }
        if(postingFilter == null)
        {
            mav.addObject("postings", postingService.getPostings(true));
            return mav;
        }

        if(postingFilter.equals("inactive"))
        {
            mav.addObject("postings", postingService.getPostings(false));
            return mav;
        }

        if(postingFilter.equals("own") && authService.isAuthenticated())
        {
            mav.addObject("postings",
                    postingService.getPostingsByUsername(authService.getAuthUsername(), true));
            return mav;
        }
        if(postingFilter.equals("new"))
        {
            LocalDateTime to = ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime from = ZonedDateTime.now(ZoneId.systemDefault()).minusSeconds(30).toLocalDateTime();
            mav.addObject("postings",
                    postingService.getNewlyPublishedPostings(from, to, true));
            return mav;
        }

        if(postingFilter.equals("favorited") && authService.isAuthenticated())
        {
            mav.addObject("postings",
                    postingFavouriteService.getFavouritePostingsByUsername(authService.getAuthUsername()));
            return mav;
        }


        return mav;
    }

    @PostMapping(value = "/postings")
    public ModelAndView addPosting(
            @RequestParam(value = "updateId", required = false) Long updateId,
            @RequestParam(value = "deactivateId", required = false) Long deactivateId,
            @RequestParam(value = "favId", required = false) Long favId,
            @RequestParam(value = "delFavId", required = false) Long delFavId,
            @Valid @ModelAttribute(value = "posting") PostingDto postingDto,
            BindingResult result,
            RedirectAttributes redirectAttributes)
    {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/postings");

        if(!authService.isAuthenticated())
        {
            redirectAttributes.addFlashAttribute("serverError",
                    "You need to log in to perform this operation"
            );
            return mav;
        }
        if(deactivateId != null)
        {
            Posting posting = postingService.getPostingById(deactivateId);
            if(posting == null)
            {

                redirectAttributes.addFlashAttribute("serverError",
                        "Specified posting doesn't exist!"
                );
                return mav;
            }
            postingService.setActive(posting.getId(), false);
            return mav;
        }
        if(favId != null)
        {
            Posting posting = postingService.getPostingById(favId);
            if(posting == null)
            {

                redirectAttributes.addFlashAttribute("serverError",
                        "Specified posting doesn't exist!"
                );
                return mav;
            }
            postingFavouriteService.addUserPostingFavourite(
                    userService.getUserByUsername(authService.getAuthUsername()),
                    postingService.getPostingById(favId)
            );

            return mav;
        }

        if(delFavId != null)
        {
            Posting posting = postingService.getPostingById(delFavId);
            if(posting == null)
            {

                redirectAttributes.addFlashAttribute("serverError",
                        "Specified posting doesn't exist!"
                );
                return mav;
            }
            postingFavouriteService.deleteUserPostingFavourite(
                    authService.getAuthUsername(), delFavId
            );

            return mav;
        }
        if(result.hasErrors())
        {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            //System.out.println(postingDto.getCategoryName());
            redirectAttributes.addFlashAttribute("formErrors", errorMessages)
                    .addFlashAttribute("title", postingDto.getTitle())
                    .addFlashAttribute("description", postingDto.getDescription())
                    .addFlashAttribute("categoryName", postingDto.getCategoryName());
            return mav;
        }

        if(updateId == null)
        {
            postingService.addNewPosting(postingDto);

            redirectAttributes.addFlashAttribute("serverSuccess",
                    "You have successfully added your posting!"
            );
            return mav;
        }

        postingService.updatePostingById(postingDto, updateId);

        redirectAttributes.addFlashAttribute("serverSuccess",
                "You have successfully updated your posting!"
        );
        return mav;
    }
    
}