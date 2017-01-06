package com.commons.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.commons.util.ExceptionUtil;

public class SysRuntimeException extends RuntimeException {

	private Result result;

	/**
	 * @param result
	 * @param cause
	 */
	public SysRuntimeException(Result result, Throwable cause) {
		super(result.getMsg(), cause);
		this.result = result;
	}

	/**
	 * @param code
	 * @param msg
	 */
	public SysRuntimeException(int code, String msg) {
		super(msg);
		this.result = new Result(code, msg);
	}

	/**
	 * @param result
	 * @param detail
	 */
	public SysRuntimeException(Result result, String detail) {
		super(result.getMsg() + "," + detail);
		this.result = new Result(result.getCode(), result.getMsg() + "," + detail);
	}

	/**
	 * @param result
	 * @param detail
	 * @param cause
	 */
	public SysRuntimeException(Result result, String detail, Throwable cause) {
		super(result.getMsg() + "," + detail, cause);
		this.result = new Result(result.getCode(), result.getMsg() + "," + detail);
	}

	/**
	 * @param code
	 * @param msg
	 * @param cause
	 */
	public SysRuntimeException(int code, String msg, Throwable cause) {
		super(msg, cause);
		this.result = new Result(code, msg);
	}

	/**
	 * @param code
	 * @param cause
	 */
	public SysRuntimeException(int code, Throwable cause) {
		super(cause);
		this.result = new Result(code, null);
	}

	/**
	 * @return
	 */
	@Override
	public String getMessage() {
		return ExceptionUtil.buildMessage(super.getMessage(), getCause());
	}
	/**
	 * @return
	 */
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<exception>");
		
		if (getResult() != null)
			sb.append(result.toString());

		sb.append("<exceptionTrace>");
		sb.append(getMessage());
		sb.append("</exceptionTrace>");
		
		sb.append("<exception/>");
		return sb.toString();
	}

	@Override
	public void printStackTrace(PrintStream ps) {
		ps.print("<exception>");
		if (getResult() != null) {
			ps.print(result.toString());
		} 
		ps.append("<exceptionTrace>");
		
		Throwable cause = getCause();
		if (cause == null) {
			super.printStackTrace(ps);
		} else {
			ps.println(this);
			ps.print("Caused by: ");
			cause.printStackTrace(ps);
		}
		ps.append("</exceptionTrace>");
		ps.println("</exception>");
	}

	@Override
	public void printStackTrace(PrintWriter pw) {
		pw.print("<exception>");
		if (getResult() != null) {
			pw.print(result.toString());
		} 
		pw.append("<exceptionTrace>");
		
		Throwable cause = getCause();
		if (cause == null) {
			super.printStackTrace(pw);
		} else {
			pw.println(this);
			pw.print("Caused by: ");
			cause.printStackTrace(pw);
		}
		pw.append("</exceptionTrace>");
		pw.println("</exception>");
	}

	/**
	 * @return
	 */
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
