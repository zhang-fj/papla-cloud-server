package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:BizStatusEnumBean	业务数据状态枚举字典表
 *
 * @author zhangfj
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class BizStatusEnumBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    private String enumId;
    private String entityId;
    private String statusCateGory; // 状态分类：A-起草，R-撤回、C-运行中、D-驳回、E-结束
    private String statusCode;
    private String statusName;
    private String statusDesc;
    private String enabled;

}
