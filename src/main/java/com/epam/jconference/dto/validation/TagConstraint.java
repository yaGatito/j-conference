package com.epam.jconference.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TagValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TagConstraint {
    String message() default "invalid tag";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
