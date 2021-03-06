package com.example.schema.datadecor;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.schema.source.Source;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  处理列表的数据节点
 * Created by baixiangzhu on 2017/7/28.
 */
@Slf4j
public class EachDecor extends AbstractDecor {

    private static final String FIELD_SOURCE = "source";

    protected String field;
    protected Source source;

    public EachDecor(Config config) {
        super(config);
    }

    @Override
    public void doDecorate(Object data, Context context) throws ConfigException {

        log.info("EachDecor doDecoreate execute.. config=[{}],data=[{}]",config,data);

        //获取要处理的节点
        List matchedNodes = super.getMatchedNodes(data, context);

        source = new Source(config.sub(FIELD_SOURCE));
        field = config.str(FIELD_FIELD,"");

        //循环处理匹配上的每个节点
        for (Object ref : matchedNodes){
            this.decorateOneNode(ref,data,context);
        }

    }

    @Override
    public String getType() {
        return AbstractDecor.TYPE_EACH;
    }


    //调用Source loadData 获取数据
    private void decorateOneNode(Object ref, Object data, Context context) {

        HashMap<String,Object> vars = Maps.newHashMap();
        vars.put("ref",ref);
        vars.put("data",data);

        Object loadedData = source.loadData(context, vars);

        if(loadedData == null) {

            log.info("load data is null...");
            return;
        }

        Map reData = Maps.newHashMap();

        if(!this.field.isEmpty()){
            reData.put(this.field,loadedData);
        }else {
            reData = (Map) loadedData;
        }
        if(ref instanceof Map){
            ((Map) ref).putAll(reData);
        }

    }


}
