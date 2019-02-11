package com.xxyp.xxyp.map.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;

public class SysUtils {

    public SysUtils() {
    }

    public static void dismissKeyBoard(Context context) {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public static boolean hideSoftInput(Context context, IBinder binder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean bool = false;

        try {
            if (imm.isActive()) {
                if (binder != null) {
                    bool = imm.hideSoftInputFromWindow(binder, 0);
                }

                if (!bool && context instanceof Activity) {
                    Activity a = (Activity) context;
                    if (a.getCurrentFocus() != null) {
                        bool = imm.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return bool;
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.setFocusable(true);
        view.requestFocus();
        boolean bool = imm.showSoftInput(view, 0);
        if (!bool) {
            bool = imm.showSoftInput(view, 2);
        }

        if (!bool) {
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 2);
        }

    }

    public static void showKeyBoard(Context context) {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManger != null) {
                inputManger.toggleSoftInput(0, 2);
            }
        }

    }

    public static String getPhoneIp() {
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return "";
    }

    private static String getOperatorBySlot(TelephonyManager telephony, String predictedMethodName, int slotID) {
        if (telephony == null) {
            return null;
        } else {
            String inumeric = null;

            try {
                Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
                if (telephonyClass != null) {
                    Class<?>[] parameter = new Class[]{Integer.TYPE};
                    Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
                    if (getSimID != null) {
                        Object[] obParameter = new Object[]{slotID};
                        Object ob_phone = getSimID.invoke(telephony, obParameter);
                        if (ob_phone != null) {
                            inumeric = ob_phone.toString();
                        }
                    }
                }
            } catch (Exception var9) {
                var9.printStackTrace();
            }

            return inumeric;
        }
    }

    public static String getVersion(Context context) {
        String name = "3.0";

        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException var3) {
            return name;
        }
    }

    public static String getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        String build = "";
        PackageManager packageManager = context.getPackageManager();
        if (null != packageManager) {
            PackageInfo pi = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (null != pi) {
                build = pi.versionCode + "";
            }
        }

        return build;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }
}
