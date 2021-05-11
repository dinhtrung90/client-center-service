package com.vts.clientcenter.helpers;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%^&*()~";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    // optional, make it more random
    private static final String PASSWORD_ALLOW_BASE_SHUFFLE = shuffleString(PASSWORD_ALLOW_BASE);
    private static final String PASSWORD_ALLOW = PASSWORD_ALLOW_BASE_SHUFFLE;

    private static SecureRandom random = new SecureRandom();

    public static String generateRandomPassword(int length) throws Exception {


        StringBuilder sb = new StringBuilder(length);

        //length must be more than 8
        if (length < 8) {
            throw new Exception("Password must be equal or more than 8");
        }

        // 4 character low case
        for (int i = 0; i < 4; i++) {
            int rndCharAt = random.nextInt(CHAR_LOWER.length());
            char rndChar = CHAR_LOWER.charAt(rndCharAt);
            sb.append(rndChar);
        }

        // 1 upper case
        int rndCharAt = random.nextInt(CHAR_UPPER.length());
        char rndChar = CHAR_UPPER.charAt(rndCharAt);
        sb.append(rndChar);

        // 1 special char
        int specialCharAt = random.nextInt(OTHER_CHAR.length());
        char specialChar = OTHER_CHAR.charAt(specialCharAt);
        sb.append(specialChar);

        // 2 number char
        for (int i = 0; i < 2; i++) {
            int numberAt = random.nextInt(NUMBER.length());
            char numberChar = NUMBER.charAt(numberAt);
            sb.append(numberChar);
        }
        return shuffleString(sb.toString());

    }

    // shuffle
    public static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return letters.stream().collect(Collectors.joining());
    }
}
