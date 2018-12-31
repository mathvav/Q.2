package net.karlshaffer.q.utility;

import java.util.Random;

public class RandomUtility {

    private static Random random = new Random();

    public static String generateRandomAlphabeticString(int length) {
        StringBuilder toReturn = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int rValue = random.nextInt(26);
            char c = (char) (rValue + 65);
            toReturn.append(c);
        }

        return toReturn.toString();
    }
}