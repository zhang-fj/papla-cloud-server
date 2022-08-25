package com.papla.cloud.logging.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.papla.cloud.common.jpa.utils.QueryHelp;
import com.papla.cloud.common.logging.dto.LogDTO;
import com.papla.cloud.logging.pojo.mapstruct.LogMapper;
import com.papla.cloud.logging.repository.LogRepository;
import com.papla.cloud.logging.service.LoggingService;
import com.papla.cloud.common.jpa.utils.PageUtil;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.logging.pojo.domain.Log;
import com.papla.cloud.logging.pojo.dto.LogQueryCriteria;
import com.papla.cloud.logging.pojo.mapstruct.LogErrorMapper;
import com.papla.cloud.logging.pojo.mapstruct.LogSmallMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Service
@RequiredArgsConstructor
public class LoggingServiceImpl implements LoggingService {

    private final LogRepository logRepository;
    private final LogMapper logMapper;
    private final LogErrorMapper logErrorMapper;
    private final LogSmallMapper logSmallMapper;

    @Override
    public Object queryAll(LogQueryCriteria criteria, Pageable pageable) {
        Page<Log> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)), pageable);
        if("ERROR".equals(criteria.getLogType())){
            return PageUtil.toPage(page.map(logErrorMapper::toDto));
        }
        return PageUtil.toPage(page.map(logSmallMapper::toDto));
    }

    @Override
    public List<Log> queryAll(LogQueryCriteria criteria) {
        return logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)));
    }

    @Override
    public Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
        Page<Log> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)), pageable);
        return PageUtil.toPage(page.map(logSmallMapper::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(LogDTO log) {
        logRepository.save(logMapper.toEntity(log));
    }

    @Override
    public Object findByErrDetail(String id) {
        Log log = logRepository.findById(id).orElseGet(Log::new);
        byte[] details = log.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
    }

    @Override
    public void download(List<Log> logs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Log log : logs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", log.getUsername());
            map.put("IP", log.getRequestIp());
            map.put("IP来源", log.getAddress());
            map.put("描述", log.getDescription());
            map.put("浏览器", log.getBrowser());
            map.put("请求耗时/毫秒", log.getTime());
            map.put("异常详情", new String(ObjectUtil.isNotNull(log.getExceptionDetail()) ? log.getExceptionDetail() : "".getBytes()));
            map.put("创建日期", log.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByError() {
        logRepository.deleteByLogType("ERROR");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        logRepository.deleteByLogType("INFO");
    }
}
