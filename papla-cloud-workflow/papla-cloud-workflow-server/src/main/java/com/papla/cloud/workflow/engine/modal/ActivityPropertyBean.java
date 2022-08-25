package com.papla.cloud.workflow.engine.modal;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityPropertyBean extends PropertyBean {

    private static final long serialVersionUID = 1L;

    private String actId;

    public ActivityPropertyBean() {
        super();
    }

    public ActivityPropertyBean(String propertyCode, String propertyName, String propertyValue, String parentId, String deployId, String createBy, Date createDt) {
        super(propertyCode, propertyName, propertyValue, parentId, deployId, createBy, createDt);
    }

}
