package com.helper.dataparser.support;


import com.helper.dataparser.util.FnvHashUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sz_qiuhf@163.com
 **/
public class ParserConfig {

    private static ParserConfig global = new ParserConfig();
    private final Map<ParserFeature, Object> config = new ConcurrentHashMap<>(1 << 4, 0.75f, 1);

    public static ParserConfig getGlobalInstance() {
        return global;
    }


    public Object getConfig(ParserFeature feature) {
        return config.get(feature);
    }

    /**
     * 只支持指定单个数组一变多
     * 可能存在键同名情况，故必须指定key所在的层级
     *
     * @param key   键名称，根节点使用null作为key
     * @param level 层级，从0（根节点层级=0）开始计算，，每嵌套一层+1
     */
    public void setArraySplitMultiple(String key, int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Level cannot be negative!");
        }
        this.config.put(ParserFeature.SpecifyArraySplitMultiple, FnvHashUtil.fnv1a64(key, String.valueOf(level)));
    }
}
