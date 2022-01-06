
package com.company.SAP_Project.repositories;

import com.company.SAP_Project.models.PostingFavourite;
import com.company.SAP_Project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    @Query("select u from User u")
    List<User> getUsers();

    @Modifying
    @Query("update User u set u.roles = ?2 where u.username = ?1")
    void setUserRole(String username, String role);


}