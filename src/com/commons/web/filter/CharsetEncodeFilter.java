package com.commons.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 字符集过滤器
 * 使用例子,此例子表示把所有的请求的字符集转换为UTF-8
 *	<filter>
 *		<filter-name>encodingFilter</filter-name>
 *		<filter-class>com.commons.web.filter.CharsetEncodeFilter</filter-class>
 *		<init-param>
 *			<param-name>encoding</param-name>
 *			<param-value>GBK</param-value>
 *		</init-param>
 *	</filter>
 *	<filter-mapping>
 *		<filter-name>encodingFilter</filter-name>
 *		<url-pattern>/*</url-pattern>
 *	</filter-mapping>
 * @author wubd
 *
 */
public class CharsetEncodeFilter extends OnceRequestFilter {

    private static final String FILTER_PARAM_ENCODING = "encoding";
    private String encoding;
    private static final String DEFAULT_CHAR_SET = "GBK";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (this.encoding != null && request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(this.encoding);
            response.setCharacterEncoding(this.encoding);
        }

        filterChain.doFilter(request, response);


        /*//如果是Ajax请求判断session是否超时
          if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
              response.setHeader("sysRetCode", "-1111");
              response.setHeader("sysErrorPage", request.getContextPath()+"/login.jsp");
          } else {
              filterChain.doFilter(request, response);
          }*/
    }

    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doInit(FilterConfig cfg) throws ServletException {

        this.encoding = cfg.getInitParameter(FILTER_PARAM_ENCODING);
        if (this.encoding == null) {
            this.encoding = DEFAULT_CHAR_SET;
        }
    }
}
