package com.example.context;

import com.example.http.RequestMessage;
import com.example.http.ResponseMessage;
import com.example.interfaces.IBuilder;
import com.example.schema.Schema;
import com.example.schema.source.Source;
import com.example.schema.source.SourceBaseFactory;


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

    private final SourceBaseFactory sourceBaseFactory;


    private Context(Builder builder){
        this.request = builder.request;
        this.response = builder.response;
        this.traceId = builder.traceId;
        this.schema = builder.schema;
        this.sourceBaseFactory = builder.sourceBaseFactory;
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

    public SourceBaseFactory getSourceBaseFactory() {
        return sourceBaseFactory;
    }

    public static class Builder implements IBuilder<Context>{

        private  RequestMessage request;

        private  ResponseMessage response;

        private String traceId;

        private Schema schema;

        private SourceBaseFactory sourceBaseFactory;

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

        public Builder sourceBaseFactory(SourceBaseFactory sourceBaseFactory){
            this.sourceBaseFactory = sourceBaseFactory;
            return this;
        }



        @Override
        public Context build() {
            return new Context(this);
        }
    }




}
