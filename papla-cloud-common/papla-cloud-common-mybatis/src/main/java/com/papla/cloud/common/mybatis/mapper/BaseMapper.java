package com.papla.cloud.common.mybatis.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: Entity.java
 * @Description: TODO Mybaits Mapper 基础接口，主要提供基本增删改查标准化方法
 * @date 2018年9月2日
 */
public interface BaseMapper<T> {

    /**
     * 根据查询信息获取信息总数量
     *
     * @param param
     * @return
     * @author zhangfj
     * @date 2017年3月27日 上午10:23:02
     */
    public Integer selectCount(Map<String, Object> param);

    /**
     * 根据id获取一条数据
     *
     * @param id
     * @return
     * @author zhangfj
     * @date 2017年3月27日 上午10:22:34
     */
    public T selectByPK(Object id);


    /**
     * 根据参数获取一条数据
     *
     * @param params
     * @throws Exception
     * @Title: selectByPropertys
     * @Description: TODO
     * @author: zhangfj
     * @date: 2017年6月29日 上午10:31:52
     * @return: T
     */
    public T selectByPropertys(Map<String, Object> params);

    /**
     * 根据实体删除一条数据
     *
     * @param entity
     * @return
     * @author zhangfj
     * @date 2017年3月27日 上午10:22:34
     */
    public Integer delete(T entity);


    /**
     * 根据参数删除
     *
     * @param param
     */
    public Integer deleteByParams(Map<String, Object> param);

    /**
     * 根据id数组删除
     *
     * @param ids
     */
    public Integer deleteByIds(List<Object> ids);

    /**
     * 添加一条数据
     *
     * @param entity
     * @author zhangfj
     * @date 2017年3月27日 上午10:23:29
     */
    public Integer insert(T entity);

    /**
     * 修改一条数据
     *
     * @param entity
     * @author zhangfj
     * @date 2017年3月27日 上午10:23:43
     */
    public Integer update(T entity);

    /**
     * 根据参数查询所有数据
     *
     * @param param
     * @return
     * @throws Exception
     * @author zhangfj
     * @date 2017年3月27日 上午10:24:13
     */
    public List<T> findAll(Map<String, Object> param);

}
