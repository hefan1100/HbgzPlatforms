package com.commons.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class OnceRequestFilter implements Filter {

	private FilterConfig _filterConfig;
	public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest)
				|| !(response instanceof HttpServletResponse)) {
			throw new ServletException("just supports HTTP requests");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
		if (request.getAttribute(alreadyFilteredAttributeName) != null) {
			chain.doFilter(request, response);
		} else {
			request.setAttribute(alreadyFilteredAttributeName, "alreadyFiltered");
			try {
				doFilterInternal(httpRequest, httpResponse, chain);
			} finally {
				request.removeAttribute(alreadyFilteredAttributeName);
			}
		}
	}

	protected abstract void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException;

	private String getAlreadyFilteredAttributeName() {
		String name = this._filterConfig != null ? this._filterConfig
				.getFilterName() : getClass().getName();

		return name + ALREADY_FILTERED_SUFFIX;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this._filterConfig = filterConfig;
		doInit(this._filterConfig);
	}
	protected abstract void doInit(FilterConfig filterConfig) throws ServletException ;
}
