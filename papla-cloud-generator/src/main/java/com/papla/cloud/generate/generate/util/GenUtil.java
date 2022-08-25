package com.papla.cloud.generate.generate.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.papla.cloud.common.web.utils.FileUtil;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateException;
import cn.hutool.extra.template.TemplateUtil;
import com.papla.cloud.generate.generate.GenerateConfig;

@SuppressWarnings({"unchecked", "all"})
public class GenUtil {

    /**
     * @param config
     * @return void
     * @throws Exception
     * @Title generatorCode
     * @Description TODO 生成代码到文件
     */
    public static void generatorCode(GenerateConfig config) throws Exception {
        TemplateEngine engine = getTemplate(config);
        String filePath = config.getOutFilePath() + config.getOutFileName();
        assert filePath != null;
        File file = new File(filePath);
        // 如果覆盖获取文件不存在则向文件中写入
        if ("Y".equals(config.getCover()) || !FileUtil.exist(file)) {
            genFile(file, engine.getTemplate(config.getTemplate()), config.getGenerateData());
        }
    }

    /**
     * @param config
     * @return void
     * @throws Exception
     * @Title generatorCode
     * @Description TODO 生成
     */
    public static String renderCode(GenerateConfig config) throws Exception {
        TemplateEngine engine = getTemplate(config);
        return engine.getTemplate(config.getTemplate()).render(config.getGenerateData());
    }

    /**
     * @param config
     * @return TemplateEngine
     * @Title getTemplate
     * @Description TODO 根据【生成配置】获取【模板引擎】
     */
    public static TemplateEngine getTemplate(GenerateConfig config) {
        TemplateEngine engine = null;
        if ("CLASSPATH".equals(config.getSrcMode())) {
            engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        } else{
            engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.STRING));
        }
        return engine;
    }


    /**
     * @param file
     * @param template
     * @param map
     * @return void
     * @throws IOException
     * @Title genFile
     * @Description TODO 生成目标文件
     */
    private static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        Writer writer = null;
        try {
            FileUtil.touch(file);
            writer = new FileWriter(file);
            template.render(map, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert writer != null;
            writer.close();
        }
    }


}
