package com.webservice;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;

public class FileUploader extends HttpServlet {



    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        String ajaxUpdateResult = "";
        try {
            List items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (int i=0;i<items.size();i++) {
                FileItem item= (FileItem) items.get(i);
                if (item.isFormField()) {
                    //表单
                } else {
                    //文件
                    String fileName = item.getName();
                    String[] type=fileName.split("\\.");
                    String tt=type[type.length-1];
                    //以时间形式保存文件名
                    Date date=new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
                    String filename=sdf.format(date);
                    //获取路径
                    String rootpath = request.getRealPath(request.getRequestURI());
                    rootpath = rootpath.substring(0, rootpath.lastIndexOf("file"));
                    String serverpath = rootpath + "upload/"+filename+"."+tt;
                    ajaxUpdateResult=filename+"."+tt;
                    System.out.println(serverpath);
                    File f=new File(serverpath);
                    //保存
                    item.write(f);

                    InputStream content = item.getInputStream();
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                }
            }
        } catch (FileUploadException e) {
            throw new ServletException("Parsing file upload failed.", e);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        response.getWriter().print(ajaxUpdateResult);
    }
}