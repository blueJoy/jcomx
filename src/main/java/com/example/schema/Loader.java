package com.example.schema;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.config.Config;
import com.example.exceptions.ConfigException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 真正加载配置文件
 * Created by baixiangzhu on 2017/7/27.
 */
@Slf4j
public class Loader {

    /**
     * @params String fileName
     * @return Config
     * @throws ConfigException
     */
    public static Config fromJsonFile(String fileName) throws ConfigException{
        String jsonString = "";
        try {
            byte[] data = Files.readAllBytes(Paths.get(fileName));
            jsonString = new String(data);
        } catch (IOException ex) {
            log.error("failed to read json file = [{}]",fileName,ex);
            throw new ConfigException("failed to read json file:" + fileName);
        }
        return fromJson(jsonString);
    }

    /**
     * @params String jsonString
     * @return Config
     * @throw ConfigException
     */
    public static Config fromJson(String jsonString) throws ConfigException{
        try {
            JSONObject data = JSON.parseObject(jsonString);
            return new Config(data);
        }catch (JSONException ex) {
            log.error("bad format json = [{}]",jsonString,ex);
            throw new ConfigException("bad format json");
        }
    }


}
