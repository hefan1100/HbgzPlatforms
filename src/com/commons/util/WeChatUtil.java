package com.commons.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;

import java.util.HashMap;
import java.util.Map;

public class WeChatUtil {
    private static HttpClient client = null;
    //获取accesstoken
    public String getAccessToken(){
         return null;
    }
    //调用微信接口
    public static String callWeChat(String url,String param) throws Exception {
        HttpConnectionManager httpConManager = new MultiThreadedHttpConnectionManager();
        client = new HttpClient(httpConManager);
        client.getParams().setParameter("http.protocol.content-charset","UTF-8");
        String grant_type = "client_credential";
        String appid = "wxe1308bc4e7519fdf";
        String secret = "cfc963d752b5d629bab9a70879fe52bc";
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("grant_type", grant_type);
        parms.put("appid", appid);
        parms.put("secret", secret);

        com.alibaba.fastjson.JSONObject json = GetAccessToken.ifAccessToken(
                appid, secret, parms);
        String data = (String) json.get("access_token");
        PostMethod method = new PostMethod(url
                        + data);
        method.setRequestBody(param);
        int statusCode = client.executeMethod(method);
        String responseMsg ="";
        if(statusCode == 200){
            responseMsg = method.getResponseBodyAsString();
        }
        return  responseMsg;
    }
}
