package org.iota.ict.ixi.util;

import java.security.SecureRandom;

public class Generator {

    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ9";
    private static SecureRandom r = new SecureRandom();

    public static String getRandomHash(){
        StringBuilder sb = new StringBuilder(81);
        for( int i = 0; i < 81; i++ )
            sb.append( alphabet.charAt( r.nextInt(alphabet.length()) ) );
        return sb.toString();
    }

}
