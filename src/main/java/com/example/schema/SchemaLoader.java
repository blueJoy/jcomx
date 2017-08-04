package com.example.schema;

import com.example.config.ComxConfigLoader;
import com.example.config.Config;
import com.example.constant.Constants;
import com.example.exceptions.ConfigException;
import com.google.common.collect.Maps;
import com.sun.deploy.net.proxy.pac.PACFunctions;

import java.util.Map;

/**
 * 加载业务配置信息
 *      如：get.json/post.json等
 * Created by baixiangzhu on 2017/7/27.
 */
public class SchemaLoader {

    private static final String CONF_FILE_SUFFIX = ".json";

    private static final String CONF_PATH_SECTION = Constants.DIRECTORY_SEPARATOR + "apis";

    /**
     * 缓存加载的配置文件
     */
    protected static Map<String,Config> shecmaPool = Maps.newHashMap();


    private String apiConfigPath;


    public static Schema load(String path, String method) throws ConfigException {
        String filePath = ComxConfigLoader.getComxHome() + CONF_PATH_SECTION + path;

        Config config = Loader.fromJsonFile(filePath + Constants.DIRECTORY_SEPARATOR + method + CONF_FILE_SUFFIX);

        return new Schema(config);
    }


}
