package com.example.config;

import com.example.constant.Constants;
import com.example.exceptions.ConfigException;
import com.example.schema.GroovyScriptFactory;
import com.example.schema.Loader;
import com.example.schema.source.SourceBaseFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 加载comx.conf.json配置文件
 * Created by baixiangzhu on 2017/7/27.
 */
@Slf4j
public class ComxConfigLoader {

    private static final String COMX_HOME_KEY = "COMX_HOME";
    private static final String CONF_FILE_NAME = "comx.conf.json";


    private static Boolean initialized = false;

    private static String COMX_HOME;

    //comx.conf.json配置信息
    private static Config comxConf;

    private static String urlPrefix;


    public static  Config load() throws ConfigException {

        if(initialized)
            return comxConf;
        synchronized (initialized){
            if (initialized)
                return comxConf;
            //初始化
            initialize();
        }

        return comxConf;
    }

    private static void initialize() throws ConfigException {

        COMX_HOME = System.getProperty(COMX_HOME_KEY);
        String configPath = COMX_HOME + Constants.DIRECTORY_SEPARATOR + CONF_FILE_NAME;

        comxConf = Loader.fromJsonFile(configPath);

        //初始化ScriptFactory
        GroovyScriptFactory.init();

        initialized = true;

        log.info("load comx.conf.json complete..path=[{}]",configPath);
    }

    public static String getComxHome(){
        return COMX_HOME;
    }

}
