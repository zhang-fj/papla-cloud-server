package com.papla.cloud.workflow.engine.modal;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:PropertyBean
 * Function:
 *
 * @author zhangfj
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class PropertyBean extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PropertyBean() {
    }

    public PropertyBean(String propertyCode, String propertyName, String propertyValue, String parentId, String deployId, String createBy, Date createDt) {
        this.propertyCode = propertyCode;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.parentId = parentId;
        this.deployId = deployId;
        this.createBy = createBy;
        this.createDt = createDt;
    }

    /**
     * 所属流程
     **/
    protected String processId;

    /**
     * 部署ID
     */
    protected String deployId;

    /**
     * 所属流程编码
     */
    protected String processCode;

    /**
     * 属性名称
     */
    protected String processName;

    /**
     * 属性编码
     **/
    protected String propertyCode;

    /**
     * 属性名称
     **/
    protected String propertyName;

    /**
     * 属性值
     **/
    protected String propertyValue;

    /**
     * 流程或者节点id
     */
    protected String parentId;

}
