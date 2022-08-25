package com.papla.cloud.workflow.engine.common.util;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author admin
 */
public class WebserviceUtil {

    // 日志记录器
    private Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     * 调用webservice服务
     * @param url        http://localhost:8082/ns?wsdl
     * @param methodName 方法名
     * @param args       String数组
     * @return 要求对方返回的结果是一个字符串 <result><flag></flag><msg></msg><data><row></row><row></row></data></result>
     * @throws Exception
     */
    public String callWebService(String url, String methodName, Object[] args) {
        try {
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            org.apache.cxf.endpoint.Client client = dcf.createClient(url);
            Object[] objects;
            objects = client.invoke(methodName, args);

            return (String) objects[0];

        } catch (Exception e) {
            log.error(e.getMessage());
            return "<result><flag>1</flag><msg>" + e.getMessage() + "</msg><data></data></result>";
        }

    }

}
