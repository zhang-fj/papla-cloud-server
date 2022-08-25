package com.papla.cloud.logging.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class LogSmallDTO implements Serializable {

    private String username;

    private String description;

    private String requestIp;

    private Long time;

    private String address;

    private String browser;

    private Timestamp createTime;
}
