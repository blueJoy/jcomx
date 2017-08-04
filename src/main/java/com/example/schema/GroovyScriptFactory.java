package com.example.schema;

import com.example.config.ComxConfigLoader;
import com.example.constant.Constants;
import com.example.exceptions.ConfigException;
import com.google.common.collect.Maps;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;
import java.util.Map;

/**
 * 加载groovy脚本的工厂
 * Created by baixiangzhu on 2017/8/2.
 */
public class GroovyScriptFactory {

    /**
     * 脚本存放相对地址
     */
    private static final String SCRIPT_HOME = "groovy-scripts" + Constants.DIRECTORY_SEPARATOR;

    /**
     * 缓存加载的groovy脚本
     */
    private static final Map<String,GroovyObject> scriptPool = Maps.newConcurrentMap();

    private static GroovyScriptEngine groovyScriptEngine;

    public static void init() {

        String comxHome = ComxConfigLoader.getComxHome();

        if(comxHome == null || comxHome.isEmpty()){
            try {
                ComxConfigLoader.load();
            } catch (ConfigException e) {
                e.printStackTrace();
            }
            comxHome = ComxConfigLoader.getComxHome();
        }

        String scriptHome = comxHome +Constants.DIRECTORY_SEPARATOR+ SCRIPT_HOME;

        try {
            groovyScriptEngine = new GroovyScriptEngine(scriptHome);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载script脚本
     * @param scriptName
     * @return
     * @throws ResourceException
     * @throws ScriptException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static GroovyObject loadScriptByName(String scriptName) throws ResourceException, ScriptException, IllegalAccessException, InstantiationException {

        if(scriptPool.containsKey(scriptName)) return scriptPool.get(scriptName);

        Class scriptClass = groovyScriptEngine.loadScriptByName(scriptName);
        GroovyObject groovyObject = (GroovyObject) scriptClass.newInstance();

        scriptPool.put(scriptName,groovyObject);

        return groovyObject;
    }

}
