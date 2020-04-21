package com.helper.dataparser.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.helper.dataparser.parser.impl.JsonGeneratorImpl;
import com.helper.dataparser.parser.impl.JsonParserImpl;
import com.helper.dataparser.support.ParserConfig;
import com.helper.dataparser.support.TemplateFeature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author sz_qiuhf@163.com
 **/
public class JsonDiagram implements Serializable {
    private static String jsonTemplate;
    private static String[][] keyMapping;
    private static String[][] typeMapping;
    private static int maxSize;
    private static int markerCard;
    private static ParserConfig parserConfig;
    /**
     * 各节点轨迹关系(有序)，数据结构：<节点坐标，节点坐标轨迹>
     */
    private static Map<Position, Integer[]> nodePath;
    /**
     * 各节点之间的关系(有序)，数据结构：<父节点坐标，子节点坐标集>
     */
    private static Map<Position, List<int[]>> nodeRelation;

    /**
     * 各节点名称对应的坐标编码(有序)，数据结构：<键名称，节点坐标集>
     */
    private static Map<String, List<Position>> nodePositionMap;
    /**
     * 各节点全路径对应的坐标编码(有序)，数据结构:<节点全路径，节点坐标>
     */
    private static Map<String, Position> fullPathMap;

    private JsonDiagram(DiagramBuilder builder) {
        jsonTemplate = builder.jsonTemplate;
        markerCard = builder.markerCard;
        parserConfig = builder.parserConfig;
        keyMapping = builder.keys;
        typeMapping = builder.types;
        nodeRelation = builder.nodeRelation;
        nodePath = builder.nodePath;
        maxSize = builder.nodePath.size();

        /* 构建节点对应的坐标映射关系 */
        this.buildNodePositionMap(keyMapping, typeMapping);
    }

    /**
     * 获取JSON生成器
     */
    JsonGenerator getJsonGenerator() {
        return new JsonGenerator();
    }

    /**
     * 获取JSON解析器
     *
     * @param text json数据
     */
    JsonParser getJsonParser(String text) {
        return new JsonParser(text);
    }

    /**
     * 获取json结构：<节点路径,节点类型>
     *
     * @return Map<节点路径, 节点类型>
     */
    public Map<String, String> getJsonKeyAndType() {
        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        fullPathMap.forEach((key, value) -> {
            int[] coordinate = value.getCoordinate();
            linkedHashMap.put(key, typeMapping[coordinate[0]][coordinate[1]]);
        });
        return linkedHashMap;
    }

    /**
     * 构建节点对应的坐标映射关系
     *
     * @param keys  键映射表
     * @param types 类型映射表
     */
    private void buildNodePositionMap(final String[][] keys, final String[][] types) {
        fullPathMap = new LinkedHashMap<>(maxSize);
        nodePositionMap = new HashMap<>(maxSize);
        nodePath.forEach((key, value) -> {
            this.buildFullPathMap(keys, types, key, value);
            this.buildNodePositionMap(keys, types, key);
        });
    }

    private void buildNodePositionMap(String[][] keys, String[][] types, Position position) {
        int[] coordinate = position.getCoordinate();
        int[] pCoordinate = position.getParentPosition().getCoordinate();
        String name = keys[coordinate[0]][coordinate[1]];
        if (!position.isRoot()) {
            name = "JSONArray".equals(types[pCoordinate[0]][pCoordinate[1]]) ? "[" + name + "]" : name;
        }
        if (!nodePositionMap.containsKey(name)) {
            List<Position> list = new ArrayList<>();
            list.add(position);
            nodePositionMap.put(name, list);
        } else {
            nodePositionMap.get(name).add(position);
        }
    }

    private void buildFullPathMap(String[][] keys, String[][] types, Position key, Integer[] value) {
        StringBuilder path = new StringBuilder();
        Stream.iterate(1, i -> i + 1).limit(value.length - 1).forEach(i -> {
            if ("JSONArray".equals(types[i - 1][value[i - 1]])) {
                int length = path.length();
                if (length > 0) {
                    path.deleteCharAt(length - 1);
                }
                path.append("[").append(keys[i][value[i]]).append("]");
            } else {
                path.append(keys[i][value[i]]);
            }
            path.append(".");
        });
        int length = path.length();
        fullPathMap.put(length > 0 ? path.deleteCharAt(length - 1).toString() : null, key);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("| -------------------------- keys --------------------------\n| ");
        Arrays.stream(keyMapping).filter(key -> !Objects.isNull(key)).forEach(key -> {
            Arrays.asList(key).forEach(name -> builder.append(name).append(" | "));
            builder.append("\n| ");
        });

        builder.append("-------------------------- types --------------------------\n| ");
        Arrays.stream(typeMapping).filter(key -> !Objects.isNull(key)).forEach(type -> {
            Arrays.asList(type).forEach(name -> builder.append(name).append(" | "));
            builder.append("\n| ");
        });

        builder.append("----------------------- nodePositionMap -----------------------\n| ");
        nodePositionMap.forEach((key, value) -> {
            builder.append(key).append(" =>> ");
            value.forEach(coordinate -> builder.append("| ").append(coordinate).append(" | "));
            builder.append("\n| ");
        });

        builder.append("----------------------- fullPathMap ----------------------\n| ");
        fullPathMap.forEach((key, value) -> builder.append(key).append(" =>> ").append(value).append("\n| "));

        builder.append("------------------------ nodePath ------------------------\n| ");
        nodePath.forEach((key, value) -> {
            builder.append(key).append(" =>> ");
            Stream.iterate(0, i -> i + 1).limit(value.length).forEach(i -> builder.append("| ").append(keyMapping[i][value[i]]).append(" | "));
            builder.append("\n| ");
        });

        builder.append("---------------------- nodeRelation ---------------------\n| ");
        nodeRelation.forEach((key, value) -> {
            builder.append(key).append(" =>> ");
            value.forEach(coordinate -> builder.append("| ").append(keyMapping[coordinate[0]][coordinate[1]]).append(" | "));
            builder.append("\n| ");
        });

        return builder.delete(builder.length() - 2, builder.length()).toString();
    }

    static class JsonGenerator implements JsonGeneratorImpl {

    }

    static class JsonParser implements JsonParserImpl {
        /**
         * 缓存根节点数据
         */
        private final JSON root;

        /**
         * 缓存节点数据，数据结构<节点坐标，值>
         */
        private Map<Position, Object> cacheNodeData;

        private static final boolean ORDERED_FIELD = TemplateFeature.isEnabled(markerCard, TemplateFeature.OrderedField);

        JsonParser(String text) {
            String type = typeMapping[0][0];
            if ("JSONObject".equals(type)) {
                this.root = ORDERED_FIELD ? JSON.parseObject(text, Feature.OrderedField) : JSON.parseObject(text);
            } else if ("JSONArray".equals(type)) {
                this.root = ORDERED_FIELD ? (JSONArray) JSON.parse(text, Feature.OrderedField) : JSON.parseArray(text);
            } else {
                throw new IllegalArgumentException(String.format("Expectation: JSONObject/JSONArray, Actual: %s, " +
                        "Illegal root node type!", type));
            }

            this.cacheNodeData = new HashMap<>(maxSize - 1);
            if (TemplateFeature.isEnabled(markerCard, TemplateFeature.KeepOriginalData)) {
                this.cacheNodeData.put(new Position(0, 0), text);
            }

            if (TemplateFeature.isEnabled(markerCard, TemplateFeature.InitJsonParserCacheParentNodeValues)) {
                nodeRelation.entrySet().stream().filter(entry -> entry.getKey().isParentNode())
                        .forEach(entry -> this.getValueByCoordinate(entry.getKey()));
            }
        }

        public List<Object> selectList() {
            return this.selectList(false);
        }

        public List<Object> selectList(boolean containParentNode) {
            List<Object> values = new ArrayList<>(maxSize);
            fullPathMap.entrySet().stream().filter(entry -> containParentNode || !entry.getValue().isParentNode()).
                    forEach(entry -> values.add(getValueByCoordinate(entry.getValue())));
            return values;
        }

        public Position eval(String path) {

            return null;
        }

        public Map<String, Object> selectByKey(String key) {
            List<Position> coordinates = nodePositionMap.get(key);
            if (Objects.isNull(coordinates)) {
                throw new NullPointerException(String.format("Key not exist, path: %s", key));
            }
            List<Object> values = new ArrayList<>(coordinates.size());
            coordinates.forEach(coordinate -> values.add(this.getValueByCoordinate(coordinate)));
            return null;
        }

        public Object selectOne(String fullPath) {
            Position position = this.eval(fullPath);
            if (Objects.isNull(position)) {
                throw new NullPointerException(String.format("Path not exist, path: %s", fullPath));
            }
            return getValueByCoordinate(position);
        }

        private Object getValueByCoordinate(Position position) {
            /* 优先从缓存中获取数据 */
            if (this.cacheNodeData.containsKey(position)) {
                return this.cacheNodeData.get(position);
            }

            /* 根据节点坐标编码获取查询节点坐标轨迹 */
            Integer[] path = nodePath.get(position);
            int length = path.length;
            Object value = this.root;
            int startOffset = 1;
            /*
             *  当查询节点轨迹大于1时，优先判断缓存中是否该节点的父节点值:
             *  1.缓存中不存在父节点值，则按原始完整轨迹遍历获取值
             *  2.缓存中存在父节点值并且值等于null，则根据解析配置决定返回null或抛异常,默认抛异常
             *  3.缓存中存在父节点值并且值不等于null，则直接从父节点位置开始取值。减少for循环次数，提升查询效率
             */
            if (length > 1) {
                Object cacheData = this.cacheNodeData.get(position.getParentPosition());
                if (Objects.isNull(cacheData) && this.cacheNodeData.containsKey(position)) {
                    if (TemplateFeature.isEnabled(markerCard, TemplateFeature.IgnoreNodeErrorToNull)) {
                        this.cacheNodeData.put(position, null);
                        return null;
                    }
                    throw new NullPointerException(String.format("ParentNode is null, path: %s", cacheNodeData.get(position)));
                }

                if (!Objects.isNull(cacheData)) {
                    value = cacheData;
                    startOffset = length - 1;
                }
            }

            for (int i = startOffset; i < length; i++) {
                String key = keyMapping[i][path[i]];
                String currentNodeType = typeMapping[i][path[i]];
                String parentNodeType = typeMapping[i - 1][path[i - 1]];
                switch (parentNodeType) {
                    case "JSONObject":
                        value = this.getValue((JSONObject) value, key, currentNodeType);
                        break;
                    case "JSONArray":
                        value = this.getValue((JSONArray) value, Integer.parseInt(key), currentNodeType);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("%s, unknown parent node type", parentNodeType));
                }

                Position cachePosition = new Position(i, path[i]);
                if (!this.cacheNodeData.containsKey(cachePosition)) {
                    this.cacheNodeData.put(cachePosition, value);
                }
            }
            return value;
        }

        private Object getValue(JSONObject json, String key, String currentNodeType) {
            switch (currentNodeType) {
                case "JSONObject":
                    return json.getJSONObject(key);
                case "JSONArray":
                    return json.getJSONArray(key);
                case "String":
                    return json.getString(key);
                case "Integer":
                    return json.getInteger(key);
                case "Long":
                    return json.getLong(key);
                case "Double":
                    return json.getDouble(key);
                case "Boolean":
                    return json.getBoolean(key);
                case "BigInteger":
                    return json.getBigInteger(key);
                case "BigDecimal":
                    return json.getBigDecimal(key);
                case "Object":
                    return json.get(key);
                default:
                    throw new IllegalArgumentException(String.format("%s,unknown type", currentNodeType));
            }
        }

        private Object getValue(JSONArray json, int index, String type) {
            switch (type) {
                case "JSONObject":
                    return json.getJSONObject(index);
                case "JSONArray":
                    return json.getJSONArray(index);
                case "String":
                    return json.getString(index);
                case "Integer":
                    return json.getInteger(index);
                case "Long":
                    return json.getLong(index);
                case "Double":
                    return json.getDouble(index);
                case "Boolean":
                    return json.getBoolean(index);
                case "BigInteger":
                    return json.getBigInteger(index);
                case "BigDecimal":
                    return json.getBigDecimal(index);
                case "Object":
                    return json.get(index);
                default:
                    throw new IllegalArgumentException(String.format("%s, unknown type", type));
            }
        }
    }

    static class DiagramBuilder {
        private final String jsonTemplate;
        private final String[][] keys;
        private final String[][] types;
        private int markerCard;
        private ParserConfig parserConfig;
        private Map<Position, List<int[]>> nodeRelation;
        private Map<Position, Integer[]> nodePath;

        DiagramBuilder(String[][] keys, String[][] types, String jsonTemplate) {
            this.keys = keys;
            this.types = types;
            this.jsonTemplate = jsonTemplate;
        }

        DiagramBuilder setNodeRelation(Map<Position, List<int[]>> nodeRelation) {
            this.nodeRelation = nodeRelation;
            return this;
        }

        DiagramBuilder setMarkerCard(int markerCard) {
            this.markerCard = markerCard;
            return this;
        }

        DiagramBuilder setParserConfig(ParserConfig parserConfig) {
            this.parserConfig = parserConfig;
            return this;
        }

        DiagramBuilder setNodePath(Map<Position, Integer[]> nodePath) {
            this.nodePath = nodePath;
            return this;
        }

        JsonDiagram build() {
            return new JsonDiagram(this);
        }
    }


}
