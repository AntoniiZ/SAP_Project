package com.company.SAP_Project.annotations;

import com.company.SAP_Project.validators.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented

public @interface PasswordMatches {
    String message() default "Your both entered passwords must match!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}