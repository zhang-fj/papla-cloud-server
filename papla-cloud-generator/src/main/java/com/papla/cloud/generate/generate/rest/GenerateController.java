package com.papla.cloud.generate.generate.rest;

import cn.hutool.core.util.ZipUtil;
import com.papla.cloud.common.mybatis.exception.MessageRuntimeException;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.generate.generate.service.GenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/code/generate")
@RequiredArgsConstructor
public class GenerateController {

    private final GenerateService service;

    @RequestMapping(value = "preview", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, Object>>> preview(@RequestBody Map<String, Object> params) throws Exception {
        return new ResponseEntity(service.preview(params), HttpStatus.OK);
    }


    @RequestMapping(value = "download", method = RequestMethod.POST)
    public ResponseEntity<Object> download(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            File file = new File(service.download(params));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            FileUtil.downloadFile(request, response, new File(zipPath), true);
        } catch (IOException e) {
            throw new MessageRuntimeException("打包失败");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\zhangfujiang\\AppData\\Local\\Temp\\papla-gen-temp\\d9bb992f-8b3d-4388-8899-b3efb128de25");
        String zipPath = file.getPath() + ".zip";
        ZipUtil.zip(file.getPath(), zipPath);
    }

}
