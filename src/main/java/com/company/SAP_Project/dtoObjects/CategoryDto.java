package com.company.SAP_Project.dtoObjects;

import javax.validation.constraints.*;

public class CategoryDto {

    @Size(min=3, max=30, message = "Your category name length must be 3-30 chars!")
    private String name;

    @Size(min=10, max=200, message = "Your category description length must be 10-200 chars!")
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}