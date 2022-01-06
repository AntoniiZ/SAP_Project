package com.company.SAP_Project.services;

import com.company.SAP_Project.models.Posting;
import com.company.SAP_Project.models.PostingFavourite;
import com.company.SAP_Project.models.User;
import com.company.SAP_Project.repositories.PostingFavouriteRepository;
import com.company.SAP_Project.repositories.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PostingFavouriteService {

    @Autowired
    private PostingFavouriteRepository postingFavouriteRepository;

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private MyUserDetailsService userService;

    public List<Posting> getFavouritePostingsByUsername(String username)
    {
        return postingRepository.getFavouritePostingsByUsername(username);
    }

    public void deleteUserPostingFavourite(String username, Long postingId)
    {
        User user = userService.getUserByUsername(username);
        if(user == null) return;
        postingFavouriteRepository.deletePostingFavouriteByUsername(user.getId(), postingId);
    }

    public void addUserPostingFavourite(User user, Posting posting)
    {
        PostingFavourite pf = new PostingFavourite();
        pf.setUser(user);
        pf.setPosting(posting);

        postingFavouriteRepository.save(pf);
    }

    public boolean findFavoriteByUsernameAndPostingId(String username, Long postingId)
    {
        return postingFavouriteRepository.findFavoriteByUsernameAndPostingId(username, postingId) != null;
    }



}