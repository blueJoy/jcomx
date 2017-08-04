package com.example.schema.source;

import com.example.config.Config;
import com.example.exceptions.ConfigException;
import com.example.exceptions.UnknownSourceBaseTypeException;
import com.example.schema.source.sourcebase.AbstractSourceBase;
import com.example.schema.source.sourcebase.HttpSourceBase;

import java.util.HashMap;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public class SourceBaseFactory {

    private static final String DEFAULT_BASE_ID = "default";

    private static final String FIELD_SOURCE_BASES      = "sourceBases";
    private static final String FIELD_ID                = "id";

    private static final String FIELD_TYPE              = "type";
    private static final String TYPE_HTTP               = "http";



    private static final String DEFAULT_TYPE            = "http";


    private static HashMap<String,AbstractSourceBase> sourcePool = new HashMap<>();


    /**
     * 根据配置文件生产SourceFactory
     * @param config
     * @return
     */
    public static SourceBaseFactory fromConf(Config config) throws ConfigException {

        SourceBaseFactory factory = new SourceBaseFactory();

        Config bases = config.sub(FIELD_SOURCE_BASES);

        for (String key : bases.keys()){

            Config subConf = bases.sub(key);
            factory.putSourceBase(populateBaseObject(subConf));

        }



        return factory;
    }

    private static AbstractSourceBase populateBaseObject(Config config) {

        String type = config.str(FIELD_TYPE,DEFAULT_TYPE);

        switch (type){
            case TYPE_HTTP: return new HttpSourceBase(config);
        }

        return null;
    }


    private void putSourceBase(AbstractSourceBase sourceBase) throws ConfigException {
        sourcePool.put(sourceBase.getId(),sourceBase);
    }


    public static AbstractSourceBase create(String baseId) throws UnknownSourceBaseTypeException {
        if(sourcePool.containsKey(baseId)) return sourcePool.get(baseId);

        throw new UnknownSourceBaseTypeException(baseId);
    }
}
