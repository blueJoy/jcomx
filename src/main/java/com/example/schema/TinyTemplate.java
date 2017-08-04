package com.example.schema;

import com.example.context.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public class TinyTemplate {

    private static final String REGEX = "\\{(.*?)\\}";

    private static final Pattern pattern = Pattern.compile(REGEX);

    private String tpl;

    private HashMap<String,Object>  vars;

    public TinyTemplate(String tpl) {
        this.tpl = tpl;
    }

    /**
     * 根据正则表达式替换占位符
     * @param vars
     * @param context
     * @return
     */
    public String render(HashMap<String,Object> vars, Context context){

        this.vars = vars;
        Matcher matcher = pattern.matcher(tpl);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()){
            MatchResult matchResult = matcher.toMatchResult();
            //匹配{}
            String oldTpl = tpl.substring(matchResult.start(1), matcher.end(1));
            String newTpl = replace2(oldTpl,context);
            matcher.appendReplacement(sb,newTpl);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    //TODO：可考虑使用反射？
    private String replace(String matched, Context context) {

        String[] varSections = matched.split("\\.");
        Object matchedValue = this.vars;

        for (String key:varSections){
            if(matchedValue instanceof Map){
                matchedValue = ((Map)matchedValue).get(key);
            }else{
                //TODO:ERROR记录日志
                return "";
            }
        }

        return matchedValue.toString();
    }

    private String replace2(String matched, Context context) {

        String[] varSections = matched.split("\\.");
        Object matchedValue = this.vars;

        for (String key:varSections){
            try {
                Method get = matchedValue.getClass().getMethod("get",Object.class);

                matchedValue = get.invoke(matchedValue, key);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

      /*      if(matchedValue instanceof Map){
                matchedValue = ((Map)matchedValue).get(key);
            }else{
                //TODO:ERROR记录日志
                return "";
            }*/
        }

        return matchedValue.toString();
    }


}
