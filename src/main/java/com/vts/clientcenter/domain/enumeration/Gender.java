package com.vts.clientcenter.domain.enumeration;

public enum Gender {
    Female("Female"),
    Male("Male"),
    Common("Common");

    private String value;

    private Gender(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public static Gender fromValue(String value) {
        if (value != null && !"".equals(value)) {
            Gender[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Gender enumEntry = var1[var3];
                if (enumEntry.toString().equals(value)) {
                    return enumEntry;
                }
            }

            throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
        } else {
            throw new IllegalArgumentException("Value cannot be null or empty!");
        }
    }
}
