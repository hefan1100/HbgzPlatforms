package com.commons.web.filter;

import com.commons.util.MD5;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.io.OutputStream;

public class AccessFilter implements Filter {
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // 获得在下面代码中要用的request,response,session对象
        request.setCharacterEncoding("utf-8"); response.setCharacterEncoding("utf-8");
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String strRootPath = servletRequest.getContextPath();
        HttpSession session = servletRequest.getSession();
        String userInfo = (String) session.getAttribute("userInfo") == null ? "" : (String) session.getAttribute("userInfo");
        String mobileNumber = servletRequest.getParameter("mobileNumber") == null ? "" : servletRequest.getParameter("mobileNumber");
        String pass = (String) servletRequest.getParameter("pass") == null ? "" : (String) servletRequest.getParameter("pass");
        String path = servletRequest.getRequestURI();

        chain.doFilter(servletRequest, servletResponse);

//        if (path.indexOf("/sign-in.html") > -1 || path.indexOf("/js/") > -1 || path.indexOf("/lib/") > -1 || path.indexOf("/img/") > -1
//                || path.indexOf("/stylesheets/") > -1 || path.indexOf("/pageApp/") > -1 || path.indexOf(".js") > -1 || path.indexOf(".ico") > -1) {
//            if (path.indexOf("/login.html") > -1){
//                session.setAttribute("sign-in.html", "");
//                session.setAttribute("pass", "");
//            }
//            chain.doFilter(servletRequest, servletResponse);
//        } else {
//            if ("".equals(userInfo)) {  // 判断是否有登录信息.
//                if (path.indexOf("/index.html") > -1) {
//                    if (!"".equals(mobileNumber) && !"".equals("pass")) {
//                        MD5 md5 = new MD5();
//                        if (md5.getMD5ofStr(mobileNumber.substring(6, 11)).toUpperCase().equals(pass)) {
//                            session.setAttribute("userInfo", mobileNumber);
//                            session.setAttribute("pass", pass);
//                            chain.doFilter(servletRequest, servletResponse);
//                        } else {
//                            servletResponse.sendRedirect(strRootPath + "/sign-in.html");
//                        }
//                    } else {
//                        servletResponse.sendRedirect(strRootPath + "/sign-in.html");
//                    }
//                } else {
//                    servletResponse.sendRedirect(strRootPath + "/sign-in.html");
//                }
//            } else {
//                chain.doFilter(servletRequest, servletResponse);
//            }
//        }
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
