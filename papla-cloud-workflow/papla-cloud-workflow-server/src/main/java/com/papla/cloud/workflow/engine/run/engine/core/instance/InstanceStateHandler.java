package com.papla.cloud.workflow.engine.run.engine.core.instance;


import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;
import com.papla.cloud.workflow.util.AppContext;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: InstanceStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.instance
 * @Description: 流程实例状态维护 启动状态 - started，
 * 运行状态 - running，
 * 中止状态 - terminate，
 * 取消状态 - canceled，
 * 完成状态 - complete。
 * @date 2021年8月26日 下午8:33:44
 */
public abstract class InstanceStateHandler {

    /**
     * @return ProcessInstanceDAO
     * @Title getProcessInstanceDAO
     * @Description TODO 取得容器对象
     */
    public ProcessInstanceDAO getProcessInstanceDAO() {
        return (ProcessInstanceDAO) AppContext.getApplicationContext().getBean("processInstanceDAO");
    }

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toStart
     * @Description TODO    启动实例
     */
    public abstract void toStart(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toRun
     * @Description TODO    运行实例
     */
    public abstract void toRun(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toTerminate
     * @Description TODO    中断实例
     */
    public abstract void toTerminate(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toCancle
     * @Description TODO    撤销实例
     */
    public abstract void toCancle(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toComplete
     * @Description TODO    完成实例
     */
    public abstract void toComplete(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @param newInstanceState
     * @return ProcessInstanceBean
     * @throws Exception
     * @Title setInstanceBean
     * @Description TODO    设置实例更新参数信息
     */
    public ProcessInstanceBean setInstanceBean(ProcessInstanceBean bean, String newInstanceState) throws Exception {
        // 当前用户
        String lastUpdatedBy = CurrentUserUtil.getCurrentUserId() == null ? bean.getCurrentUserId() : CurrentUserUtil.getCurrentUserId();
        // 当前时间
        bean.setInstanceState(newInstanceState);
        bean.setUpdateDt(CurrentUserUtil.getCurrentDate());
        bean.setUpdateBy(lastUpdatedBy);
        return bean;
    }
}
