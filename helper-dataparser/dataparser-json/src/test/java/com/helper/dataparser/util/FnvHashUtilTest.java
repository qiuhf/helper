package com.helper.dataparser.util;

import org.junit.Test;

public class FnvHashUtilTest {

    @Test
    public void fnv1a64() {
        System.out.println(FnvHashUtil.fnv1a64("0" + "1"));
        System.out.println(FnvHashUtil.fnv1a64("" + "01"));
    }
}
