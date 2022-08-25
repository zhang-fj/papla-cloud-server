package com.papla.cloud.workflow.engine.modal;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeployPropertyBean extends PropertyBean {

    private static final long serialVersionUID = 1L;

    public DeployPropertyBean() {
        super();
    }

    public DeployPropertyBean(String propertyCode, String propertyName, String propertyValue, String parentId, String deployId, String createBy, Date createDt) {
        super(propertyCode, propertyName, propertyValue, parentId, deployId, createBy, createDt);
    }

}
