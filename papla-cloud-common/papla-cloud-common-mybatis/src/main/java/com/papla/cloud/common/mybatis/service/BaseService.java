package com.papla.cloud.common.mybatis.service;

import java.util.List;
import java.util.Map;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import org.apache.logging.log4j.Logger;

import com.papla.cloud.common.mybatis.domain.Entity;
import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.model.TabPage;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: BaseService<T extends Entity>
 * @Description: TODO Service公共接口，公共业务的标准化方法
 * @date 2018年9月2日
 */
public interface BaseService<T extends Entity> {

    /**
     * 使用钩子方法 获取对应的mapper
     *
     * @return BaseDao<T>
     * @throws Exception
     * @author zhangfj
     * @date 2017年3月27日 上午10:09:51
     */
    public BaseMapper<T> getMapper();

    /**
     * 使用钩子方法 获取对应的子类的日志对象
     *
     * @return Logger
     * @throws Exception
     * @author zhangfj
     * @date 2017年3月27日 上午10:09:51
     */
    public Logger getLogger();

    /**
     * 根据查询查询参数获取分页列表
     *
     * @return JsonPage<T>
     * @throws Exception
     * @params params
     * @author zhangfj
     * @date 2019年4月25日 下午5:00:59
     */
    public TabPage<T> selectForPage(Map<String, Object> params);

    /**
     * 根据主键id 获取对象信息
     *
     * @throws Exception
     * @params params
     * @author zhangfj
     * @date 2019年4月25日 下午5:01:06
     * @return T
     */
    public T selectByPK(Object id);

    /**
     * 根据参数查询单条数据
     *
     * @throws Exception
     * @params params
     * @author zhangfj
     * @date 2019年4月25日 下午5:01:47
     * @return T
     */
    public T selectByPropertys(Map<String, Object> params);


    /**
     * 插入一条信息
     *
     * @throws Exception
     * @params entity
     * @author zhangfj
     * @date 2019年4月25日 下午5:02:10
     * @return Integer
     */
    public Integer insert(T entity);

    /**
     * 修改一条信息
     *
     * @throws Exception
     * @params entity
     * @author zhangfj
     * @date 2019年4月25日 下午5:05:44
     * @return Integer
     */
    public Integer update(T entity);


    /**
     * 批量保存或更新信息
     *
     * @throws Exception
     * @params entitys
     * @author zhangfj
     * @date 2019年4月25日 下午5:00:20
     * @return void
     */
    public List<T> batchSaveOrUpdate(List<T> entitys);

    /**
     * @return void
     * @throws Exception
     * @Title saveOrUpdate
     * @Description TODO 保存或更新
     * @params entity
     */
    public T saveOrUpdate(T entity);

    /**
     * 批量保存或更新信息
     *
     * @throws Exception
     * @params datas
     * @author zhangfj
     * @date 2019年4月25日 下午5:00:20
     * @return void
     */
    public SaveModel<T> batchSaveOrUpdate(SaveModel<T> datas);

    /**
     * 根据对象数组删除数据
     *
     * @throws Exception
     * @params entitys
     * @author: zhangfj
     * @date: 2017年6月6日 上午10:31:39
     * @return: void
     */
    public void delete(List<T> entitys);


    /**
     * 根据对象删除数据
     *
     * @throws Exception
     * @Title: delete
     * @author: zhangfj
     * @date: 2017年6月6日 下午6:44:42
     * @return: void
     */
    public Integer delete(T entity);

    /**
     * 根据参数删除数据
     *
     * @throws Exception
     * @throws Exception
     * @params params
     * @author: zhangfj
     * @date: 2017年6月6日 下午6:44:42
     */
    public Integer deleteByParams(Map<String, Object> params);

    /**
     * 根据ids数组删除数据
     *
     * @throws Exception
     * @throws Exception
     * @params ids
     * @author: zhangfj
     * @date: 2017年6月6日 下午6:44:42
     */
    public Integer deleteByIds(List<Object> ids);

    /**
     * 查询根据所有参数查询信息
     *
     * @return List<T>
     * @throws Exception
     * @params params
     * @author zhangfj
     * @date 2017年3月27日 上午10:09:18
     */
    public List<T> findAll(Map<String, Object> params);

}
