package com.papla.cloud.generate.module.model;

import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.generate.module.domain.ModuleTableTemp;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModuleTableTempSaveModel extends SaveModel<ModuleTableTemp> {
    String tableId;
    String cfgId;
}
