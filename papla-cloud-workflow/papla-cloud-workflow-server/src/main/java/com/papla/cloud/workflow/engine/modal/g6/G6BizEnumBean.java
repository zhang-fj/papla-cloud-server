package com.papla.cloud.workflow.engine.modal.g6;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @ClassName: G6BizEnumBean
 * @Description: 业务状态信息
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class G6BizEnumBean {

    private String entityId;
    private String enumId;
    private String bizCategory;
    private String bizDesc;
    private String enabled;

}
