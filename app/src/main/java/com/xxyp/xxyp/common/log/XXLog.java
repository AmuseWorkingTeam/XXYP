
package com.xxyp.xxyp.common.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Description : 日志打印 Created by sunpengfei on 2017/8/3. Person in charge :
 * sunpengfei
 */
public class XXLog {
    private static final String DEFAULT_TAG = "XXYP";

    public static boolean debug = true;

    static {
        if (debug) {
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                    .methodCount(0)         // (Optional) How many method line to show. Default 2
                    .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                    .tag(DEFAULT_TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                    .build();
            // 初始化日志打印
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        }
    }

    public static void log_d(String tag, String message) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" content is null");
            }
            Logger.t(tag).d(message);
        }
    }

    public static void log_d(String tag, String message, Object... obj) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" Invalid format");
            }
            Logger.t(tag).d(message, obj);
        }
    }

    public static void log_e(String tag, String message) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" content is null");
            }
            Logger.t(tag).e(message);
        }
    }

    public static void log_e(String tag, String message, Object... obj) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" Invalid format");
            }
            Logger.t(tag).e(message, obj);
        }
    }

    public static void log_e(String tag, Throwable throwable, String message,
            Object... obj) {
        if (debug) {
            if ((message == null) || (throwable == null)) {
                throw new IllegalArgumentException(" Invalid format or throwable is null");
            }
            Logger.t(tag).e(throwable, message, obj);
        }
    }

    public static void log_i(String tag, String message) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" content is null");
            }
            Logger.t(tag).i(message);
        }
    }

    public static void log_i(String tag, String message, Object... obj) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" Invalid format");
            }
            Logger.t(tag).i(message, obj);
        }
    }


    public static void log_v(String tag, String message) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" content is null");
            }
            Logger.t(tag).v(message);
        }
    }

    public static void log_w(String tag, String message) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" content is null");
            }
            Logger.t(tag).w(message);
        }
    }

    public static void log_w(String tag, String message, Object... obj) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" Invalid format");
            }
            Logger.t(tag).w(message, obj);
        }
    }

    public static void log_json(String tag, String json) {
        if (debug) {
            Logger.t(tag).json(json);
        }
    }

    public static void log_obj(String tag, Object obj) {
        if (debug) {
            if (obj == null) {
                throw new IllegalArgumentException(" Invalid object");
            }
            Logger.t(tag).d(obj);
        }
    }

    public static void log_wtf(String tag, String message) {
        if (debug) {
            if (message == null) {
                throw new IllegalArgumentException(" content is null");
            }
            Logger.t(tag).wtf(message);
        }
    }

    public static void log_xml(String tag, String xml) {
        if (debug) {
            Logger.t(tag).xml(xml);
        }
    }
}
