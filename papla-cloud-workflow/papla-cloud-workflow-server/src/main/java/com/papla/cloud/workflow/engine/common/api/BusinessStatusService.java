package com.papla.cloud.workflow.engine.common.api;

/**
 * @author linp
 * @ClassName: BusinessStatusService
 * @Description: 工作流审批业务状态服务接口类
 * @date 2017年2月27日 上午9:56:18
 */
public interface BusinessStatusService {

    /**
     * @param insCode
     * @param insCol
     * @param bizTabName
     * @param bizPKCol
     * @param bizId
     * @return
     * @throws Exception
     * @Description 按业务主键更新流程实例编码
     */
    public String updateInsCode(String insCode, String insCol, String bizTabName, String bizPKCol, String bizId) throws Exception;

    /**
     * 按业务主键更新业务状态信息
     *
     * @param insCode       流程实例编码
     * @param insCol        流程实例编码储存列
     * @param bizTabName    业务表名
     * @param bizPKCol      业务主键列
     * @param bizId         业务主键
     * @param statusCol     业务状态码存储列
     * @param statusVal     业务状态码
     * @param stautsDescCol 业务状态描述存储列
     * @param stautsDescVal 业务状态码描述
     * @return
     * @throws Exception 设定文件
     * @Title: updateBizStatus
     * @Description: 按业务主键更新业务状态信息
     */
    public String updateBizStatus(String insCode, String insCol, String bizTabName, String bizPKCol, String bizId, String statusCol, String statusVal, String stautsDescCol, String stautsDescVal) throws Exception;


    /**
     * @param insCode       流程实例编码
     * @param insCol        流程实例编码储存列
     * @param bizTabName    业务表名
     * @param statusCol     业务状态码存储列
     * @param statusVal     业务状态码
     * @param stautsDescCol 业务状态描述存储列
     * @param stautsDescVal 业务状态码描述
     * @return
     * @throws Exception 设定文件
     * @Title: updateBizStatus
     * @Description: 按流程实例编码更新业务状态信息
     */
    public String updateBizStatusByCode(String insCode, String insCol, String bizTabName, String statusCol, String statusVal, String stautsDescCol, String stautsDescVal) throws Exception;

}
