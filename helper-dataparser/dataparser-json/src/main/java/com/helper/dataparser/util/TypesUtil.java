package com.helper.dataparser.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author sz_qiuhf@163.com
 **/
public class TypesUtil {

    private static Map<String, Class> mapping = new HashMap<>(1 << 4);

    static {
        mapping.put("String", String.class);
        mapping.put("Integer", Integer.class);
        mapping.put("Double", Double.class);
        mapping.put("Boolean", Boolean.class);
        mapping.put("JSONObject", JSONObject.class);
        mapping.put("JSONArray", JSONArray.class);
    }

    private TypesUtil() {
    }

    public static Class getClass(String type) {
        return Optional.ofNullable(mapping.get(type)).orElse(Object.class);
    }
}
