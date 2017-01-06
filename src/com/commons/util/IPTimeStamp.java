package com.commons.util;

import java.text.SimpleDateFormat;
import java.util.Random;
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-3
 * Time: 下午3:45
 * To change this template use File | Settings | File Templates.
 */
public class IPTimeStamp {
    private String ip;
    public IPTimeStamp(String ip) {
        this.ip = ip; // 设置 ip地址
    }
    /*
    * 将当前时间以固定的格式显示出来
    */
    private  String getTimeStamp() {
        String temp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        temp = sdf.format(new java.util.Date());
        return temp;
    }

    /*
    * 请调用此方法，生成不重复的文件名称
    */
    public String getIPTimeStampRand() {
        StringBuffer buf = new StringBuffer();
        if (ip != null) {
            String str[] = this.ip.split("\\.");
            for (int i = 0; i < str.length; i++) {
                buf.append(this.addZero(str[i], 3));
            }
        }
        buf.append(this.getTimeStamp());
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            buf.append(rand.nextInt(10)) ;
        }
        return buf.toString() ;
    }

    /*
    * 字符串后面加0
    */
    private String addZero(String str, int len) {
        StringBuffer s = new StringBuffer();
        s.append(str);
        while (s.length() < len) {
            s.insert(0, "0");
        }
        return s.toString();
    }
}
