package com.epam.jconference.dto.validation.strings;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringItemValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
/**
 * Used to validate specified strings in com.epam.jconference.dto.validation.string.StringItem
 * When the string is null return true
 */
public @interface ValidateString {
    StringItem value();

    String message() default "invalid string";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
