package com.example.schema;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.exceptions.PreConditionException;
import com.example.exceptions.UnknownDecorTypeException;
import com.example.schema.datadecor.ScriptDecor;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by baixiangzhu on 2017/8/9.
 */
@Slf4j
public class PreCondition {

    private static final String DEFAULT_PRECONDITION_TYPE = "Script";

    private Config config;

    public PreCondition(Config config) throws PreConditionException {
        if(config == null || config.getData().isEmpty()){
            throw new PreConditionException("");
        }
        this.config = config;
    }

    public boolean doExecute(Context context,Object data) throws Exception {

        log.info("PreCondition doExecute execute .. config=[{}],data=[{}]",config,data);

        ScriptDecor scriptDecor = (ScriptDecor) DecorFactory.create(this.config, DEFAULT_PRECONDITION_TYPE);

        return (boolean) scriptDecor.doCallbackResult(context,data);
    }

}
