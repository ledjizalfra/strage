package it.buniva.strage.utility;

import org.apache.commons.lang3.RandomStringUtils;

public class MyStringUtil {

    public static String generateRandomNumericString(int length){
        return RandomStringUtils.random(length, false, true);
    }

    public static char[] replace(char[] charArray, int index, char replace){

        if(charArray == null) {
            return new char[0];
        }else if( (index < 0) || (index >= charArray.length) ) {
            return charArray;
        }
        StringBuilder  strBuilder = new StringBuilder(String.valueOf(charArray));
        strBuilder.setCharAt(index, replace);

        return (new String(strBuilder)).toCharArray();
    }
}
