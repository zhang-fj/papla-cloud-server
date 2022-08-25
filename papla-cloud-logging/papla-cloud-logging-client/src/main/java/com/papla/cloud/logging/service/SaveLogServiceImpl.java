package com.papla.cloud.logging.service;

import com.papla.cloud.common.logging.dto.LogDTO;
import com.papla.cloud.common.logging.service.SaveLogService;
import com.papla.cloud.logging.client.LoggingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service()
@RequiredArgsConstructor
public class SaveLogServiceImpl implements SaveLogService {

    private final LoggingFeignClient loggingFeignClient;

    @Override
    public void save(LogDTO log) {
        loggingFeignClient.save(log);
    }

}
