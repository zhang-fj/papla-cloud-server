package com.papla.cloud.workflow.cfg.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;

import com.papla.cloud.workflow.cfg.domain.Message;
import com.papla.cloud.workflow.cfg.mapper.MessageMapper;
import com.papla.cloud.workflow.cfg.service.MessageService;

/**
* @Title: MessageServiceImpl
* @Description: TODO 消息定义表管理
* @author 
* @date 2021-10-05
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final MessageMapper mapper;

	@Override
	public BaseMapper<Message> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<Message> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Message message : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("消息编码", message.getMesCode());
			map.put("语言", message.getMesLang());
			map.put("内容", message.getMesContent());
			map.put("创建日期", message.getCreateDt());
			map.put("创建人", message.getCreateBy());
			map.put("最后更新日期", message.getUpdateDt());
			map.put("最后更新人", message.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
