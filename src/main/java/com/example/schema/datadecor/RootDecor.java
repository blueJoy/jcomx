package com.example.schema.datadecor;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import lombok.extern.slf4j.Slf4j;

/**
 *  虚拟跟处理器【相当于一个跟节点】
 *      目的：通过跟找到需要执行的具体子decor
 * Created by baixiangzhu on 2017/7/31.
 */
@Slf4j
public class RootDecor extends AbstractDecor {
    public RootDecor(Config config) {
        super(config);
    }

    @Override
    public void doDecorate(Object data, Context context) throws ConfigException {

        log.info("ScriptDecor doDecoreate execute .. config=[{}],data=[{}]",config,data);
    }

    @Override
    public String getType() {
        return null;
    }
}
