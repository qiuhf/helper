package com.helper.dataparser.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.util.TypeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author @author sz_qiuhf@163.com
 **/
public class RecursionParser {

    public List<Object> parse(String text) {
        Object json = JSON.parse(text, Feature.OrderedField);
        List<Object> list = new ArrayList<>();
        getAllValues(json, list);
        return list;
    }

    private void getAllValues(Object object, List<Object> values) {
        if (Objects.isNull(object)) {
            values.add(null);
            return;
        }
        if (object instanceof JSONObject) {
            JSONObject jsonObject = JSONObject.parseObject(object.toString(), Feature.OrderedField);
            values.add(jsonObject);
            jsonObject.forEach((key, value) -> getAllValues(value, values));
        } else if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) JSONArray.parse(object.toString(), Feature.OrderedField);
            values.add(jsonArray);
            jsonArray.forEach(element -> getAllValues(element, values));
        } else {
            values.add(TypeUtils.cast(object, object.getClass(), null));
        }
    }
}
