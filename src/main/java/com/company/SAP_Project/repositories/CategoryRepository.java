
package com.company.SAP_Project.repositories;

import com.company.SAP_Project.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c")
    List<Category> getCategories();

    @Query("select c from Category c where c.name = ?1")
    Category findByName(String name);

    @Modifying
    @Query("update Category c set c.description = ?2 where c.name = ?1")
    void updateCategoryByName(String name, String description);

}