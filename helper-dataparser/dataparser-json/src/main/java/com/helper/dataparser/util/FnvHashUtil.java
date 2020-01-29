package com.helper.dataparser.util;

/**
 * @author sz_qiuhf@163.com
 **/
public final class FnvHashUtil {
    private final static long BASIC = 0xcbf29ce484222325L;
    private final static long PRIME = 0x100000001b3L;

    public static long fnv1a64(String str1, String str2) {
        return fnv1a64(str1 + "," + str2);
    }

    public static long fnv1a64(String input) {
        if (input == null) {
            return 0;
        }

        long hash = BASIC;
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            hash ^= c;
            hash *= PRIME;
        }
        return hash;
    }
}
