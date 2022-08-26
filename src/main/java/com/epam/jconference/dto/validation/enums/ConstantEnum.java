package com.epam.jconference.dto.validation.enums;

import org.springframework.lang.NonNull;

public class ConstantEnum {

    private final Class<? extends Enum> enumClass;
    private final String enumName;

    public ConstantEnum(@NonNull String string, @NonNull Class<? extends Enum> enumClass) {
        this.enumName = string.toUpperCase();
        this.enumClass = enumClass;
    }

    public boolean exists() {
        try {
            Enum.valueOf(enumClass, enumName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
