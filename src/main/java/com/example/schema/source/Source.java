package com.example.schema.source;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.exceptions.SourceException;
import com.example.exceptions.UnknownSourceBaseTypeException;
import com.example.schema.TinyTemplate;
import com.example.schema.source.sourcebase.AbstractSourceBase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 *
 * source 是一个对应的source 实例， 提供loadData 返回 Map Object;(或非Map)
 * 它将使用 sourcebase 资源以及 sourceCache localCache 缓存
 * 同一个source 应对refJsonPath 不同情况下拥有不同运行结果，所以不能是object 属性
 * 不拥有变量 reservedVariables
 * renderedUri 会随着 reservedVariables 变化而不断变化
 * 一个source 是一个最小单位，所以可以直接在variables 添加renderedUri
 *
 *
 * Created by baixiangzhu on 2017/7/28.
 */
@Slf4j
public class Source {

    public static final String FIELD_ON_METHOD = "method";
    public static final  String FIELD_TIMEOUT = "timeout";
    public static final  String DEFAULT_BASE = "default";
    public static final String RESERVED_RENDERED_URI = "reserved_rendered_uri";



    private static final Integer DEFAULT_TIMEOUT = 8000;
    private static final String FIELD_BASE = "base";
    private static final String FIELD_URI = "uri";
    private static final String FIELD_ON_ERROR = "onError";



    private static final String RESERVED_TPL_VAR_REQUEST    = "request";


    private Config config;

    public Source(Config config){
        this.config = config;
    }

    public Object loadData(Context context, HashMap<String,Object> reservedVariables){

        long startTime = System.currentTimeMillis();
        Object result = null;

        try {
            result = this.doLoadData(context, reservedVariables);

            long spendTime = System.currentTimeMillis() - startTime;

            log.info("load data expend [{}] ms...",spendTime);

            return result;
        } catch (ConfigException e) {
           log.error("");
        }

        return null;
    }

    private Object doLoadData(Context context, HashMap<String, Object> reservedVariables) throws ConfigException {

        try {

            String uri = config.str(Source.FIELD_URI);

            TinyTemplate tinyTemplate = new TinyTemplate(uri);

            reservedVariables.put(RESERVED_TPL_VAR_REQUEST,context.getRequest());

            String renderedUri = tinyTemplate.render(reservedVariables, context);

            //TODO:何作用？
            reservedVariables.put(RESERVED_RENDERED_URI, renderedUri);

            //根据不同的类型，使用不同的sourceBase具体执行
            Object data = this.getBase(context).executeLoading(context, config, reservedVariables);

            return data;
        } catch (SourceException e) {
            e.printStackTrace();
        } catch (UnknownSourceBaseTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private AbstractSourceBase getBase(Context context) throws UnknownSourceBaseTypeException {

        String baseId = config.str(Source.FIELD_BASE, Source.DEFAULT_BASE);

        return SourceBaseFactory.create(baseId);
    }

}
