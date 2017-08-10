package com.example.schema.datadecor;

import com.example.config.Config;
import com.example.constant.Constants;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.schema.GroovyScriptFactory;
import com.google.common.collect.Maps;
import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;

/**
 * 用于执行groovy脚本，做一些逻辑控制
 * Created by baixiangzhu on 2017/8/2.
 */
@Slf4j
public class ScriptDecor extends AbstractDecor{

    // script脚本目录
    private static final String J_SCRIPT = "jscript";
    // lambda 表达式
    private static final String J_LAMBDA = "jlambda";

    private static final String METHOD = "method";

    private static final String GROOVY_SUFFIX = ".groovy";

    //groovy脚本默认执行方法
    private static final String DEFAULT_GROOVY_METHOD = "callback";

    private static final String PARAM_CONTEXT = "context";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_REF = "ref";



    public ScriptDecor(Config config) {
        super(config);
    }

    @Override
    public void doDecorate(Object data, Context context) throws Exception {

        log.info("ScriptDecor doDecoreate execute .. config=[{}],data=[{}]",config,data);

        String scriptName = null;

        String lambdaStr = null;

        try {

            ArrayList matchedNodes = super.getMatchedNodes(data, context);

            scriptName = super.config.str(J_SCRIPT, "");

            if(!scriptName.isEmpty()){
                //根据groovy文件生产groovy对象
                GroovyObject groovyObject = GroovyScriptFactory.loadScriptByName(scriptName);

                //循环执行脚本
                matchedNodes.forEach( ref -> executeGroovyScript(groovyObject,data,context,ref));

                return;
            }

            //执行lambda表达式

            String lambda = super.config.str(J_LAMBDA,"");

            //纯粹为了打印错误日志用的
            lambdaStr = lambda;

            //循环执行lambda
            matchedNodes.forEach(ref -> executeLambda(lambda,data,context,ref));


        }catch (Exception e){
            log.error("execute script error!scriptName=[{}],lambda=[{}]",scriptName,lambdaStr,e);
            throw new Exception(e);
        }
    }

    /**
     * 执行脚本获取返回值
     *      目前：主要用在precondition中
     * @param context
     * @param data
     */
    public Object doCallbackResult(Context context, Object data) throws Exception {

        String scriptName = null;
        String lambda = null;

        try{

            scriptName = super.config.str(J_SCRIPT, "");

            if(!scriptName.isEmpty()){
                //根据groovy文件生产groovy对象
                GroovyObject groovyObject = GroovyScriptFactory.loadScriptByName(scriptName);

                //循环执行脚本
                return executeGroovyScript(groovyObject,data,context,null);

            }

            //执行lambda表达式
            lambda = super.config.str(J_LAMBDA,"");

            //循环执行lambda
            return executeLambda(lambda,data,context,null);

        }catch (Exception e){
            log.error("execute script error!scriptName=[{}],lambda=[{}]",scriptName,lambda,e);
            throw new Exception(e);
        }

    }


    private Object executeLambda(String lambda, Object data, Context context,Object ref) {

       return getGroovyShell(getVariables(context,data,ref)).evaluate(lambda);
    }

    private Map<String,Object> getVariables(Context context, Object data, Object ref){
        Map<String,Object> variables = Maps.newHashMap();
        variables.put(PARAM_CONTEXT,context);
        variables.put(PARAM_DATA,data);
        variables.put(PARAM_REF,ref);

        return variables;
    }


    //获取脚本执行器
    private GroovyShell getGroovyShell(Map<String, Object> variables){
        return  new GroovyShell( new Binding(variables));
    }

    /**
     * 执行groovy脚本的callback方法
     *
     * @param groovyObject
     * @param data
     * @param context
     * @throws ResourceException
     * @throws ScriptException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Object executeGroovyScript(GroovyObject groovyObject, Object data, Context context,Object ref){
        //反射执行方法方法
        return groovyObject.invokeMethod(DEFAULT_GROOVY_METHOD,new Object[]{context,data,ref});
    }

    @Override
    public String getType() {
        return AbstractDecor.TYPE_SCRIPT;
    }
}
