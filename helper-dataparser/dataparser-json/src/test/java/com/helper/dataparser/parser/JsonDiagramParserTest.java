package com.helper.dataparser.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonDiagramParserTest extends BaseJsonParserTest {
    JsonDiagram jsonDiagram;

    @Before
    public void test_GenerationTemplate() {
        long t = System.currentTimeMillis();
        AbstractJsonTemplateParser parser = new GeneralTemplateParser();
        jsonDiagram = parser.generationTemplate(this.jsonTemplate);
        System.out.println(String.format("GenerationTemplate speed time: %dms\n", System.currentTimeMillis() - t));
        test_Performance();
    }

    public void test_Performance() {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 48000; i++) {
            JsonDiagram.JsonParser jsonParser = jsonDiagram.getJsonParser(this.jsonTemplate);
            jsonParser.selectList();
        }
        System.out.println(String.format("JsonParser.selectList() speed time: %dms\n", System.currentTimeMillis() - t));
    }

    @Test
    public void test_SelectOne() {
        JsonDiagram.JsonParser jsonParser = jsonDiagram.getJsonParser(this.jsonTemplate);
        String key = null;
        System.out.println(jsonParser.selectOne(key));
    }

    @Test
    public void test_SelectList() {
        JsonDiagram.JsonParser jsonParser = jsonDiagram.getJsonParser(this.jsonTemplate);
        jsonParser.selectList().forEach(System.out::println);
        jsonParser.selectList(true).forEach(System.out::println);
    }

    @Test
    public void test_SelectByKey() {
        JsonDiagram.JsonParser jsonParser = jsonDiagram.getJsonParser(this.jsonTemplate);
        String key = "[0]";
//        jsonParser.selectByKey(key).forEach(System.out::println);
    }

    @Test
    public void test_GetJsonKeyAndType() {
        System.out.println(jsonDiagram.getJsonKeyAndType());
    }

    @After
    public void print_JsonDiagram() {
        System.out.println(jsonDiagram);
    }
}
