package com.example.entity;

import java.util.List;

/**
 *
 * //TODO:情况太多，只能放弃,没有JsonObject灵活性高
 *
 * Created by baixiangzhu on 2017/7/27.
 */
public class BusinessConfig {

    /**
     * 描述信息
     */
    private Meta meta;

    private






    static class Meta{

        private String module;

        private String memo;

        private URI uri;

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }
    }



    static class URI{

        List<Parameter> parameters;

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }
    }




    static class Parameter{
        /**
         * 请求字段
         */
        private String field;
        /**
         * 描述
         */
        private String name;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}

