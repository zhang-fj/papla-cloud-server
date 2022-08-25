package com.papla.cloud.workflow.engine.run.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.papla.cloud.workflow.engine.mapper.ProcessDeployTransitionMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsTransitionStateMapper;
import com.papla.cloud.workflow.engine.mapper.RunInstanceMapper;
import com.papla.cloud.workflow.engine.modal.TransStateBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.run.dao.TransitionDAO;


/**
 * @author linpeng
 * @ClassName: TransitionDAOImpl
 * @Description: 连线处理DAO接口实现类
 * @date 2015年4月23日 下午1:55:01
 */
@Repository("transitionDAO")
public class TransitionDAOImpl implements TransitionDAO {

    @Resource
    private ProcessDeployTransitionMapper processDeployTransitionMapper;
    @Resource
    private RunInsTransitionStateMapper insTransStateMapper;
    @Resource
    private RunInstanceMapper runInstanceMapper;
    @Resource
    private JdbcTemplate jdbcTemplate;


    /*
     * (非 Javadoc)
     * <p>Title: getInsTransitionNextOrder</p>
     * <p>Description: 获取流程实例下连线下一个序号 </p>
     * @param instanceId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#getInsTransitionNextOrder(java.lang.String)
     */
    @Override
    public synchronized int getInsTransitionNextOrder(String instanceId) throws Exception {
        int nextOrder = 0;

        try {
            nextOrder = insTransStateMapper.getMaxInsTransOrder(instanceId);
            nextOrder += 1;

        } catch (Exception e) {
            nextOrder = 0;
        }

        return nextOrder;
    }

    /*
     * (非 Javadoc)
     * <p>Title: insertTransitionState</p>
     * <p>Description:  记录流程流经的连线</p>
     * @param transBean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#insertTransitionState(com.papla.cloud.wf.modal.TransitionBean)
     */
    @Override
    public void insertTransitionState(TransitionBean transBean) throws Exception {
        try {
            // 最大序号
            transBean.setOrderBy(this.getInsTransitionNextOrder(transBean.getInstanceId()));
            // 保存连线信息
            insTransStateMapper.saveTransitionState(transBean);
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: deleteTransitionState</p>
     * <p>Description: 删除流程实例流经的连线 </p>
     * @param transIdList
     * @param insId
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#deleteTransitionState(java.util.List, java.lang.String)
     */
    @Override
    public void deleteTransitionState(List<String> transIdList, String insId) throws Exception {
        List<TransitionBean> beanList = new ArrayList<TransitionBean>();
        for (int i = 0; i < transIdList.size(); i++) {
            TransitionBean bean = new TransitionBean();
            bean.setInstanceId(insId);
            bean.setId((String) transIdList.get(i));
            beanList.add(bean);
        }
        final List<TransitionBean> list = beanList;
        String sql = "delete from xip_wf_ins_transitions where instance_id = ? and transition_id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, list.get(i).getInstanceId());
                ps.setString(2, list.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    /*
     * (非 Javadoc)
     * <p>Title: batDelTransState</p>
     * <p>Description: 流程回退时, 批量清除连线状态信息 </p>
     * @param insId
     * @param acts
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#batDelTransState(java.lang.String, java.util.List)
     */
    @Override
    public void batDelTransState(String insId, List<String> acts) throws Exception {
        try {
            // 封装参数
            HashMap<String, Object> hashmap = new HashMap<String, Object>();
            hashmap.put("insId", insId);
            hashmap.put("acts", acts);

            // 执行删除
            insTransStateMapper.batDelTransState(hashmap);

        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: getTransBean</p>
     * <p>Description: 根据待办Id和连线编码, 取得连线对象信息 </p>
     * @param taskId
     * @param lineCode
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#getTransBean(java.lang.String, java.lang.String)
     */
    @Override
    public TransitionBean getTransBean(String taskId, String lineCode) throws Exception {
        String insId = runInstanceMapper.getRunInstanceIdByTaskId(taskId);
        return this.getTransitionBean(insId, lineCode);
    }

    /*
     * (非 Javadoc)
     * <p>Title: getTransitionBean</p>
     * <p>Description: 根据实例Id和连线编码, 取得连线对象信息 </p>
     * @param insId
     * @param lineCode
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#getTransitionBean(java.lang.String, java.lang.String)
     */
    @Override
    public TransitionBean getTransitionBean(String insId, String lineCode) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", insId);
        params.put("lineCode", lineCode);
        return processDeployTransitionMapper.getTransitionBean(params);
    }

    /*
     * (非 Javadoc)
     * <p>Title: deleteInsTransState</p>
     * <p>Description: 删除流程实例下连线状态信息 </p>
     * @param insId
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#deleteInsTransState(java.lang.String)
     */
    @Override
    public void deleteInsTransState(String insId) throws Exception {
        try {
            insTransStateMapper.clearInsTransStateByInsId(insId);
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: getInsTransStateList</p>
     * <p>Description: 查询实例下连线状态信息</p>
     * @param instanceId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#getInsTransStateList(java.lang.String)
     */
    @Override
    public List<TransStateBean> getInsTransStateList(String instanceId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        return insTransStateMapper.getTransStateList(params);
    }

    /*
     * (非 Javadoc)
     * <p>Title: getBackTransStateList</p>
     * <p>Description: 查询环路流程的回退连线轨迹 </p>
     * @param instanceId
     * @param moveOutTransIds	迁出线ID
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#getBackTransStateList(java.lang.String, java.util.List)
     */
    @Override
    public List<TransStateBean> getBackTransStateList(String instanceId, List<String> moveOutTransIds) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("insId", instanceId);
        map.put("list", moveOutTransIds);

        return insTransStateMapper.getBackTransStateList(map);
    }

    /*
     * (非 Javadoc)
     * <p>Title: deleteBackTransState</p>
     * <p>Description: 删除环路流程的回退连线轨迹 </p>
     * @param backTransStateList
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.TransitionDAO#deleteBackTransState(java.util.List)
     */
    @Override
    public void deleteBackTransState(List<TransStateBean> backTransStateList) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String instanceId = null;
        List<Integer> list = new ArrayList<Integer>();

        for (TransStateBean bean : backTransStateList) {
            instanceId = bean.getInstanceId();
            list.add(bean.getOrderBy());
        }

        map.put("insId", instanceId);
        map.put("list", list);

        insTransStateMapper.deleteBackTransState(map);
    }
}
