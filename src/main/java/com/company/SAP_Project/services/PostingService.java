package com.company.SAP_Project.services;

import com.company.SAP_Project.dtoObjects.PostingDto;
import com.company.SAP_Project.models.Category;
import com.company.SAP_Project.models.Posting;
import com.company.SAP_Project.repositories.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PostingService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthService authService;

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private PostingRepository postingRepository;

    public List<Posting> getPostings(Boolean filterByActive)
    {
        List<Posting> postings = postingRepository.getPostings(filterByActive);
        if(postings == null) postings = new ArrayList<>();
        return postings;
    }

    public Posting getPostingById(Long id)
    {
        return postingRepository.getById(id);
    }

    public List<Posting> getPostingsByCategoryName(String categoryName, Boolean filterByActive)
    {
        List<Posting> postings = postingRepository.getPostingsByCategoryName(categoryName, filterByActive);
        if(postings == null) postings = new ArrayList<>();
        return postings;
    }

    public List<Posting> getPostingsByUsername(String username, Boolean filterByActive)
    {
        List<Posting> postings = postingRepository.getPostingsByUsername(username, filterByActive);
        if(postings == null) postings = new ArrayList<>();
        return postings;
    }
    public List<Posting> getNewlyPublishedPostings(LocalDateTime dateFrom, LocalDateTime dateTo, Boolean filterByActive)
    {
        List<Posting> postings = postingRepository.getNewlyPublishedPostings(dateFrom, dateTo, filterByActive);
        if(postings == null) postings = new ArrayList<>();
        return postings;
    }

    public void addNewPosting(PostingDto postingDto)
    {
        Posting posting = new Posting();
        posting.setActive(true);
        posting.setTitle(postingDto.getTitle());
        posting.setDescription(postingDto.getDescription());
        posting.setDateOfCreation(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());

        Category category = categoryService.findCategoryByName(postingDto.getCategoryName());

        if(category == null)
        {
            return;
        }
        posting.setCategory(category);
        posting.setUser(userService.getUserByUsername(authService.getAuthUsername()));
        postingRepository.save(posting);
    }

    public void updatePostingById(PostingDto postingDto, Long postingId)
    {
        Category category = categoryService.findCategoryByName(postingDto.getCategoryName());

        if(category == null)
        {
            return;
        }

        postingRepository.updatePostingById(
                postingDto.getTitle(), postingDto.getDescription(), category.getId(), postingId
        );
    }

    public void setActive(Long postingId, Boolean active)
    {
        postingRepository.setActive(postingId, active);
    }

    public void deleteByCategoryId(Long categoryId)
    {
        postingRepository.deleteByCategoryId(categoryId);
    }


}