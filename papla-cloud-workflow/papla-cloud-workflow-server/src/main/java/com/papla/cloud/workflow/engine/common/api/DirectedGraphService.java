package com.papla.cloud.workflow.engine.common.api;

import java.util.List;

/**
 * @author linp
 * @ClassName: DirectedGraphService
 * @Description: 有向图操作集合服务类
 * @date 2016年10月26日 下午3:08:44
 */
public interface DirectedGraphService extends GraphService {

    /**
     * @param vertexs 排序前节点数
     * @param edges   边关系
     * @return
     * @throws Exception 设定文件
     * @Title: isExistsLoop
     * @Description: 判断流程图是否存在环路
     * <p>
     * 1.先将图执行拓扑排序处理。
     * 2.循环结束后，若输出的顶点数小于网中的顶点数，则存在回路信息，否则不存在回路且输出的顶点序列就是一种拓扑序列。
     * </p>
     */
     boolean isExistsLoop(List<Integer> vertexs, List<int[]> edges) throws Exception;

    /**
     * @param vertexs 排序前节点数
     * @param edges   边关系
     * @return
     * @throws Exception 设定文件
     * @Title: sortDirectedGraphByTopology
     * @Description: 对有向图执行拓扑排序处理
     * <p>
     * 由AOV网构造拓扑序列的拓扑排序算法主要是循环执行以下两步，直到不存在入度为0的顶点为止。
     * (1) 选择一个入度为0的顶点并输出之；
     * (2) 从网中删除此顶点及所有出边。
     * 循环结束后，若输出的顶点数小于网中的顶点数，则存在回路信息，否则不存在回路且输出的顶点序列就是一种拓扑序列。
     * 注意：拓扑序列只是所有拓扑序列其中的一种，而不是唯一拓扑序列
     * </p>
     */
     List<Object> sortDirectedGraphByTopology(List<Integer> vertexs, List<int[]> edges) throws Exception;


}
