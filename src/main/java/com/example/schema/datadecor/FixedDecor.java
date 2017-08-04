package com.example.schema.datadecor;

import com.alibaba.fastjson.JSONObject;
import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;

import java.util.Map;

/**
 * 处理固定值的数据节点
 *      例如： fixedData:{id:3,name:test}  field:hello
 *
 *      hello:{id:3,name:test}
 *
 * Created by baixiangzhu on 2017/8/1.
 */
public class FixedDecor extends AbstractDecor {

    private static final String FIELD_FIXED_DATA = "fixedData";


    public FixedDecor(Config config) {
        super(config);
    }

    @Override
    public void doDecorate(Object data, Context context) throws ConfigException {

        JSONObject loaded;
        String field = super.config.str(FIELD_FIELD, "");
        JSONObject fixedData = super.config.sub(FIELD_FIXED_DATA).getData();
 /*       if(field.isEmpty()){
            loaded = fixedData;
        }else {
            loaded = new JSONObject();
            loaded.put(field,fixedData);
        }*/

        if(data instanceof Map){
            if(field.isEmpty()){
                ((Map) data).putAll(fixedData);
            }else {
                ((Map) data).put(field,fixedData);
            }
        }
    }

    @Override
    public String getType() {
        return null;
    }
}
