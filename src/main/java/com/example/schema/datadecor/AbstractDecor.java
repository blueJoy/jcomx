package com.example.schema.datadecor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.exceptions.PreConditionException;
import com.example.exceptions.UnknownDecorTypeException;
import com.example.schema.ConfBaseNode;
import com.example.schema.DecorFactory;
import com.example.schema.PreCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public abstract class AbstractDecor extends ConfBaseNode{

    public static final String FIELD_DECORS = "decors";
    public static final String FIELD_CACHE = "cache";
    public static final String FIELD_TYPE = "type";


    protected static final String FIELD_REF_JSON_PATH = "refJsonPath";
    protected static final String FIELD_FIELD = "field";
    protected static final String FIELD_PRECONDITION  = "precondition";





    public static final String TYPE_EACH = "Each";
    public static final String TYPE_ROOT = "Root";
    public static final String TYPE_SCRIPT = "Script";



    public AbstractDecor(Config config) {
        super(config);
    }
    
    public void decorate(Object data,Context context){
        
        try{

            //前置条件检验不过直接返回
            if(!executePreconditionNode(data,context)) return;

            //执行主要接口
            this.doDecorate(data,context);

            //执行子decors，拼接接口
            this.executeChildDecors(data,context);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void executeChildDecors(Object data, Context context) throws ConfigException, UnknownDecorTypeException {
        this.sequentialExecuteChildDecors(data,context);
    }

    protected void sequentialExecuteChildDecors(Object data, Context context) throws ConfigException, UnknownDecorTypeException {
        Config children = super.config.sub(FIELD_DECORS);
        Set<String> keys = children.keys();
        //有几个子的decors，执行几次逻辑
        for (String key : keys){
            AbstractDecor decor =  DecorFactory.create(children.sub(key));
            decor.decorate(data,context);
        }
    }

    /**
     * 匹配要处理的数据节点
     * @param data
     * @param context
     * @return
     * @throws ConfigException
     */
    protected ArrayList getMatchedNodes(Object data, Context context) throws ConfigException {

        String refJsonPath = this.config.str(FIELD_REF_JSON_PATH,null);

        if(refJsonPath == null){
            return new ArrayList(Arrays.asList(data));
        }

        try{
            //根据$.data.name  的表达式，找到对接的节点数据
            Object matchedNode = JSONPath.eval(data, refJsonPath);
            if(matchedNode instanceof JSONArray){
                return new ArrayList((JSONArray)matchedNode);
            }else{
                return new ArrayList(Arrays.asList(matchedNode));
            }
        }catch (Exception e){
            System.out.println("refJsonPath is error!");
            return new ArrayList(Arrays.asList(data));
        }

    }

    /**
     * 校验前置条件
     *      precondition 配置的lambda表达式
     * @param data
     * @param context
     * @return
     */
    private boolean executePreconditionNode(Object data, Context context) throws Exception {

        Config preConditionConf = super.config.sub(FIELD_PRECONDITION);

        if(preConditionConf == null || preConditionConf.getData().isEmpty()) return true;

        try {
            PreCondition preCondition = new PreCondition(preConditionConf);

            return preCondition.doExecute(context,data);

        } catch (PreConditionException e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (UnknownDecorTypeException e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }


    public abstract void doDecorate(Object data, Context context) throws Exception;
    public abstract String getType();


}
