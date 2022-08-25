package com.papla.cloud.workflow.engine.mapper;

public interface InstanceArchiveMapper {

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: insertArchInstance
     * @Description: 归档实例信息
     */
    void insertArchInstance(String instanceId);


    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: insertArchInsAttrValue
     * @Description: 归档实例属性信息
     */
    void insertArchInsAttrValue(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: insertArchInsActState
     * @Description: 归档节点状态信息
     */
    void insertArchInsActState(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: insertArchInsTransState
     * @Description: 归档连线状态信息
     */
    void insertArchInsTransState(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: insertArchInsTask
     * @Description: 归档待办信息
     */
    void insertArchInsTask(String instanceId);

    /**
     * @param instanceId 设定文件
     * @Title: insertArchInsActQueue
     * @Description: 归档流程实例动态通知节点审批队列信息
     */
    void insertArchInsActQueue(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteInsActState
     * @Description: 删除待办信息
     */
    void deleteInsTask(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteInsTask
     * @Description: 删除流程实例节点状态
     */
    void deleteInsActState(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteInsTransState
     * @Description: 删除流程实例连线状态
     */
    void deleteInsTransState(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteInsAttrValues
     * @Description: 删除流程实例实体属性
     */
    void deleteInsAttrValues(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteProcessInstance
     * @Description: 删除流程实例
     */
    void deleteProcessInstance(String instanceId);

    /**
     * @param instanceId 设定文件
     * @Title: deleteDynaActQueue
     * @Description: 删除流程实例动态通知节点审批队列信息
     */
    void deleteDynaActQueue(String instanceId);

    // ========================删除归档待办信息==========================

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteArchInstance
     * @Description: 删除归档实例信息
     */
    void deleteArchInstance(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteArchInsAttrValue
     * @Description: 删除归档实例属性信息
     */
    void deleteArchInsAttrValue(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteArchInsActState
     * @Description: 删除归档节点状态信息
     */
    void deleteArchInsActState(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteArchInsTransState
     * @Description: 删除归档连线状态信息
     */
    void deleteArchInsTransState(String instanceId);

    /**
     * @param instanceId
     * @return void    返回类型
     * @Title: deleteArchInsTask
     * @Description: 删除归档待办信息
     */
    void deleteArchInsTask(String instanceId);

    /**
     * @param instanceCode 设定文件
     * @Title: deleteArchInsActQueue
     * @Description: 删除归档流程实例动态通知节点审批队列信息
     */
    void deleteArchInsActQueue(String instanceCode);

}
