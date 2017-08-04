package com.example.context;

import com.example.http.RequestMessage;
import com.example.http.ResponseMessage;
import com.example.interfaces.IBuilder;
import com.example.schema.Schema;


/**
 * 执行上下文  Builder模式
 * Created by baixiangzhu on 2017/7/27.
 */
public class Context {

    private final RequestMessage request;

    private final ResponseMessage response;

    /**
     * 日志追踪ID
     */
    private final String traceId;

    private final Schema schema;


    private Context(Builder builder){
        this.request = builder.request;
        this.response = builder.response;
        this.traceId = builder.traceId;
        this.schema = builder.schema;
    }


    public RequestMessage getRequest() {
        return request;
    }

    public ResponseMessage getResponse() {
        return response;
    }

    public String getTraceId() {
        return traceId;
    }

    public Schema getSchema() {
        return schema;
    }

    public static class Builder implements IBuilder<Context>{

        private  RequestMessage request;

        private  ResponseMessage response;

        private String traceId;

        private Schema schema;

        public Builder request(RequestMessage request){
            this.request = request;
            return this;
        }

        public Builder response(ResponseMessage response){
            this.response = response;
            return this;
        }


        public Builder traceId(String traceId){
            this.traceId = traceId;
            return this;
        }

        public Builder schema(Schema schema){
            this.schema = schema;
            return this;
        }

        @Override
        public Context build() {
            return new Context(this);
        }
    }




}
