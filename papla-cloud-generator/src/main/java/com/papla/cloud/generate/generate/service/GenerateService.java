package com.papla.cloud.generate.generate.service;

import java.util.List;
import java.util.Map;

public interface GenerateService {

    /**
     * @param params
     * @return List<Map < String, Object>>
     * @throws Exception
     * @Title preview
     * @Description TODO 预览
     */
    List<Map<String, Object>> preview(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return String
     * @throws Exception
     * @Title download
     * @Description TODO 下载
     */
    String download(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return String
     * @throws Exception
     * @Title generator
     * @Description TODO 生成
     */
    void generator(Map<String, Object> params) throws Exception;

}
