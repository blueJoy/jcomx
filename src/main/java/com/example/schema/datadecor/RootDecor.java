package com.example.schema.datadecor;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;

/**
 *  虚拟跟处理器
 *      目的：通过跟找到需要执行的具体子decor
 * Created by baixiangzhu on 2017/7/31.
 */
public class RootDecor extends AbstractDecor {
    public RootDecor(Config config) {
        super(config);
    }

    @Override
    public void doDecorate(Object data, Context context) throws ConfigException {

        System.out.println(this.getClass().getName()+ "--->执行啦");
    }

    @Override
    public String getType() {
        return null;
    }
}
