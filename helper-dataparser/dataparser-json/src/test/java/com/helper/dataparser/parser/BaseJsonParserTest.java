package com.helper.dataparser.parser;


import org.junit.Before;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class BaseJsonParserTest {

    String jsonTemplate = "";

    @Before
    public void getJson() {
        jsonTemplate = json3();
        System.out.println(String.format("json.getBytes().length = %dKB", jsonTemplate.getBytes(StandardCharsets.UTF_8).length));
    }

    public String json1() {
        return "[\n" +
                "        1,\n" +
                "        [],\n" +
                "        {\n" +
                "            \"1\": 1454" +
                "        }\n" +
                "    ]";
    }

    public String json2() {
        return "[\n" +
                "    [\n" +
                "      1,\n" +
                "        [],\n" +
                "        {\n" +
                "            \"1\": 999999999999999999999\n" +
                "        }\n" +
                "    ],\n" +
                "    [\n" +
                "        1,\n" +
                "        {\n" +
                "            \"1\": 222222222222222222222222\n" +
                "        },\n" +
                "        []\n" +
                "    ]\n" +
                "]";
    }

    public String json3() {
        return "[{\n" +
                "\t\t\"batchNo\": \"" + UUID.randomUUID().toString() + "\",\n" +
                "\t\t\"senderId\": \"wxea33555e2a839286152221121\",\n" +
                "\t\t\"channel\": \"WX_TAGWRTGERRGETWFWE\",\n" +
                "\t\t\"messageId\": \"uuuuudfsfgfgsdfgeqqwqfuahahh@@u\",\n" +
                "\t\t\"callback\": \"11110000-01-A00\",\n" +
                "\t\t\"body\": {\n" +
                "\t\t\t\"filter\": {\n" +
                "\t\t\t\t\"id\": 1001216155516,\n" +
                "\t\t\t\t\"tag_ids\": [100],\n" +
                "\t\t\t\t\"is_to_all\": \"is_to_allis_to_alsadasfgsl\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"text\": {\n" +
                "\t\t\t\t\"content\": \"con奥术大师多大大l你好1\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"msgtype\": \"第三方第三方士费全额王企鹅群翁群无\"\n" +
                "\t\t}\n" +
                "\t}, {\n" +
                "\t\t\"batchNo\": \"" + UUID.randomUUID().toString() + "\",\n" +
                "\t\t\"senderId\": \"wxea33555e2sadaghtytrrewa83926\",\n" +
                "\t\t\"channel\": \"WX_TAGFDFGVSEWRWE\",\n" +
                "\t\t\"messageId\": \"告诉对方更多化工儿童热\",\n" +
                "\t\t\"callback\": \"11110000-01-A01\",\n" +
                "\t\t\"body\": {\n" +
                "\t\t\t\"filter\": {\n" +
                "\t\t\t\t\"id\": 10046876416874648786488687,\n" +
                "\t\t\t\t\"tag_ids\": [100,200],\n" +
                "\t\t\t\t\"is_to_all\": \"is_to_allis_to_all\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"text\": {\n" +
                "\t\t\t\t\"content\": \"特热同外国人合同号,你好ntent\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"msgtype\": \"msgtypemsgt范德萨发生韩国法国和认同和pemsgtyp那是的emsgtype\"\n" +
                "\t\t}\n" +
                "\t}]";
    }

    public String json4() {
        return "{\n" +
                "\t\"messageList\": [{\n" +
                "\t\t\"callback\": \"11110000-01-A00\",\n" +
                "\t\t\"batchNo\": \"" + UUID.randomUUID().toString() + "\",\n" +
                "\t\t\"messageId\": \"uuuuuuu\",\n" +
                "\t\t\"senderId\": \"wxea33555e2a839286\",\n" +
                "\t\t\"channel\": \"WX_TAG\",\n" +
                "\t\t\"body\": {\n" +
                "\t\t\t\"filter\": {\n" +
                "\t\t\t\t\"tag_id\": [100,200],\n" +
                "\t\t\t\t\"is_to_all\": \"is_to_allis_to_all\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"text\": {\n" +
                "\t\t\t\t\"content\": \"contentcontentcontent\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"msgtype\": \"msgtypemsgtypemsgtypemsgtype\"\n" +
                "\t\t}\n" +
                "\t}, {\n" +
                "\t\t\"batchNo\": \"" + UUID.randomUUID().toString() + "\",\n" +
                "\t\t\"senderId\": \"wxea33555e2a839286\",\n" +
                "\t\t\"channel\": \"WX_TAG\",\n" +
                "\t\t\"messageId\": \"uuuuuuu\",\n" +
                "\t\t\"callback\": \"11110000-01-A00\",\n" +
                "\t\t\"body\": {\n" +
                "\t\t\t\"filter\": {\n" +
                "\t\t\t\t\"is_to_all\": \"is_to_allis_to_all\",\n" +
                "\t\t\t\t\"tag_id\": []\n" +
                "\t\t\t},\n" +
                "\t\t\t\"text\": {\n" +
                "\t\t\t\t\"content\": \"contentcontentcontent\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"msgtype\": \"msgtypemsgtypemsgtypemsgtype\"\n" +
                "\t\t}\n" +
                "\t}],\n" +
                "\t\"channel\": \"WX_TAG\",\n" +
                "\t\"callback\": \"11110000-01-A00\",\n" +
                "\t\"date\": \"" + new Date() + "\"\n" +
                "}";
    }

    public String json5() {
        return "{\n" +
                "\t\"messageList\": [{},\n{" +
                "\t\t\"batchNo\": \"" + UUID.randomUUID().toString() + "\",\n" +
                "\t\t\"senderId\": \"wxea33555e2a839286\",\n" +
                "\t\t\"channel\": \"WX_TAG\",\n" +
                "\t\t\"messageId1\": \"uuuuuuu\",\n" +
                "\t\t\"callback\": \"11110000-01-A00\",\n" +
                "\t\t\"body\": {\n" +
                "\t\t\t\"filter\": {\n" +
                "\t\t\t\t\"is_to_all\": \"is_to_allis_to_all\",\n" +
                "\t\t\t\t\"tag_id\": []\n" +
                "\t\t\t},\n" +
                "\t\t\t\"text\": {\n" +
                "\t\t\t\t\"content\": \"contentcontentcontent\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"msgtype\": \"msgtypemsgtypemsgtypemsgtype\"\n" +
                "\t\t}\n" +
                "\t}],\n" +
                "\t\"channel\": \"WX_TAG\",\n" +
                "\t\"callback\": \"11110000-01-A00\",\n" +
                "\t\"date\": \"" + new Date() + "\"\n" +
                "}";
    }
}
