package com.example.schema.source.sourcebase;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.exceptions.SourceException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public abstract class AbstractSourceBase {

    private static final String FIELD_ID = "id";

    protected Config config;

    public AbstractSourceBase(Config config){
        this.config = config;
    }

    public String getId() throws ConfigException {
        return config.str(FIELD_ID);
    }

    public abstract Object executeLoading(Context context, Config sourceOptions, HashMap<String,Object> reservedVariables) throws SourceException, ConfigException, IOException;

}
