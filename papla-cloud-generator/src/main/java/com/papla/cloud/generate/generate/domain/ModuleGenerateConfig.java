package com.papla.cloud.generate.generate.domain;

import java.io.File;
import java.util.regex.Matcher;

import com.google.common.base.CaseFormat;

import com.papla.cloud.common.web.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModuleGenerateConfig {

    /**
     * 资源模式
     */
    public String srcMode;

    /**
     * 关联模板ID
     */
    private String tableTempId;

    /**
     * 模板文件ID
     */
    private String tempFileId;

    /**
     * 关联表ID
     */
    private String tableId;

    /**
     * 接口描述
     */
    private String apiAlias;

    /**
     * 关联表名
     */
    private String tableName;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * JAVA包名称
     */
    private String packageName;

    /**
     * 作者
     */
    private String author;

    /**
     * 模板分类
     */
    private String tempCate;

    /**
     * 是否覆盖
     */
    private String cover;

    /**
     * 是否生成接口
     */
    private String ganApi;

    /**
     * 是否生成功能菜单
     */
    private String genMenu;

    /**
     * 模板文件名称
     */
    private String tempFileName;

    /**
     * 模板内容
     */
    private String tempContent;

    /**
     * 是否必须生成
     */
    private String required;

    /**
     * JAVA工程位置
     */
    private String projectPath;

    /**
     * WEB页面位置
     */
    private String pagePath;

    /**
     * 模块路径
     */
    private String modulePath;

    /**
     * 模板路径
     */
    private String tempFilePath;

    /**
     * 资源路径
     */
    private String genSrcPath;

    /**
     * 文件前缀
     */
    private String genFilePrefix;

    /**
     * 文件后缀
     */
    private String genFileSuffix;

    /**
     * 生成文件路径
     */
    private String genFilePath;

    /**
     * 生成文件格式
     */
    private String genFileFormat;

    /**
     * @return String
     * @Title getTemplate
     * @Description TODO 获取模板
     */
    public String getTemplate() {
        if("CLASSPATH".equals(getSrcMode())){
            return  this.tempFilePath.replaceAll("\\\\", "/") + "/" +tempFileName + ".ftl";
        }else{
            return this.tempContent;
        }
    }


    /**
     * @return String
     * @Title getOutFilePath
     * @Description TODO 获取输出路径
     */
    public String getOutFilePath() {

        StringBuffer buffer = new StringBuffer("");

        if ("SERVICE".equals(getTempCate())) {
            // 如果模板为【服务器模板】 输出路径为 [projectPath]+[genSrcPath]+[packageName]+[modulePath]+[genFilePath]
            // 如果模板为【服务器模板】 输出路径为 [项目路径]+[资源路径]+[项目包名]+[模块路径]+[生成路径]
            if (projectPath != null) {
                buffer.append(projectPath.replaceAll("\\\\", "/")).append("/");
            }
            if (genSrcPath != null) {
                buffer.append(genSrcPath.replaceAll("\\\\", "/")).append("/");
            }
            if (packageName != null) {
                buffer.append(packageName.replaceAll("\\.", "/")).append("/");
            }
            if (genFilePath != null) {
                buffer.append(genFilePath.replaceAll("\\\\", "/")).append("/");
            }

        } else if("PAGE".equals(getTempCate())){

            // 否则输出路径为  [pagePath]+[genSrcPath]+[modulePath]+[ClassName]+[genFilePath]
            //               [前端路径]+[资源路径]+[模块路径]+[实体类名]+[生成路径]
            if (pagePath != null) {
                buffer.append(pagePath.replaceAll("\\\\", "/")).append("/");
            }
            if (genSrcPath != null) {
                buffer.append(genSrcPath.replaceAll("\\\\", "/")).append("/");
            }
            if (modulePath != null) {
                buffer.append(modulePath.replaceAll("\\\\", "/")).append("/");
            }
            buffer.append(getClassName()).append("/");
            if (genFilePath != null) {
                buffer.append(genFilePath.replaceAll("\\\\", "/")).append("/");
            }

        }else{

            // 否则输出路径为 [前端路径]+[资源路径]+[模块路径]+[生成路径]
            if (pagePath != null) {
                buffer.append(pagePath.replaceAll("\\\\", "/")).append("/");
            }
            if (genSrcPath != null) {
                buffer.append(genSrcPath.replaceAll("\\\\", "/")).append("/");
            }
            if (modulePath != null) {
                buffer.append(modulePath.replaceAll("\\\\", "/")).append("/");
            }
            if (genFilePath != null) {
                buffer.append(genFilePath.replaceAll("\\\\", "/")).append("/");
            }
        }
        return buffer.toString().replaceAll("(/)+", "/").replaceAll("/", Matcher.quoteReplacement(File.separator));
    }

    /**
     * @return String
     * @Title getOutFileName
     * @Description TODO 获取输出文件名
     */
    public String getOutFileName() {
        if ("SERVICE".equals(getTempCate())) {
            return this.getUpperFileName() + "." + this.genFileFormat;
        } else {
            return this.getHyphenFileName() + "." + this.genFileFormat;
        }
    }

    /**
     * @return String
     * @Title getUpperFileName
     * @Description TODO 获取类名
     */
    private String getUpperFileName() {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, getHyphenFileName());
    }

    /**
     * @return String
     * @Title getHyphenFileName
     * @Description TODO 类名转【-】
     */
    private String getHyphenFileName() {
        String className = genFilePrefix == null ? "" : genFilePrefix;

        className = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, className);
        className = className + "-" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, entityName);
        className = className + "-" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, genFileSuffix == null ? "" : genFileSuffix);

        className = className.replaceAll("(-)+", "-");
        if (className.indexOf("-") == 0) {
            className = className.substring(1);
        }
        if (className.endsWith("-")) {
            className = className.substring(0, className.length() - 1);
        }
        return className;
    }

    /**
     * @return String
     * @Title getClassName
     * @Description TODO 实体类名
     */
    public String getClassName() {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, getHyphenClassName());
    }

    /**
     * @return String
     * @Title getLowerEntityClassName
     * @Description TODO 实体类名首字母小写
     */
    public String getLowerClassName() {
        return StringUtils.uncapitalize(getClassName());
    }

    /**
     * @return String
     * @Title getHyphenClassName
     * @Description TODO 实体类名转【-】
     */
    public String getHyphenClassName() {
        String className = entityName == null ? "" : entityName;

        className = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, entityName);

        className = className.replaceAll("(-)+", "-");
        if (className.indexOf("-") == 0) {
            className = className.substring(1);
        }
        if (className.endsWith("-")) {
            className = className.substring(0, className.length() - 1);
        }
        return className;
    }

}
