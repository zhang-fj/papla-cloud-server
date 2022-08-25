package com.papla.cloud.common.jpa.utils;

import org.springframework.data.domain.Page;

import java.util.LinkedHashMap;
import java.util.Map;

public class PageUtil extends cn.hutool.core.util.PageUtil {

    public static Map<String, Object> toPage(Page page) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("data", page.getContent());
        map.put("total", page.getTotalElements());
        return map;
    }

}
