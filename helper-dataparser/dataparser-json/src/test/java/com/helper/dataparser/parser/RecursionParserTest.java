package com.helper.dataparser.parser;

import org.junit.Test;

public class RecursionParserTest extends BaseJsonParserTest {

    @Test
    public void parse() {
        RecursionParser recursionParser = new RecursionParser();
        System.out.println(recursionParser.parse(super.jsonTemplate));
    }

}
