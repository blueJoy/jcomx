package com.example.schema;

import com.example.config.Config;
import com.example.exceptions.UnknownDecorTypeException;
import com.example.schema.datadecor.*;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public class DecorFactory {

    private static final String EACH = "each";
    private static final String ROOT = "root";
    private static final String FIXED = "fixed";
    private static final String SCRIPT = "script";


    public static AbstractDecor create(Config config) throws UnknownDecorTypeException {
        return create(config,null);
    }

    public static AbstractDecor create(Config config, String forceType) throws UnknownDecorTypeException {

        String type;

        if(forceType!= null && !forceType.isEmpty()){
            type = forceType;
        }else {
            type =  config.str(AbstractDecor.FIELD_TYPE,AbstractDecor.TYPE_EACH);;
        }

        switch (type.toLowerCase()){
            case EACH: return new EachDecor(config);
            case ROOT: return new RootDecor(config);
            case FIXED: return new FixedDecor(config);
            case SCRIPT: return new ScriptDecor(config);

            default: throw new UnknownDecorTypeException(type);
        }

    }
}
