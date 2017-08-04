package com.example.config;

import com.example.exceptions.ConfigException;
import com.example.schema.Loader;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by baixiangzhu on 2017/8/4.
 */
public class ConfigTest {

    @Test
    public void testLoader(){

        String path = "config/comx.conf.json";

        try {

            Config config = Loader.fromJsonFile(path);
            Assert.assertTrue(!!!config.getData().isEmpty());
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStr(){



    }

}
