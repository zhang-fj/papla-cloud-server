package com.papla.cloud.common.mybatis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SaveModel<T> {

    private List<T> adds;
    private List<T> edits;
    private List<T> dels;

    @JsonIgnore
    public List<T> getAllDatas(){
        // 拼接数组
        List<T> datas= new ArrayList<>();
        if(adds!=null){
            datas.addAll(adds);
        }

        if(edits!= null){
            datas.addAll(edits);
        }

        if(dels!= null){
            datas.addAll(dels);
        }
        return datas;
    }

}
