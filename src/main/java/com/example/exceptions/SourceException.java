package com.example.exceptions;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public class SourceException extends Exception {
    public SourceException(UrlException e) {
        super(e);
    }
}
