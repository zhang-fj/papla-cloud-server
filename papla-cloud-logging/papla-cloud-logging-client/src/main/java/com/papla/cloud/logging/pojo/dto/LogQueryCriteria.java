package com.papla.cloud.logging.pojo.dto;

import com.papla.cloud.common.jpa.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * 日志查询类
 *
 * @author Zheng Jie
 * @date 2019-6-4 09:23:07
 */
@Data
public class LogQueryCriteria {

    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;

    @Query
    private String logType;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
