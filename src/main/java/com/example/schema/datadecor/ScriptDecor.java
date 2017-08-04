package com.example.schema.datadecor;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.schema.GroovyScriptFactory;
import com.google.common.collect.Maps;
import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.util.ArrayList;
import java.util.Map;

/**
 * 用于执行groovy脚本，做一些逻辑控制
 * Created by baixiangzhu on 2017/8/2.
 */
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
    public void doDecorate(Object data, Context context) throws ConfigException {

        try {

            ArrayList matchedNodes = super.getMatchedNodes(data, context);

            String scriptName = super.config.str(J_SCRIPT, "");
            String lambda = super.config.str(J_LAMBDA,"");

            if(!scriptName.isEmpty()){
                executeGroovyScript(scriptName,matchedNodes,data,context);
            }else if(!lambda.isEmpty()){
                executeLambda(lambda,matchedNodes,data,context);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * 执行lambda表达式
     * @param lambda
     * @param matchedNodes
     * @param data
     * @param context
     */
    private void executeLambda(String lambda, ArrayList matchedNodes, Object data, Context context) {

        matchedNodes.forEach(ref -> getGroovyShell(getVariables(context,data,ref)).evaluate(lambda));
    }

    private Map<String,Object> getVariables(Context context, Object data, Object ref){
        Map<String,Object> variables = Maps.newHashMap();
        variables.put(PARAM_CONTEXT,context);
        variables.put(PARAM_DATA,data);
        variables.put(PARAM_REF,ref);

        return variables;
    }


    private GroovyShell getGroovyShell(Map<String, Object> variables){
        return  new GroovyShell( new Binding(variables));
    }

    /**
     * 执行groovy脚本的callback方法
     * @param scriptName
     * @param matchedNodes
     * @param data
     * @param context
     * @throws ResourceException
     * @throws ScriptException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void executeGroovyScript(String scriptName, ArrayList matchedNodes, Object data, Context context)
            throws ResourceException, ScriptException, IllegalAccessException, InstantiationException {

        //根据groovy文件生产groovy对象
        GroovyObject groovyObject = GroovyScriptFactory.loadScriptByName(scriptName);

        //反射执行方法方法
        matchedNodes.forEach( ref -> groovyObject.invokeMethod(DEFAULT_GROOVY_METHOD,new Object[]{context,data,ref}));

    }

    @Override
    public String getType() {
        return AbstractDecor.TYPE_SCRIPT;
    }
}
