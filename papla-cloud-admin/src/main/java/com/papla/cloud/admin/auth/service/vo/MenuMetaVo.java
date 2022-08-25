package com.papla.cloud.admin.auth.service.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zheng Jie
 * @date 2018-12-20
 */
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {

    private String title;

    private String icon;

    private Boolean noCache;
}
