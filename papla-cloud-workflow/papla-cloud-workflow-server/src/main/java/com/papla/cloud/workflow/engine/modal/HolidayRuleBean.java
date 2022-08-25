package com.papla.cloud.workflow.engine.modal;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 假期规则
 *
 * @author zhangfj
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class HolidayRuleBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    private String privilegeId;
    private String masterUserId;
    private String proxyUserId;
    private Date beginDate;
    private Date endDate;

    private String masterUserName;
    private String proxyUserName;
    private String enableFlag;

}
