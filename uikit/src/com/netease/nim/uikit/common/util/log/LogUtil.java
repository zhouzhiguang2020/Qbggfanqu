package com.netease.nim.uikit.common.util.log;

/**
 * vERBOSE 类型调试信息，verbose啰嗦的意思
 DEBUG 类型调试信息, debug调试信息
 INFO   类型调试信息, 一般提示性的消息information
 WARN  类型调试信息，warning警告类型信息
 ERROR 类型调试信息，错误信息
 */
public class LogUtil {
    public static final void init(String logFile, int level) {
        LogImpl.init(logFile, level);
    }

	public static final void v(String tag, String msg) {
		LogImpl.v(tag, buildMessage(msg));
	}

	public static final void v(String tag, String msg, Throwable thr) {
		LogImpl.v(tag, buildMessage(msg), thr);
	}

	public static final void d(String tag, String msg) {
		LogImpl.d(tag, buildMessage(msg));
	}

	public static final void d(String tag, String msg, Throwable thr) {
		LogImpl.d(tag, buildMessage(msg), thr);
	}

	public static final void i(String tag, String msg) {
		LogImpl.i(tag, buildMessage(msg));
	}

	public static final void i(String tag, String msg, Throwable thr) {
		LogImpl.i(tag, buildMessage(msg), thr);
	}

	public static final void w(String tag, String msg) {
		LogImpl.w(tag, buildMessage(msg));
	}

	public static final void w(String tag, String msg, Throwable thr) {
		LogImpl.w(tag, buildMessage(msg), thr);
	}

	public static final void w(String tag, Throwable thr) {
		LogImpl.w(tag, buildMessage(""), thr);
	}

	public static final void e(String tag, String msg) {
		LogImpl.e(tag, buildMessage(msg));
	}

	public static final void e(String tag, String msg, Throwable thr) {
		LogImpl.e(tag, buildMessage(msg), thr);
	}
	
	public static final void ui(String msg) {
		LogImpl.i("ui", buildMessage(msg));
	}

	public static final void res(String msg) {
		LogImpl.i("RES", buildMessage(msg));
	}

	public static final void audio(String msg) {
		LogImpl.i("AudioRecorder", buildMessage(msg));
	}

	public static String getLogFileName(String cat) {
		return LogImpl.getLogFileName(cat);
	}

	private static String buildMessage(String msg) {
		return msg;
	}
}
