package com.papla.cloud.common.mybatis.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.papla.cloud.common.mybatis.model.TabPage;

public class PageUtils {


    public static <X> TabPage<X> selectForPage(Map<String, Object> params, SelectCallBack<X> callBack) throws Exception {
        TabPage<X> tabPage = new TabPage<X>();
        Integer page = (Integer) params.get("page");
        Integer size = (Integer) params.get("size");
        List<X> list = new ArrayList<X>();
        if (size != null) {
            int offset = 0;
            if (page != null) {
                offset = page * size;
            }
            PageHelper.startPage(offset, size);
            PageInfo<X> da = new PageInfo<X>(callBack.query(params));
            list = da.getList();
            tabPage.setData(list);
            tabPage.setTotal(da.getTotal());
        } else {
            list = callBack.query(params);
            tabPage.setData(list);
            tabPage.setTotal(list.size());
        }
        return tabPage;
    }

    public interface SelectCallBack<X> {
        public List<X> query(Map<String, Object> params) throws Exception;
    }

}
