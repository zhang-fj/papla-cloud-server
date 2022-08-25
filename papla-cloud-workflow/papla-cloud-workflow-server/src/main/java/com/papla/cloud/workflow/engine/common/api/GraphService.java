package com.papla.cloud.workflow.engine.common.api;

/**
 * @author linp
 * @ClassName: GraphService
 * @Description: 图操作集合服务类
 * @date 2016年10月26日 下午3:08:39
 */
public interface GraphService {

    /**
     * @param vertexs
     * @param edges
     * @param startVertex
     * @return
     * @throws Exception 设定文件
     * @Title: depthFSearchGraph
     * @Description: 深度优先搜索图
     */
     int[] depthFSearchGraph(int[] vertexs, int[][] edges, int startVertex) throws Exception;

    /**
     * @param vertexs
     * @param edges
     * @param startVertex
     * @return
     * @throws Exception 设定文件
     * @Title: broadFSearchGraph
     * @Description: 广度优先搜索图
     */
     int[] broadFSearchGraph(int[] vertexs, int[][] edges, int startVertex) throws Exception;

}
