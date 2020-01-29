package com.helper.dataparser.parser;

import org.junit.Test;

public class AbstractJsonTemplateParserTest extends BaseJsonParserTest {

    @Test
    public void generationTemplate() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("asdas").deleteCharAt(stringBuilder.length() - 1);
        System.out.println("stringBuilder = " + stringBuilder);
        String code = Integer.toHexString(17) + Integer.toHexString(99);
        System.out.println("code = " + code);
        System.out.println(Integer.parseInt(Integer.toHexString(17), 16));
        AbstractJsonTemplateParser parser = new GeneralTemplateParser();
        System.out.println(parser.generationTemplate(super.json4()));
    }
}
