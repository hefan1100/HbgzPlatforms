package com.commons.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.*;
import java.util.Set;
import java.util.Map.Entry;

import java.util.Vector;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;

public class GetAccessToken {
	public static Map<String, JSONObject> AccessToken = new HashMap<String, JSONObject>();
    private static HttpClient client = null;
	
	/**
	 * 判断 AccessToken 是否存在或是否超时，不存在或超时重新获取
	 * 
	 * @param appId
	 * @param appSecret
	 * @return access_token
	 * @throws IOException
	 */
	public synchronized static JSONObject ifAccessToken(String appId,
			String appSecret, Map<String, Object> parms)
			throws Exception {
		Long time = System.currentTimeMillis(); // 当前时间戳
		// 判断 AccessToken 是否存在
		if (GetAccessToken.AccessToken == null
				|| GetAccessToken.AccessToken.get("AccessTokenJson") == null) {
			GetAccessToken.AccessToken(appId, appSecret, parms);
		}
		// 上次获取 AccessToken 的时间戳
		Long oldtime = Long.parseLong(GetAccessToken.AccessToken
				.get("AccessTokenJson").get("time").toString());
		if (time - oldtime > 100 * 1000) { // 上次获取的时间大于7200秒，重新获取
			GetAccessToken.AccessToken(appId, appSecret, parms);
		}
		return AccessToken.get("AccessTokenJson");
	}

	/**
	 * 获取 AccessToken
	 * 
	 * @param appId
	 * @param appSecret
	 * @throws IOException
	 */
	public synchronized static void AccessToken(String appId, String appSecret,
			Map<String, Object> parms) throws Exception {
       	String grant_type = "client_credential";
		String result = doPost("https://api.weixin.qq.com/cgi-bin/token?grant_type="
						+ grant_type + "&appid=" + appId + "&secret="
						+ appSecret, parms);
		com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject
				.parse(result);
		json.put("time", System.currentTimeMillis());
		AccessToken.put("AccessTokenJson", json);
//		int count = 0;
//		count ++;
//		log.info("Access_token count :" + count);
	}

    static {
        HttpConnectionManager httpConManager = new MultiThreadedHttpConnectionManager();
        // HttpConnectionManagerParams params = httpConManager.getParams();
        client = new HttpClient(httpConManager);
        client.getParams().setParameter("http.protocol.content-charset",
                "UTF-8");
    }

    public static String doPost(String url, Map<String, Object> parms)
            throws Exception {
        PostMethod method = new PostMethod(url);
        String responseMsg = "";
        NameValuePair[] data = {};
        if (parms != null && !parms.isEmpty()) {
            Set<Entry<String, Object>> set = parms.entrySet();

            Vector<NameValuePair> nameValuePairs = new Vector<NameValuePair>();
            for (Iterator<Entry<String, Object>> iterator = set.iterator(); iterator
                    .hasNext();) {
                Entry<String, Object> entry = (Entry<String, Object>) iterator
                        .next();
                if (entry.getValue() == null) {
                    nameValuePairs.add(new NameValuePair(entry.getKey(), ""));
                } else {
                    nameValuePairs.add(new NameValuePair(entry.getKey(), entry
                            .getValue().toString()));
                }
            }
            data = nameValuePairs.toArray(new NameValuePair[nameValuePairs
                    .size()]);
        }
        // 设置参数
        method.setRequestBody(data);

        int statusCode = client.executeMethod(method);
        if (statusCode == 200) {
            responseMsg = method.getResponseBodyAsString();
        }
        return responseMsg;
    }
}