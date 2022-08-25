package com.papla.cloud.common.logging.service;


import com.papla.cloud.common.logging.dto.LogDTO;

public interface SaveLogService {

   void save(LogDTO log);

}
