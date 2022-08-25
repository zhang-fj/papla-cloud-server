package com.papla.cloud.workflow.engine.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.workflow.engine.modal.CreateByEntityParams;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.engine.core.task.WorkTaskService;
import com.papla.cloud.workflow.engine.run.service.ProcessRunService;
import com.papla.cloud.workflow.util.XipUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * @author zhangfj
 * @ClassName: ProcessRunController
 * @Description: 封装流程一些通用的方法
 * @date 2015年4月29日 下午1:41:12
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "流程:通用的方法 ")
@RequestMapping("/run/")
public class ProcessRunController {

    private final WorkTaskService workTaskService;

    private final ProcessRunService processRunService;


    @Log("根据【流程编码】查找【流程部署ID】")
    @ApiOperation("根据【流程编码】查找【流程部署ID】")
    @RequestMapping(value = "getDeployIdByProcessCode", method = RequestMethod.GET)
    public ResponseEntity<String> getDeployIdByProcessCode(String processCode) throws Exception {
        return new ResponseEntity<>(processRunService.getDeployIdByProcessCode(processCode), HttpStatus.OK);
    }

    @Log("根据【流程部署ID】创建【流程实例】 ")
    @ApiOperation("根据【流程部署ID】创建【流程实例】 ")
    @RequestMapping(value = "createInstanceByDeployId", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> createInstanceByDeployId(String deployId, String businessId) throws Exception {
        if (deployId == null || "".equals(deployId)) {
            throw new Exception(XipUtil.getMessage("XIP_WF_ACTION_0003", null));
        }
        return new ResponseEntity<>(processRunService.createInstanceByDeployId(deployId, businessId),HttpStatus.OK);
    }

    @Log("根据流程Code创建流程实例 ")
    @ApiOperation("根据流程Code创建流程实例 ")
    @RequestMapping(value = "revokeByInsCode", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> revokeByInsCode(String instanceCode) throws Exception {
        if (instanceCode == null || "".equals(instanceCode)) {
            throw new Exception(XipUtil.getMessage("XIP_WF_ACTION_0006", null));
        }
        return new ResponseEntity<>(processRunService.revokeProcess(instanceCode),HttpStatus.OK);
    }

    @Log("提交流程  ")
    @ApiOperation("提交流程 ")
    @RequestMapping(value = "submitProcessOld", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> submitProcessOld(@RequestBody CreateByEntityParams entityParams) throws Exception {
        String instanceCode = entityParams.getInstanceCode();
        if (instanceCode == null || "".equals(instanceCode)) {
            throw new Exception(XipUtil.getMessage("XIP_WF_ACTION_0005", null));
        }
        return new ResponseEntity<>(processRunService.submitProcess(instanceCode, entityParams.getEntityAttJson(), entityParams.getUserIds()),HttpStatus.OK);
    }

    /**
     * 返回值JSON字符串说明：
     * flag:0   正确，1 错误, 错误时msg为错误信息
     * {"flag":"","msg":""}
     *
     * @throws Exception
     */
    @Log("按实例code重新启动流程流程  ")
    @ApiOperation("按实例code重新启动流程 ")
    @RequestMapping(value = "restartInstance", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> restartInstance(String instanceCode, String processId) throws Exception {
        if (instanceCode == null || "".equals(instanceCode)) {
            throw new Exception(XipUtil.getMessage("XIP_WF_ACTION_0007", null));
        }
        if (instanceCode == null || "".equals(instanceCode)) {
            processId = "";
        }
        return new ResponseEntity<>(processRunService.restartInstance(instanceCode),HttpStatus.OK);
    }

    @Log("完成待办")
    @ApiOperation("完成待办")
    @RequestMapping(value = "completeWorkItem", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> completeWorkItem(String taskId, String lineCode, String comment, String userIds) {
        String userIdArray[] = null;
        if (!StringUtils.isEmpty(userIds)) {
            userIdArray = userIds.split(",");
        }
        ;
        return new ResponseEntity<>(processRunService.completeWorkItem(taskId, lineCode, comment, userIdArray),HttpStatus.OK);
    }

//	@Log("取得回退节点")
//    @ApiOperation("取得回退节点")
//    @PreAuthorize("@el.check('wf:run:getBackActivityList')")
//    @RequestMapping(value="getBackActivityList",method=RequestMethod.GET)
//	public Map<String, Object> getBackActivityList(String taskId) throws Exception {
//		return workTaskService.getBackActivityList(taskId);
//	}
//
//	@Log("回退")
//    @ApiOperation("回退")
//    @PreAuthorize("@el.check('wf:run:doBack')")
//    @RequestMapping(value="doBack",method=RequestMethod.GET)
//	public Map<String, Object> doBack(String taskId, String backToActivityId, String comment) {
//		return workTaskService.doBack(taskId, backToActivityId, comment);
//	}

//	@Log("根据待办ID, 获取征询或委派的用户信息")
//    @ApiOperation("根据待办ID, 获取征询或委派的用户信息")
//    @PreAuthorize("@el.check('wf:run:getUsersForConsultOrDelegate')")
//    @RequestMapping(value="getUsersForConsultOrDelegate",method=RequestMethod.GET)
//	public Map<String,Object> getUsersForConsultOrDelegate(String taskId,String condition,int page,int size) throws Exception {
//		return workTaskService.getUserListByTaskId(taskId, condition, page*size, size);
//	}

    @Log("征询")
    @ApiOperation("征询")
    @RequestMapping(value = "doConsult", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> doConsult(String taskId, String userId, String comment) throws Exception {
        return new ResponseEntity<>(workTaskService.doConsult(taskId, userId, comment),HttpStatus.OK);
    }

    @Log("委派")
    @ApiOperation("委派")
    @RequestMapping(value = "doDelegate", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> doDelegate(String taskId, String userId, String comment) throws Exception {
        return new ResponseEntity<>(workTaskService.doDelegate(taskId, userId, comment),HttpStatus.OK);
    }

    @Log("转发获取节点执行人")
    @ApiOperation("转发获取节点执行人")
    @RequestMapping(value = "getForwardExecutors", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getForwardExecutors(String taskId) throws Exception {
        return new ResponseEntity<>(workTaskService.getForwardExecutors(taskId),HttpStatus.OK);
    }

    @Log("待办转发处理")
    @ApiOperation("待办转发处理")
    @RequestMapping(value = "doForward", method = RequestMethod.GET)
    public ResponseEntity<Object> doForward(String taskId, String userIds) throws Exception {
        String[] userArray = userIds.split(",");
        workTaskService.doForward(taskId, userArray);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Log("取得按钮列表")
    @ApiOperation("取得按钮列表")
    @RequestMapping(value = "getButtonsByTaskId", method = RequestMethod.GET)
    public ResponseEntity<List<TransitionBean>> getButtonsByTaskId(String taskId) throws Exception {
        return new ResponseEntity<>(workTaskService.getButtonsByTaskId(taskId),HttpStatus.OK);
    }

//	@Log("获取待办关联表单信息")
//    @ApiOperation("获取待办关联表单信息")
//    @PreAuthorize("@el.check('wf:run:getTaskBaseInfo')")
//    @RequestMapping(value="getTaskBaseInfo",method=RequestMethod.GET)
//	public Map<String, String> getTaskBaseInfo(String taskId) throws Exception {
//		Map<String,String> obj=new HashMap<String,String>();
//		String createType=null;
//		// 取得待办信息
//		WorkItemBean bean = workTaskService.findWorkItemByTaskId(taskId);
//		createType = bean.getCreateType();
//		// 取得关联表单
//		String url = workTaskService.getFormUrlByTaskId(taskId);
//		url = (url == null ? "" : url);
//
//		if (!"".equals(url)) { // 处理URL参数
//			if (url.indexOf("?") == -1) {
//				url = url.concat("?taskId=" + taskId);
//			} else {
//				url = url.concat("&taskId=" + taskId);
//			}
//		}
//
//		// 封装返回信息
//		obj.put("formUrl", url) ;
//		obj.put("createType", createType) ;
//		return obj;
//	}

//	@Log("撤销征询")
//    @ApiOperation("撤销征询")
//    @PreAuthorize("@el.check('wf:run:revokeConsultTask')")
//    @RequestMapping(value="revokeConsultTask",method=RequestMethod.GET)
//	public Map<String, Object> revokeConsultTask(String taskId, String comment) {
//		return workTaskService.revokeConsultTask(taskId, comment);
//	}
//
//	@Log("征询反馈处理")
//    @ApiOperation("征询反馈处理")
//    @PreAuthorize("@el.check('wf:run:closeConsultTask')")
//    @RequestMapping(value="closeConsultTask",method=RequestMethod.GET)
//	public Map<String, Object> closeConsultTask(String taskId, String comment) {
//		return workTaskService.closeConsultTask(taskId, comment);
//	}

    @Log("撤销流程")
    @ApiOperation("撤销流程")
    @RequestMapping(value = "revokeProcess", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> revokeProcess(String instanceCode) {
        return new ResponseEntity<>(processRunService.revokeProcess(instanceCode),HttpStatus.OK);
    }

    @Log("关闭抄送")
    @ApiOperation("关闭抄送")
    @RequestMapping(value = "closeCopyToTask", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> closeCopyToTask(String taskId) throws Exception {
        return new ResponseEntity<>(workTaskService.closeCopyToTask(taskId, ""),HttpStatus.OK);
    }

    @Log("驳回流程")
    @ApiOperation("驳回流程")
    @RequestMapping(value = "rejectProcess", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> rejectProcess(String taskId, String comment) {
        return new ResponseEntity<>(processRunService.rejectProcess(taskId, comment),HttpStatus.OK);
    }

    @ApiOperation("分页查询用户待办任务列表")
    @RequestMapping(value = "getTaskByUser", method = RequestMethod.POST)
    public ResponseEntity<TabPage<WorkItemBean>> getTaskByUser(@RequestBody Map<String, Object> params) throws Exception {
        List<String> states = new ArrayList<String>();
        states.add("open");
        states.add("hung-up");
        params.put("states", states);
        return new ResponseEntity<>(workTaskService.getUserTaskPage(params),HttpStatus.OK);
    }

    @Log("分页查询用户已办任务列表")
    @ApiOperation("分页查询用户已办任务列表")
    @RequestMapping(value = "getClosedTaskByUser", method = RequestMethod.POST)
    public ResponseEntity<TabPage<WorkItemBean>> getClosedTaskByUser(@RequestBody Map<String, Object> params) throws Exception {
        return new ResponseEntity<>(workTaskService.getUserTaskPage(params),HttpStatus.OK);
    }

    @Log("分页查询用户归档任务列表")
    @ApiOperation("分页查询用户归档任务列表")
    @RequestMapping(value = "getArchTaskByUser", method = RequestMethod.POST)
    public ResponseEntity<TabPage<WorkItemBean>> getArchTaskByUser(@RequestBody Map<String, Object> params) throws Exception {
        params.put("arch", "Y");
        return new ResponseEntity<>(workTaskService.getUserTaskPage(params),HttpStatus.OK);
    }

    @Log("获取审批历史记录")
    @ApiOperation("获取审批历史记录")
    @RequestMapping(value = "findWorkItemListByInstanceCode", method = RequestMethod.GET)
    public ResponseEntity<List<WorkItemBean>> findWorkItemListByInstanceCode(String instanceCode) throws Exception {
        return new ResponseEntity<>(workTaskService.findWorkItemListByInstanceCode(instanceCode),HttpStatus.OK);
    }

}
