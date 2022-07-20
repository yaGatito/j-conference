package com.epam.jconference.dto.validation.strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;

public class StringItemValidator implements ConstraintValidator<ValidateString, String> {

    private final String NAME = "^[-' \\p{L}]{2,20}$";
    private final String LASTNAME = "^[-' \\p{L}]{2,20}$";
    private final String TAG = "^\\w{2,20}$";
    private final String LOCATION = "^[.,\\-\\s0-9\\p{L}]{10,100}$";
    private final String TOPIC = "^[\\-\\s0-9\\p{L}]{3,50}$";

    //    private final String PASSWORD = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    private final String PASSWORD = "^\\w{8,20}$";
    private final HashMap<StringItem, String> validations = new HashMap<>();
    private StringItem item;

    @Override
    public void initialize(ValidateString constraintAnnotation) {
        validations.put(StringItem.TAG, TAG);
        validations.put(StringItem.NAME, NAME);
        validations.put(StringItem.LASTNAME, LASTNAME);
        validations.put(StringItem.TOPIC, TOPIC);
        validations.put(StringItem.LOCATION, LOCATION);
        validations.put(StringItem.PASSWORD, PASSWORD);

        item = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null) {
            return true;
        }
        if (validations.containsKey(item)) {
            return string.matches(validations.get(item));
        } else {
            return false;
        }
    }
}
