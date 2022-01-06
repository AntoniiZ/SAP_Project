
package com.company.SAP_Project.repositories;

import com.company.SAP_Project.models.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {

    @Query("select p from Posting p where p.active = ?1")
    List<Posting> getPostings(Boolean filterByActive);

    @Query(value = "select p.* from Posting p where p.category_id = ?1 AND p.active = ?2", nativeQuery = true)
    List<Posting> getPostingsByCategoryName(String categoryName, Boolean filterByActive);

    @Query(value = "select p.* from Posting p LEFT JOIN User u ON u.id = p.user_id where u.username = ?1 AND p.active = ?2",
        nativeQuery = true)
    List<Posting> getPostingsByUsername(String username, Boolean filterByActive);

    @Query("select p from Posting p where p.dateOfCreation >= ?1 AND p.dateOfCreation <= ?2 AND p.active = ?3")
    List<Posting> getNewlyPublishedPostings(LocalDateTime dateFrom, LocalDateTime dateTo, Boolean filterByActive);

    @Query(value = "select p.* from Posting p LEFT JOIN PostingFavourite f ON p.id = f.posting_id " +
            "LEFT JOIN User u ON u.id = f.user_id where u.username = ?1",
            nativeQuery = true)
    List<Posting> getFavouritePostingsByUsername(String username);

    @Modifying
    @Query(value = "update Posting p set p.title = ?1, p.description = ?2, p.category_id = ?3 where p.id = ?4",
            nativeQuery = true)
    void updatePostingById(String title, String description, Long categoryId, Long id);


    @Modifying
    @Query(value = "update Posting p set p.active = ?2 where p.id = ?1",
            nativeQuery = true)
    void setActive(Long postingId, Boolean active);

    @Modifying
    @Query(value = "delete from Posting p where p.category_id = ?1", nativeQuery = true)
    void deleteByCategoryId(Long categoryId);

}