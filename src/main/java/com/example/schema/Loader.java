package com.example.schema;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.config.Config;
import com.example.exceptions.ConfigException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 真正加载配置文件
 * Created by baixiangzhu on 2017/7/27.
 */
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
            //ex.printStackTrace();
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
            //ex.printStackTrace();
            throw new ConfigException("bad format json");
        }
    }


}
