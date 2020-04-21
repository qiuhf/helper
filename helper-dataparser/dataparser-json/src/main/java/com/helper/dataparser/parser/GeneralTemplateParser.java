package com.helper.dataparser.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.helper.dataparser.support.TemplateFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author sz_qiuhf@163.com
 **/
public class GeneralTemplateParser extends AbstractJsonTemplateParser {

    private static boolean orderedField;

    /**
     * 递归获取每个节点的信息
     *
     * @param text           json样例
     * @param parserFeatures parserFeatures
     * @return 节点的信息, 数组结构【键，值类型，当前层级】
     */
    @Override
    public List<String[]> getEachNodeByRecursively(String text, int parserFeatures) {
        orderedField = TemplateFeature.isEnabled(parserFeatures, TemplateFeature.OrderedField);
        Object data = orderedField ? JSON.parse(text, Feature.OrderedField) : JSON.parse(text);
        if (Objects.isNull(data)) {
            throw new NullPointerException("json is null");
        }
        List<String[]> nodes = new ArrayList<>();
        this.buildNodeInfoByRecursively(data, new String[]{null, null, "0"}, nodes, 0);
        return nodes;
    }

    /**
     * 递归构建每个节点的信息
     *
     * @param json         json
     * @param nodePath     单个节点的信息
     * @param nodes        所有节点的信息集
     * @param currentDepth 当前层级
     */
    private void buildNodeInfoByRecursively(Object json, String[] nodePath, List<String[]> nodes, int currentDepth) {
        nodePath[1] = Objects.isNull(json) ? "Object" : json.getClass().getSimpleName();
        nodes.add(nodePath);
        switch (nodePath[1]) {
            case "JSONArray":
                currentDepth++;

                JSONArray jsonArray = orderedField ? (JSONArray) JSON.parse(json.toString(), Feature.OrderedField)
                        : JSON.parseArray(json.toString());
                int originSize = jsonArray.size();
                int size = super.sizeByCompareListElements(jsonArray, originSize);
                boolean isEqual = originSize == size;
                for (int i = 0; i < size; i++) {
                    /* TemplateFeature设置SimpleStructure，如果是同构数组，下标值变为-(i + size) */
                    String index = String.valueOf(isEqual ? i : -(i + size));
                    String[] childNodePath = {index, null, String.valueOf(currentDepth)};
                    this.buildNodeInfoByRecursively(jsonArray.get(i), childNodePath, nodes, currentDepth);
                }
                break;

            case "JSONObject":
                currentDepth++;

                JSONObject jsonObject = orderedField ? JSON.parseObject(json.toString(), Feature.OrderedField)
                        : JSON.parseObject(json.toString());
                for (Map.Entry<String, Object> map : jsonObject.entrySet()) {
                    String[] childNodePath = {map.getKey(), null, String.valueOf(currentDepth)};
                    this.buildNodeInfoByRecursively(map.getValue(), childNodePath, nodes, currentDepth);
                }

                break;
            default:

        }
    }
}
