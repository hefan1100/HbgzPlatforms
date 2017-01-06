package com.commons.util;

import java.net.URL;

public class Path {

    public static String getPath(String fileName) {
        // 得到工程路径
        String path = System.getProperty("user.dir");
        String fs = System.getProperty("file.separator");
        path = path + fs + "WebRoot" + fs + "jsp" + fs + "workflow" + fs + "jsondata" + fs + fileName;
        System.out.println("path : " + path);
        return path;
    }

    public static String getResource() {
        URL url = Path.class.getResource("/");
        String rePath = url.toString();
        rePath = rePath.substring(6, rePath.length() - 8);
        return rePath;
    }

    public static void main(String[] args) {
        Path path = new Path();
        Path.getPath("aaa");
        System.out.println(path.getResource());
    }
}
