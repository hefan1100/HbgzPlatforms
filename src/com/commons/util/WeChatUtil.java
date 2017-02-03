package com.commons.util;

import com.Constant;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
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
        String appid = Constant.appID;
        String secret = Constant.appSecret;
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
    //根据点击菜单推送-发送消息
    public String pushMsgForEvent(HttpServletRequest request) throws Exception {
        //从request中获取map
        Map map= resolveXmlToMap(request);
        StringBuffer sb=new StringBuffer();
        sb.append("{\"touser\":\""+map.get("FromUserName")+"\",");
        sb.append("\"msgtype\":\"news\",");
        sb.append("\"news\":{\"articles\":[{");
        System.out.println(map.get("EventKey").toString());
        if(map.get("EventKey").toString().equals("qcbx")){
            sb.append("\"title\":\""+Constant.qcbx+"\",");
            sb.append("\"description\":\""+Constant.qcbx+"\",");
            sb.append("\"url\":\""+(Constant.qcbxUrl+"?openid="+map.get("FromUserName"))+"\",");
            sb.append("\"picurl\":\"http://120.27.221.0/pageApp/secure/img/banner.png\"");
            sb.append("}]}}");
            return callWeChat("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=",sb.toString());
        } else{
            return "fail";
        }

    }
    //解析微信发送的数据包
    public Map resolveXmlToMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        try{
            InputStream in=request.getInputStream();
            // 解析xml
            SAXReader reader = new SAXReader();
            Document document = null;
            try {
                document = reader.read(in);
            } catch (DocumentException e1) {
                e1.printStackTrace();
            }
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList){
                map.put(e.getName(), e.getText());
            }
        }catch (Exception e){
              e.printStackTrace();
        }
        return map;
    }
}
