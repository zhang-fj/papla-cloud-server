package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

@Service("sqlActivityConfigExecutorService")
public class SqlActivityConfigExecutorServiceImpl extends AbstractActivityConfigExecutorServiceImpl {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private ProcessEngineCommonService processEngineCommonService;


    @Override
    public List<ActivityExecutorBean> getActivityConfigExecutorList(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) throws Exception {
        // 获取配置SQL信息
        String sqlStr = PropertyHandlerUtil.findPropertyValue(activityBean.getActPropertyBeanList(), WorkFlowConstants.A_CONFIG_SQL);

        // 替换变量后的SQL
        String repSql = processEngineCommonService.replaceVariable(sqlStr, processInstanceBean.getAttrList(), activityBean);

        final List<ActivityExecutorBean> executorList = new ArrayList<ActivityExecutorBean>();

        jdbcTemplate.query(repSql, new RowCallbackHandler() {
            public void processRow(ResultSet rs) throws SQLException {
                ActivityExecutorBean bean = new ActivityExecutorBean();
                // userId
                try {
                    bean.setUserId(rs.getString("userId"));
                } catch (Exception e) {
                }
                // userName
                try {
                    bean.setUserName(rs.getString("userName"));
                } catch (Exception e) {
                }
                // empId
                try {
                    bean.setEmpId(rs.getString("empId"));
                } catch (Exception e) {
                }
                // empCode
                try {
                    bean.setEmpCode(rs.getString("empCode"));
                } catch (Exception e) {
                }
                // empName
                try {
                    bean.setEmpName(rs.getString("empName"));
                } catch (Exception e) {
                }
                // orgName
                try {
                    bean.setOrgId(rs.getString("orgId"));
                } catch (Exception e) {
                }
                // orgName
                try {
                    bean.setOrgName(rs.getString("orgName"));
                } catch (Exception e) {
                }

                executorList.add(bean);
            }

        });
        return executorList;
    }
}
