package com.papla.cloud.common.mybatis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TabPage<T> {

    /**
     * 总数
     */
    protected long total = 0;

    /**
     * 当前页面数据
     */
    protected List<T> data;

    /**
     * 提示信息
     */
    protected String msg;

}
