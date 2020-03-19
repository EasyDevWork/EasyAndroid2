package com.easy.framework.Http.exception;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.text.ParseException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

public class ExceptionEngine {

    public static final int UN_KNOWN_ERROR = 1000;//未知错误
    public static final int ANALYTIC_SERVER_DATA_ERROR = 1001;//解析(服务器)数据错误
    public static final int CONNECT_ERROR = 1003;//网络连接错误
    public static final int TIME_OUT_ERROR = 1004;//网络连接超时
    public static final int UNKNOWN_HOST = 1005;//域名错误
    public static final int SSL_HAND_SHAKE = 1007;//服务器证书错误
    public static final int UNKNOWN_SERVICE = 1008;//未知服务器


    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof ApiException) {
            ex = (ApiException) e;
            return ex;
        } else if (e instanceof HttpException) {//HTTP错误
            HttpException httpExc = (HttpException) e;
            ex = new ApiException(httpExc.code(), e.getMessage(), e);
            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误(交由开发者自己处理)
            ServerException serverExc = (ServerException) e;
            ex = new ApiException(serverExc.getCode(), serverExc.getMsg(), serverExc);
            return ex;
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException || e instanceof MalformedJsonException) {  //解析数据错误
            ex = new ApiException(ANALYTIC_SERVER_DATA_ERROR, e.getMessage(), e);
            return ex;
        } else if (e instanceof ConnectException) {//连接网络错误
            ex = new ApiException(CONNECT_ERROR, e.getMessage(), e);
            return ex;
        } else if (e instanceof SSLHandshakeException) {//服务器证书错误
            ex = new ApiException(SSL_HAND_SHAKE, e.getMessage(), e);
            return ex;
        } else if (e instanceof UnknownHostException) {//域名错误
            ex = new ApiException(UNKNOWN_HOST, e.getMessage(), e);
            return ex;
        } else if (e instanceof SocketTimeoutException) {//网络超时
            ex = new ApiException(TIME_OUT_ERROR, e.getMessage(), e);
            return ex;
        } else if(e instanceof UnknownServiceException){
            ex = new ApiException(UNKNOWN_SERVICE, e.getMessage(), e);
            return ex;
        }else { //未知错误
            ex = new ApiException(UN_KNOWN_ERROR, "未知错误：" + e.getMessage(), e);
            return ex;
        }
    }

}
