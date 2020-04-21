package com.helper.dataparser.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import org.junit.Test;

import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FnvHashUtilTest {

    @Test
    public void fnv1a64() {
        System.out.println(FnvHashUtil.fnv1a64("0" + "1"));
        System.out.println(FnvHashUtil.fnv1a64("" + "01"));
    }

    final String DEATH_STRING = "{\"a\":\"\\x";

    @Test
    public void test() {
        String line = "[{\\x22a\\x22:\\x22a\\xB1ph.\\xCD\\x86\\xBEI\\xBA\\xC3\\xBCiM+\\xCE\\xCE\\x1E\\xDF7\\x1E\\xD9z\\xD9Q\\x8A}\\xD4\\xB2\\xD5\\xA0y\\x98\\x08@\\xE1!\\xA8\\xEF^\\x0D\\x7F\\xECX!\\xFF\\x06IP\\xEC\\x9F[\\x85;\\x02\\x817R\\x87\\xFB\\x1Ch\\xCB\\xC7\\xC6\\x06\\x8F\\xE2Z\\xDA^J\\xEB\\xBCF\\xA6\\xE6\\xF4\\xF7\\xC1\\xE3\\xA4T\\x89\\xC6\\xB2\\x5Cx]";
        line = line.replaceAll("\\\\x", "%");
        try {
            String decodeLog = URLDecoder.decode(line, "UTF-8");
            JSON.parse(decodeLog);
        } catch (Exception e) {
            assertEquals(e.getClass(), JSONException.class);
            assertTrue(e.getMessage().contains("invalid escape character \\x"));
        }
    }

    @Test
    public void test_OOM() throws Exception {
        String line = "[{\\x22a\\x22:\\x22a\\xB1ph.\\xCD\\x86\\xBEI\\xBA\\xC3\\xBCiM+\\xCE\\xCE\\x1E\\xDF7\\x1E\\xD9z\\xD9Q\\x8A}\\xD4\\xB2\\xD5\\xA0y\\x98\\x08@\\xE1!\\xA8\\xEF^\\x0D\\x7F\\xECX!\\xFF\\x06IP\\xEC\\x9F[\\x85;\\x02\\x817R\\x87\\xFB\\x1Ch\\xCB\\xC7\\xC6\\x06\\x8F\\xE2Z\\xDA^J\\xEB\\xBCF\\xA6\\xE6\\xF4\\xF7\\xC1\\xE3\\xA4T\\x89\\xC6\\xB2\\x5Cx]";
        line = line.replaceAll("\\\\x", "%");
        String decodeLog = URLDecoder.decode(line, "UTF-8");
        System.out.println(decodeLog);
        try {
            System.out.println(JSON.parse(decodeLog));
            System.out.println(JSON.parse(DEATH_STRING));
        } catch (Exception e) {
            assertEquals(e.getClass(), JSONException.class);
            assertTrue(e.getMessage().contains("invalid escape character \\x"));
        }
    }

    @Test
    public void ReadWithFastJson() {
        String jsonString = "{\"array\":[1,2,3],\"arraylist\":[{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"}],\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"HelloWorld\"}";

        // 如果json数据以形式保存在文件中，用FileReader进行流读取！！
        // path为json数据文件路径！！
        // JSONReader reader = new JSONReader(new FileReader(path));
        JSONReader reader = new JSONReader(new StringReader(jsonString));
        reader.startObject();
        System.out.println("start fastjson：");
        while (reader.hasNext()) {
            String key = reader.readString();
            System.out.println("key " + key);
            switch (key) {
                case "array":
                    reader.startArray();
                    System.out.println("start " + key);
                    while (reader.hasNext()) {
                        String item = reader.readString();
                        System.out.print(item);
                        System.out.print(",");
                    }
                    reader.endArray();
                    System.out.println("end " + key);
                    break;
                case "arraylist":
                    reader.startArray();
                    System.out.println("start " + key);
                    while (reader.hasNext()) {
                        reader.startObject();
                        System.out.println("start arraylist item");
                        while (reader.hasNext()) {
                            String arrayListItemKey = reader.readString();
                            String arrayListItemValue = reader.readObject().toString();
                            System.out.print("key=" + arrayListItemKey);
                            System.out.print(":");
                            System.out.print("value=" + arrayListItemValue);
                        }
                        reader.endObject();
                        System.out.println(", end arraylist item");
                    }
                    reader.endArray();
                    System.out.println("end " + key);
                    break;
                case "object":
                    reader.startObject();
                    System.out.println("start object item");
                    while (reader.hasNext()) {
                        String objectKey = reader.readString();
                        String objectValue = reader.readObject().toString();
                        System.out.print("key " + objectKey);
                        System.out.print(":");
                        System.out.print("value " + objectValue);
                    }
                    reader.endObject();
                    System.out.println(", end object item");
                    break;
                case "string":
                    System.out.println("start string");
                    String value = reader.readObject().toString();
                    System.out.print("value " + value);
                    System.out.println(", end string");
                    break;
            }
        }
        reader.endObject();
        System.out.println("start fastjson");
    }

    @Test
    public void test_oom() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 54000; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key_" + i, new String[1024]);
            list.add(jsonObject.toString());
        }
        for (String string : list) {
            try (JSONReader reader = new JSONReader(new StringReader(string));) {
                reader.startObject();
                while (reader.hasNext()) {
                    String key = reader.readString();
//                    System.out.println("key = " + key);
                    Object value = reader.readObject();
//                    System.out.println("value = " + value);
                }
                reader.endObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
