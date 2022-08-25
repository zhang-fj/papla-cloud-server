package com.papla.cloud.common.core.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页工具
 * @author Zheng Jie
 * @date 2018-12-10
 */
public class PageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * List 分页
     */
    public static List toPage(int page, int size, List list) {
        int fromIndex = page * size;
        int toIndex = page * size + size;
        if (fromIndex > list.size()) {
            return new ArrayList();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * 自定义分页
     */
    public static Map<String, Object> toPage(Object object, Object totalElements) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("data", object);
        map.put("total", totalElements);
        return map;
    }

}
