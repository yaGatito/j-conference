package com.epam.jconference.dto.validation.enums;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumConstraint {
    String message() default "EnumConstant doesn't exist";

    Class<? extends Enum> value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
