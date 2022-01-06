package com.company.SAP_Project.dtoObjects;

import javax.validation.constraints.*;

public class PostingDto {

    @Size(min=3, max=30, message = "Your posting title length must be 3-30 chars!")
    private String title;

    @Size(min=15, max=200, message = "Your posting description length must be 15-200 chars!")
    private String description;

    @NotNull(message = "Please select a category that fits the posting!")
    private String categoryName;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}