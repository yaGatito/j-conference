package com.epam.jconference.dto.validation.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Objects;

public class EnumValidator implements ConstraintValidator<EnumConstraint, String> {

    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(EnumConstraint constraintAnnotation) {
        enumClass = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(name)) {
            return false;
        }
        name = name.toUpperCase(Locale.ROOT);
        return new ConstantEnum(name, enumClass).exists();
    }
}
