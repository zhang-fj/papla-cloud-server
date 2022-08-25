package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @ClassName: PubValSetBean
 * @Description: 值集信息Bean
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class PubValSetBean {

    private String dtlId;        // 值ID
    private String valCode;    // 值集编码
    private String dtlCode;    // 值编码
    private String dtlName;    // 值名称
    private String dtlDesc;    // 值描述
    private String dtlEnable;    // 是否启用

}
