package it.buniva.strage.constant;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordConstant {

    public static final char[] SYMBOLS = "@#$%&-+=()".toCharArray();

    public static final char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static final char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static final char[] NUMBERS = "0123456789".toCharArray();

    public static final char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&-+=()".toCharArray();

    public static Random rand = new SecureRandom();
}
