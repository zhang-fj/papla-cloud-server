package com.papla.cloud.generate.module.service.impl;

import com.papla.cloud.common.mybatis.exception.MessageRuntimeException;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.generate.code.domain.CodeTempFile;
import com.papla.cloud.generate.code.mapper.CodeTempFileMapper;
import com.papla.cloud.generate.module.domain.ModuleTableTemp;
import com.papla.cloud.generate.module.mapper.ModuleTableTempMapper;
import com.papla.cloud.generate.module.model.ModuleTableTempSaveModel;
import com.papla.cloud.generate.module.service.ModuleTableTempService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleTableTempServiceImpl
 * @Description: TODO   模块关联表模板配置管理
 * @date 2021-09-05
 */
@Service
@RequiredArgsConstructor
public class ModuleTableTempServiceImpl extends BaseServiceImpl<ModuleTableTemp> implements ModuleTableTempService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Logger getLogger(){
        return logger;
    }

    private final ModuleTableTempMapper mapper;

    private final CodeTempFileMapper fileMapper;

    @Override
    public BaseMapper<ModuleTableTemp> getMapper(){
        return mapper;
    }

    @Override
    public List<ModuleTableTemp> temps(Map<String, Object> params) throws Exception {

        if (StringUtils.isAllBlank((String) params.get("tableId"))) throw new MessageRuntimeException("关联表ID不能为空");
        if (StringUtils.isBlank((String) params.get("cfgId"))) params.put("cfgId", "not_data");

        // 查询已配置【关联模板】
        List<ModuleTableTemp> temps = findAll(params);

        if (temps == null || temps.size() == 0) {
            temps = new ArrayList<ModuleTableTemp>();
        }

        Set<String> fileids = new HashSet<String>();

        for (ModuleTableTemp temp : temps) {
            fileids.add(temp.getTempFileId());
        }

        // 查询当前【模板配置】下【模板文件】
        List<CodeTempFile> files = fileMapper.findAll(params);

        if (files != null) {
            for (CodeTempFile file : files) {
                // 如果【模板文件】未配置，则添加追加配置
                if (!fileids.contains(file.getId())) {
                    ModuleTableTemp temp = new ModuleTableTemp();
                    temp.setTableId((String) params.get("tableId"));
                    temp.setTempCfgId(file.getCfgId());
                    temp.setTempFileId(file.getId());
                    temp.setTempFileName(file.getTempName());
                    temp.setTempCate(file.getTempCate());
                    temp.setGanApi("N");
                    temp.setGenMenu("N");
                    temp.setCover("N");
                    temps.add(temp);
                }
            }
        }

        return temps;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public ModuleTableTempSaveModel batchSave(ModuleTableTempSaveModel data) throws Exception {

        // 添加【新增数据】
        List<ModuleTableTemp> adds = data.getAdds();
        if (adds != null) {
            for (ModuleTableTemp entity : adds) {
                if (StringUtils.isNotBlank(entity.getId())) {
                    mapper.update(entity);
                } else {
                    mapper.insert(entity);
                }
            }
        }

        return data;
    }

    @Override
    public void download(List<ModuleTableTemp> entitys, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ModuleTableTemp moduleTableTemp : entitys) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("关联表ID", moduleTableTemp.getTableId());
            map.put("模板配置", moduleTableTemp.getTempCfgId());
            map.put("模板文件", moduleTableTemp.getTempFileId());
            map.put("创建日期", moduleTableTemp.getCreateDt());
            map.put("创建人", moduleTableTemp.getCreateBy());
            map.put("最后更新日期", moduleTableTemp.getUpdateDt());
            map.put("最后更新人", moduleTableTemp.getUpdateBy());
            map.put("模板名称", moduleTableTemp.getTempCfgName());
            map.put("模板名称", moduleTableTemp.getTempFileName());
            map.put("模板分类", moduleTableTemp.getTempCate());
            map.put("生成菜单", moduleTableTemp.getGenMenu());
            map.put("生成接口", moduleTableTemp.getGanApi());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
