package com.example.entity;


import java.util.List;

/**
 * 资源配置信息
 * Created by baixiangzhu on 2017/7/27.
 */
public class ComxConfig {

    private String urlPrefix;
    //是否开启请求返回debug信息
    private Boolean debug;
    //其他资源信息
    private List<SourceBase> sourceBases;


    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public List<SourceBase> getSourceBases() {
        return sourceBases;
    }

    public void setSourceBases(List<SourceBase> sourceBases) {
        this.sourceBases = sourceBases;
    }

    static class SourceBase {

        /**
         * 资源名称
         */
        private String id;
        /**
         * 访问地址前缀
         */
        private String urlPrefix;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrlPrefix() {
            return urlPrefix;
        }

        public void setUrlPrefix(String urlPrefix) {
            this.urlPrefix = urlPrefix;
        }
    }

}
