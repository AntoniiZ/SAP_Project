
package com.company.SAP_Project.repositories;

import java.util.List;

import com.company.SAP_Project.models.Posting;
import com.company.SAP_Project.models.PostingFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingFavouriteRepository extends JpaRepository<PostingFavourite, Long> {

    @Query(value = "select f.* from PostingFavourite f LEFT JOIN User u ON u.id = f.user_id " +
            "where u.username = ?1 AND f.posting_id = ?2",
            nativeQuery = true)
    PostingFavourite findFavoriteByUsernameAndPostingId(String username, Long postingId);

    @Modifying
    @Query(value = "delete f from PostingFavourite f where f.user_id = ?1 AND f.posting_id = ?2",
        nativeQuery = true)
    void deletePostingFavouriteByUsername(Long userId, Long postingId);


}