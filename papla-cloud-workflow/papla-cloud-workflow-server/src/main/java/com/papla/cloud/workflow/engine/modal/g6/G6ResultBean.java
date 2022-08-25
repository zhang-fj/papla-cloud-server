package com.papla.cloud.workflow.engine.modal.g6;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @ClassName: ResultBean
 * @Description:
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class G6ResultBean {

    // 应用编码
    private String appcode;
    // 应用名称
    private String appname;
    // 关联表单信息
    private G6FormsBean forms;
    // 所包含实体
    private List<G6EntitiesBean> entities;

    // 覆盖流程关联表单
    private String isCoverForms;
    // 覆盖业务实体属性
    private String isCoverEntityAttr;
    // 覆盖业务状态信息
    private String isCoverBizStatus;

}
