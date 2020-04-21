package com.helper.uuid;

import java.util.UUID;

/**
 * @author sz_qiuhf@163.com
 **/
public class UUIDUtil {

    private UUIDUtil() {
    }

    public static String createUuid() {
        return UUID.randomUUID().toString();
    }

    public static String createUuid32() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
