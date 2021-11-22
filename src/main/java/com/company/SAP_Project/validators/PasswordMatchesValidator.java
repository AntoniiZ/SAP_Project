package com.company.SAP_Project.validators;

import com.company.SAP_Project.annotations.PasswordMatches;
import com.company.SAP_Project.models.UserModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserModel user = (UserModel) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}