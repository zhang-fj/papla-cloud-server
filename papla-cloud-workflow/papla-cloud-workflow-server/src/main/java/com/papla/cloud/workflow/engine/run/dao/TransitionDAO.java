package com.papla.cloud.workflow.engine.run.dao;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.TransStateBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;

/**
 * @author linpeng
 * @ClassName: TransitionDAO
 * @Description: 连线处理DAO接口
 * @date 2015年4月23日 下午1:54:43
 */
public interface TransitionDAO {

    /**
     * @param instanceId
     * @return
     * @throws Exception 设定文件
     * @Title: getInsTransitionNextOrder
     * @Description： 获取流程实例下连线下一个序号
     */
    public int getInsTransitionNextOrder(String instanceId) throws Exception;

    /**
     * @param transBean
     * @return void    返回类型
     * @throws Exception
     * @Title: insertTransitionState
     * @Description: 记录流程实例流经的连线
     */
    public void insertTransitionState(TransitionBean transBean) throws Exception;

    /**
     * @param transIdList
     * @param insId
     * @return void    返回类型
     * @throws Exception
     * @Title: deleteTransitionState
     * @Description: 删除流程实例流经的连线
     */
    public void deleteTransitionState(List<String> transIdList, String insId) throws Exception;

    /**
     * @param insId
     * @param acts
     * @return void    返回类型
     * @throws Exception
     * @Title: batDelTransState
     * @Description: 流程回退时, 批量清除连线状态信息
     */
    public void batDelTransState(String insId, List<String> acts) throws Exception;

    /**
     * @param taskId
     * @param lineCode
     * @return TransitionBean    返回类型
     * @throws Exception
     * @Title: getTransBean
     * @Description: 获取连线对象信息
     */
    public TransitionBean getTransBean(String taskId, String lineCode) throws Exception;

    /**
     * @param insId
     * @param lineCode
     * @return TransitionBean    返回类型
     * @throws Exception
     * @Title: getTransitionBean
     * @Description: 取得连线对象信息
     */
    public TransitionBean getTransitionBean(String insId, String lineCode) throws Exception;

    /**
     * @param insId
     * @return void    返回类型
     * @throws Exception
     * @Title: deleteInsTransState
     * @Description: 删除流程实例下连线信息
     */
    public void deleteInsTransState(String insId) throws Exception;

    /**
     * @param instanceId
     * @return List<TransStateBean>    返回类型
     * @Title: getInsTransStateList
     * @Description: 查询实例下连线状态信息
     */
    public List<TransStateBean> getInsTransStateList(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @param moveOutTransIds 迁出线ID
     * @return
     * @throws Exception 设定文件
     * @Title: getBackTransStateList
     * @Description: 查询环路流程的回退连线轨迹
     */
    public List<TransStateBean> getBackTransStateList(String instanceId, List<String> moveOutTransIds) throws Exception;

    /**
     * @param backTransStateList
     * @throws Exception 设定文件
     * @Title: deleteBackTransState
     * @Description: 删除环路流程的回退连线轨迹
     */
    public void deleteBackTransState(List<TransStateBean> backTransStateList) throws Exception;
}
