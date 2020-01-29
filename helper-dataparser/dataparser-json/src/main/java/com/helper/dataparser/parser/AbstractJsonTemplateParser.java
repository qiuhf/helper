package com.helper.dataparser.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.helper.dataparser.support.ParserConfig;
import com.helper.dataparser.support.TemplateFeature;
import com.helper.dataparser.util.FnvHashUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.helper.dataparser.parser.JsonDiagram.DiagramBuilder;

/**
 * @author sz_qiuhf@163.com
 **/
public abstract class AbstractJsonTemplateParser {

    private static int DEFAULT_PARSER_FEATURES;

    private static Map<String, JsonDiagram> cacheTemplateMap = new ConcurrentHashMap<>(1 << 4, 0.75f, 1);

    static {
        /* 设置默认属性 */
        int featureValue = 0;
        featureValue |= TemplateFeature.KeepOriginalData.getMark();
        featureValue |= TemplateFeature.OrderedField.getMark();
        DEFAULT_PARSER_FEATURES = featureValue;
    }

    public JsonDiagram generationTemplate(String text) {
        return generationTemplate(text, ParserConfig.getGlobalInstance());
    }

    public JsonDiagram generationTemplate(String text, ParserConfig parserConfig) {
        return generationTemplate(text, parserConfig, (TemplateFeature[]) null);
    }

    /**
     * 生成json模板
     *
     * @param text         json样例
     * @param parserConfig 解析配置
     * @param features     TemplateFeature
     * @return JsonDiagram
     */
    public JsonDiagram generationTemplate(String text, ParserConfig parserConfig, TemplateFeature... features) {

        DEFAULT_PARSER_FEATURES = TemplateFeature.markerCard(DEFAULT_PARSER_FEATURES, features);

        parserConfig = Objects.isNull(parserConfig) ? ParserConfig.getGlobalInstance() : parserConfig;
        List<String[]> nodes = getEachNodeByRecursively(text, DEFAULT_PARSER_FEATURES);

        return createJsonDiagram(text, nodes, parserConfig);
    }

    /**
     * @param uniqueKey   缓存json模板的唯一标识，不能是null或空字符串
     * @param jsonDiagram json模板
     */
    public void cacheJsonDiagram(String uniqueKey, JsonDiagram jsonDiagram) {
        if (!Objects.isNull(uniqueKey) && uniqueKey.trim().isEmpty()) {
            cacheTemplateMap.put(uniqueKey, jsonDiagram);
        }
    }

    /**
     * 获取json模板
     *
     * @param uniqueKey 唯一标识
     * @return json模板
     */
    public JsonDiagram getJsonDiagramByCache(String uniqueKey) {
        JsonDiagram jsonDiagram = cacheTemplateMap.get(uniqueKey);
        if (Objects.isNull(jsonDiagram)) {
            throw new NullPointerException("JsonDiagram is null, try calling generationTemplate method.");
        }
        return jsonDiagram;
    }

    /**
     * 递归获取每个节点的信息
     *
     * @param text           json样例
     * @param parserFeatures parserFeatures
     * @return 节点的信息, 数组结构【键，值类型，当前层级】
     */
    protected abstract List<String[]> getEachNodeByRecursively(String text, int parserFeatures);

    /**
     * 通过节点的信息生成json关系图
     *
     * @param text         json样例
     * @param nodes        节点信息集，数组结构【键，值类型，当前层级】
     * @param parserConfig
     * @return JsonDiagram
     */
    private JsonDiagram createJsonDiagram(String text, List<String[]> nodes, ParserConfig parserConfig) {
        /* 获取每层对应的最大深度，数据结构：<层级，最大深度> */
        Map<Integer, Long> levelAndMaxDepth = nodes.stream().collect(Collectors.groupingBy(node -> Integer.valueOf(node[2]), Collectors.counting()));
        /* 每层最后偏移量 */
        int maxLevel = levelAndMaxDepth.size();
        Integer[] lastOffset = new Integer[maxLevel];
        final String[][] keys = new String[maxLevel][];
        final String[][] types = new String[maxLevel][];
        Map<Position, List<int[]>> nodeRelation = new LinkedHashMap<>(1 << 4);
        Map<Position, Integer[]> nodePath = new LinkedHashMap<>(nodes.size());
        for (String[] node : nodes) {
            int currentLevel = Integer.parseInt(node[2]);
            /* 当前层级的的最大深度 */
            int maxDepth = Math.toIntExact(levelAndMaxDepth.get(currentLevel));
            lastOffset[currentLevel] = Objects.isNull(lastOffset[currentLevel]) ? 0 : lastOffset[currentLevel] + 1;
            keys[currentLevel] = Optional.ofNullable(keys[currentLevel]).orElse(new String[maxDepth]);
            keys[currentLevel][lastOffset[currentLevel]] = node[0];
            types[currentLevel] = Optional.ofNullable(types[currentLevel]).orElse(new String[maxDepth]);
            types[currentLevel][lastOffset[currentLevel]] = node[1];
            /* 节点对应的坐标 */
            Position position = buildPosition(lastOffset, node, currentLevel);
            /* 构建节点坐标与节点轨迹的映射关系 */
            this.buildNodePath(nodePath, lastOffset, currentLevel, position);
            /* 构建父节点与子节点坐标的关系树 */
            this.buildNodeRelation(nodeRelation, node[1], position);
        }
        return new DiagramBuilder(keys, types, text).setMarkerCard(DEFAULT_PARSER_FEATURES).setParserConfig(parserConfig)
                .setNodeRelation(nodeRelation).setNodePath(nodePath).build();
    }

    private Position buildPosition(Integer[] lastOffset, String[] nodeInfo, int currentLevel) {
        Position position = new Position(currentLevel, lastOffset[currentLevel]);
        int pid = currentLevel - 1;
        position.setParentPosition(currentLevel == 0 ? new int[2] : new int[]{pid, lastOffset[pid]});
        position.setParentNode("JSONObject".equals(nodeInfo[1]) || "JSONArray".equals(nodeInfo[1]));
        return position;
    }

    /**
     * 构建节点坐标与节点轨迹的映射关系
     *
     * @param nodePath   节点坐标与节点轨迹的映射关系
     * @param lastOffset 最后偏移量
     * @param level      层级
     * @param position   坐标
     */
    private void buildNodePath(Map<Position, Integer[]> nodePath, Integer[] lastOffset, int level, Position position) {
        Integer[] path = new Integer[level + 1];
        System.arraycopy(lastOffset, 0, path, 0, level + 1);
        nodePath.put(position, path);
    }

    /**
     * 构建父节点与子节点坐标的关系树
     *
     * @param nodeRelation 节点关系树
     * @param type         节点类型
     * @param position     坐标
     */
    private void buildNodeRelation(Map<Position, List<int[]>> nodeRelation, String type, Position position) {
        if ("JSONObject".equals(type) || "JSONArray".equals(type)) {
            if (Objects.isNull(nodeRelation.get(position))) {
                List<int[]> list = new ArrayList<>();
                nodeRelation.put(position, list);
            }
            if (position.isRoot()) {
                return;
            }
        }
        nodeRelation.get(position.getParentPosition()).add(position.getCoordinate());
    }

    /**
     * 根据JSONArray中的元素是否相同决定集合的大小；
     *
     * @param jsonArray jsonArray
     * @return size
     */
    int sizeByCompareListElements(JSONArray jsonArray, int size) {
        if (size <= 1) {
            return size;
        }

        if (TemplateFeature.isEnabled(DEFAULT_PARSER_FEATURES, TemplateFeature.DynamicReadArray)) {
            Map<Long, Integer> indexMap = new HashMap<>(1 << 2);
            int offset = -1;
            for (int i = 0; i < size; i++) {
                long fnvHash = nodeFnvHash(jsonArray.get(i));
                if (!indexMap.containsKey(fnvHash)) {
                    /* 只支持动态读取同构连续的数组 */
                    if (i - offset != 1) {
                        throw new UnsupportedOperationException("Irregular arrays do not support dynamic reads");
                    }
                    offset = i;
                    indexMap.put(fnvHash, offset);
                }
            }
            return indexMap.size();
        }

        return size;
    }

    /**
     * 计算JSONArray中每一个元素的Hash(FNVLa64)值；
     *
     * @param object 元素
     * @return 哈希值
     */
    private long nodeFnvHash(Object object) {
        if (object instanceof JSONArray) {
            JSONArray array = JSONArray.parseArray(object.toString());
            long sum = 0L;
            for (int i = 0; i < array.size(); i++) {
                Object element = Objects.isNull(array.get(i)) ? "" : array.get(i);
                sum += FnvHashUtil.fnv1a64(i + element.getClass().getSimpleName());
            }
            return sum == 0L ? FnvHashUtil.fnv1a64("0") : sum;
        } else if (object instanceof JSONObject) {
            return JSONObject.parseObject(object.toString()).keySet().stream().mapToLong(FnvHashUtil::fnv1a64).sum();
        } else {
            return -1L;
        }
    }
}
