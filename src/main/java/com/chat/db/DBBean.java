package com.chat.db;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBBean {

    private static DBBean instance;

    @Value("${ctap.driver}")
    private String driver;
    @Value("${ctap.url}")
    private String dburl;
    @Value("${ctap.unm}")
    private String dbunm;
    @Value("${ctap.pwd}")
    private String dbpwd;
    @Value("${sensitive.info.log}")
    private String sen_info;

    @PostConstruct
    private void init() {
        instance = this;  // assign current bean to static instance
    }

    public static String getDriver() {
        return instance.driver;
    }

    public static String getDburl() {
        return instance.dburl;
    }

    public static String getDbunm() {
        return instance.dbunm;
    }

    public static String getDbpwd() {
        return instance.dbpwd;
    }

    public static String getSen_info() {
        return instance.sen_info;
    }
}
