package com.helper.dataparser.support;

import java.util.Arrays;

/**
 * @author sz_qiuhf@163.com
 **/
public enum TemplateFeature {
    /**
     * 保证JSONObject中字段顺序不变化
     */
    OrderedField,
    /**
     * 动态读取数组,同构数组只保留第一个
     */
    DynamicReadArray,
    /**
     * 初始化生成JsonParser时，解析缓存当前json的所有父节点值
     */
    InitJsonParserCacheParentNodeValues,
    /**
     * 自动识别键是否包含'.'或'【',兼容键含特殊符号问题.影响初始化性能
     */
    AutoDiscernOfSpecialCharacters,
    /**
     * 保留原数据,定位数据是否为异常数据
     */
    KeepOriginalData,
    /**
     * 返回值类型为java类型,兼容序列化问题
     */
    ReturnToJavaType,
    /**
     * 解析节点报错时,该节点下的所有子节点均返回值null
     */
    IgnoreNodeErrorToNull;

    public final int mark;

    TemplateFeature() {
        mark = (1 << ordinal());
    }

    public static boolean isEnabled(int features, TemplateFeature feature) {
        return (features & feature.mark) != 0;
    }

    public static int config(int features, TemplateFeature feature, boolean state) {
        if (state) {
            features |= feature.mark;
        } else {
            features &= ~feature.mark;
        }
        return features;
    }

    public static int markerCard(int initValue, TemplateFeature... features) {
        return features == null ? initValue : Arrays.stream(features).mapToInt(feature -> feature.mark).reduce(0, (a, b) -> a | b);
    }

    public final int getMark() {
        return mark;
    }
}
