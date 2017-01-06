package com.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.SQLException;

public class StringUtil {
    /**
     * 判断字符串是否为空
     * @param principal		待判断的字符串
     * @return				判断结果
     */
    public static boolean isBlank(String principal)
    {
        return ( principal == null ) || ( principal.equals( "" ) );
    }
    /**
     * 将字符串转换为中文
     * @param s		转换前的字符串
     * @return		转换后的字符串
     */
    public static String changToChinese( String s )
    {
        if (s == null)
            return "";
        byte[] aa;
        try
        {
            aa = s.getBytes("8859_1");
            String ss = new String( aa, "GBK" );
            return ss;
        }
        catch ( UnsupportedEncodingException e )
        {
            return s;
        }
    }
    /**
     * 将字符串转换为英文
     * @param s		转换前的字符串
     * @return		转换后的字符串
     */
    public static String changToEnglish(String s)
    {
        if (s == null)
            return "";
        try
        {
            byte[] aa = s.getBytes("GBK");
            String ss = new String(aa, "8859_1");
            return ss;
        }
        catch (UnsupportedEncodingException e)
        {
            return s;
        }
    }
    public static String ClobToString(Clob clob) throws SQLException, IOException {

        String reString = "";
        Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给 StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }

    /**
     * 判断字符串是否为null或者空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 字符串替换


     *
     * @param text
     *            原始字符串


     * @param repl
     *            想被替换的内容


     * @param with
     *            替换后的内容
     * @return
     */
    public static String replace(String text, String repl, String with) {
        if (text == null || repl == null || with == null || repl.length() == 0) {
            return text;
        }

        StringBuffer buf = new StringBuffer(text.length());
        int searchFrom = 0;
        while (true) {
            int foundAt = text.indexOf(repl, searchFrom);
            if (foundAt == -1) {
                break;
            }

            buf.append(text.substring(searchFrom, foundAt)).append(with);
            searchFrom = foundAt + repl.length();
        }
        buf.append(text.substring(searchFrom));

        return buf.toString();
    }
}
