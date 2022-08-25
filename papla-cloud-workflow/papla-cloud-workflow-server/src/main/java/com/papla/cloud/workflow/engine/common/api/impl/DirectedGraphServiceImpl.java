package com.papla.cloud.workflow.engine.common.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.DirectedGraphService;
import com.papla.cloud.workflow.util.XipUtil;

/**
 * @author linp
 * @ClassName: DirectedGraphServiceImpl
 * @Description: 有向图操作集合服务接口实现类
 * @date 2016年10月26日 下午3:08:48
 */
@Service("directedGraphService")
public class DirectedGraphServiceImpl implements DirectedGraphService {

    /*
     * (非 Javadoc)
     * <p>Title: isExistsLoop</p>
     * <p>Description: 判断流程图是否存在环路 </p>
     * <p>
     * 	  1.先将图执行拓扑排序处理。
     * 	  2.循环结束后，若输出的顶点数小于网中的顶点数，则存在回路信息，否则不存在回路且输出的顶点序列就是一种拓扑序列。
     * </p>
     * @param map
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.DirectedGraphService#isExistsLoop(java.util.Map)
     */
    @Override
    public boolean isExistsLoop(List<Integer> vertexs, List<int[]> edges) throws Exception {
        boolean flag = false;

        // 排序前顶点数
        List<Integer> beforeSortedVertexs = new ArrayList<Integer>();
        beforeSortedVertexs.addAll(vertexs);

        // 排序后顶点数
        List<Object> afterSortedVertexs = this.sortDirectedGraphByTopology(vertexs, edges);

        // 如果排序前顶点数大于排序后顶点数，则图中存在回路
        if (beforeSortedVertexs.size() > afterSortedVertexs.size()) {
            flag = true;
        }

        return flag;
    }

    /*
     * (非 Javadoc)
     * <p>Title: execTopologicalSorting4AOV</p>
     * <p>Description: 对AOV网执行拓扑排序处理 </p>
     * <p> 有效图拓扑排序说明：
     *    由AOV网构造拓扑序列的拓扑排序算法主要是循环执行以下两步，直到不存在入度为0的顶点为止。
     * 		 (1) 选择一个入度为0的顶点并输出之；
     * 		 (2) 从网中删除此顶点及所有出边。
     * 	    循环结束后，若输出的顶点数小于网中的顶点数，则存在回路信息，否则不存在回路且输出的顶点序列就是一种拓扑序列。
     * </p>
     * @param map
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.DirectedGraphService#execTopologicalSorting4AOV(java.util.Map)
     */
    @Override
    public List<Object> sortDirectedGraphByTopology(List<Integer> vertexs, List<int[]> edges) throws Exception {
        List<Object> sortedVertexs = new ArrayList<Object>();

        // 条件校验
        if (vertexs == null) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0023", null));
        if (vertexs != null && vertexs.size() == 0) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0023", null));

        if (edges == null) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0024", null));
        if (edges != null && edges.size() == 0) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0024", null));

        // 执行排序处理
        this.sortGraph(vertexs, edges, sortedVertexs);

        // 返回处理
        return sortedVertexs;
    }

    /**
     * @param vertexs
     * @param edges
     * @param sortedVertexs
     * @throws Exception 设定文件
     * @Title: sortGraph
     * @Description: 执行图的拓扑排序处理
     */
    private void sortGraph(List<Integer> vertexs, List<int[]> edges, List<Object> sortedVertexs) throws Exception {
        // 查找入度为0的顶点
        List<Integer> copyVertexs = new ArrayList<Integer>();
        copyVertexs.addAll(vertexs);

        for (int[] e : edges) {
            int toActId = e[1];
            copyVertexs.remove(Integer.valueOf(toActId));
        }

        // 删除所有入度为0的顶点及其出边
        if (copyVertexs.size() > 0) {
            for (Integer v : copyVertexs) {
                sortedVertexs.add(v);
                for (int i = 0; i < edges.size(); i++) {
                    int[] e = edges.get(i);
                    if (v.intValue() == e[0]) {
                        edges.remove(e);
                        i--;
                    }
                }
                vertexs.remove(v);
            }
            // 递归
            if (edges != null && edges.size() > 0) {
                this.sortGraph(vertexs, edges, sortedVertexs);

            } else {
                // 不存在线但仍存在节点，则直接加入排序后节点数列表中
                if (!vertexs.isEmpty()) {
                    sortedVertexs.addAll(vertexs);
                }
            }
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: depthFSearchGraph</p>
     * <p>Description: 有向图深度优先搜索图 </p>
     * @param vertexs
     * @param edges
     * @param startVertex
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.GraphService#depthFSearchGraph(int[], int[][], int)
     */
    @Override
    public int[] depthFSearchGraph(int[] vertexs, int[][] edges, int startVertex) throws Exception {
        return null;
    }

    /*
     * (非 Javadoc)
     * <p>Title: broadFSearchGraph</p>
     * <p>Description: 有向图广度优先搜索图 </p>
     * @param vertexs
     * @param edges
     * @param startVertex
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.GraphService#broadFSearchGraph(int[], int[][], int)
     */
    @Override
    public int[] broadFSearchGraph(int[] vertexs, int[][] edges, int startVertex) throws Exception {
        return null;
    }

}
