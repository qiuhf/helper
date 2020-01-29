package com.helper.pattern;

/**
 * @author sz_qiuhf@163.com
 **/
class ReadXml {
    static Object getObject() {
        try {
//            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = dFactory.newDocumentBuilder();
//            Document doc;
//            doc = builder.parse(new File("src/Builder/config.xml"));
//            NodeList nl = doc.getElementsByTagName("className");
//            Node classNode = nl.item(0).getFirstChild();
//            String cName = "Builder." + classNode.getNodeValue();
//            System.out.println("新类名：" + cName);
//            Class<?> c = Class.forName(cName);
//            return c.newInstance();
            return new ConcreteDecorator2();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
