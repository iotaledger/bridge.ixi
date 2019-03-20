package org.iota.ict.ixi.util;

public class InputValidator {

    public static boolean isValidHash(String hash) {
        if(hash == null)
            return false;
        return hash.matches("^[A-Z9]{81,81}$");
    }

    public static boolean areValidHashes(String[] hashes) {
        if(hashes == null)
            return false;
        if(hashes.length == 0)
            return false;
        for(String hash: hashes) {
            if(!isValidHash(hash))
                return false;
        }
        return true;
    }

}