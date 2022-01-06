package com.company.SAP_Project.services;

import com.company.SAP_Project.dtoObjects.CategoryDto;
import com.company.SAP_Project.repositories.CategoryRepository;
import com.company.SAP_Project.models.Category;
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
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostingService postingService;

    public List<Category> getCategories()
    {
       List<Category> categories = categoryRepository.getCategories();
       if(categories  == null) categories = new ArrayList<>();
       return categories;
    }

    public Category findCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    public Category getCategoryByName(String categoryName) throws CustomServiceException {
        Category category = findCategoryByName(categoryName);
        if(category == null) throw new CustomServiceException("Category with that name is not found!");
        return category;
    }

    public void addNewCategory(CategoryDto categoryDto) throws CustomServiceException {
        if(findCategoryByName(categoryDto.getName()) != null)
        {
            throw new CustomServiceException("Category with that name already exists!");
        }

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setDateOfCreation(ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
        categoryRepository.save(category);
    }

    public void updateCategory(CategoryDto categoryDto)
    {
        if(findCategoryByName(categoryDto.getName()) == null)
        {
            return;
        }

        categoryRepository.updateCategoryByName(categoryDto.getName(), categoryDto.getDescription());

    }
    public void deleteCategory(String categoryName) throws CustomServiceException {
        categoryRepository.delete(getCategoryByName(categoryName));

    }


}