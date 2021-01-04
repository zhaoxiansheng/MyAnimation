package com.autopai.common.utils.utils.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.autopai.common.utils.utils.Utils;
import com.autopai.common.utils.utils.bean.TaskResult;
import com.autopai.common.utils.utils.shell.ShellUtils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.MODIFY_PHONE_STATE;
import static android.content.Context.WIFI_SERVICE;

public final class NetworkUtils {

    private NetworkUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public enum NetworkType {
        /**
         * 以太网
         */
        NETWORK_ETHERNET,
        /**
         * wifi
         */
        NETWORK_WIFI,
        /**
         * 4G
         */
        NETWORK_4G,
        /**
         * 3G
         */
        NETWORK_3G,
        /**
         * 2G
         */
        NETWORK_2G,
        /**
         * 未知网络类型
         */
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    /**
     * 打开无线设置.
     */
    public static void openWirelessSettings() {
        Utils.getApp().startActivity(
                new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    /**
     * 判断是否连接网络
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 判断是否可以获取到网络
     *
     * @param callback
     * @return
     */
    @RequiresPermission(INTERNET)
    public static Utils.Task<Boolean> isAvailableAsync(@NonNull final Utils.Callback<Boolean> callback) {
        return Utils.doAsync(new Utils.Task<Boolean>(callback) {
            @RequiresPermission(INTERNET)
            @Override
            public Boolean doInBackground() {
                return isAvailable();
            }
        });
    }

    /**
     * 判断是否可以获取到网络
     *
     * @return
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailable() {
        return isAvailableByDns() || isAvailableByPing(null);
    }

    /**
     * 是否可以ping通某个ip地址
     *
     * @param callback
     */
    @RequiresPermission(INTERNET)
    public static void isAvailableByPingAsync(final Utils.Callback<Boolean> callback) {
        isAvailableByPingAsync("", callback);
    }

    /**
     * 是否可以ping通某个ip地址
     *
     * @param ip
     * @param callback
     * @return
     */
    @RequiresPermission(INTERNET)
    public static Utils.Task<Boolean> isAvailableByPingAsync(final String ip,
                                                             @NonNull final Utils.Callback<Boolean> callback) {
        return Utils.doAsync(new Utils.Task<Boolean>(callback) {
            @RequiresPermission(INTERNET)
            @Override
            public Boolean doInBackground() {
                return isAvailableByPing(ip);
            }
        });
    }

    /**
     * 是否可以ping通某个ip地址
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailableByPing() {
        return isAvailableByPing("");
    }

    /**
     * 是否可以ping通某个ip地址
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailableByPing(final String ip) {
        final String realIp = TextUtils.isEmpty(ip) ? "223.5.5.5" : ip;
        TaskResult<Integer> result = ShellUtils.execCmd(String.format("ping -c 1 %s", realIp), false);
        boolean ret = result.result == 0;
        if (result.errorMsg != null) {
            Log.d("NetworkUtils", "isAvailableByPing() called" + result.errorMsg);
        }
        if (result.successMsg != null) {
            Log.d("NetworkUtils", "isAvailableByPing() called" + result.successMsg);
        }
        return ret;
    }

    /**
     * 是否可以获得DNS
     *
     * @param callback
     */
    @RequiresPermission(INTERNET)
    public static void isAvailableByDnsAsync(final Utils.Callback<Boolean> callback) {
        isAvailableByDnsAsync("", callback);
    }

    /**
     * 是否可以获得DNS
     *
     * @param domain
     * @param callback
     * @return
     */
    @RequiresPermission(INTERNET)
    public static Utils.Task isAvailableByDnsAsync(final String domain,
                                                   @NonNull final Utils.Callback<Boolean> callback) {
        return Utils.doAsync(new Utils.Task<Boolean>(callback) {
            @RequiresPermission(INTERNET)
            @Override
            public Boolean doInBackground() {
                return isAvailableByDns(domain);
            }
        });
    }

    /**
     * 是否可以获得DNS
     *
     * @return
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailableByDns() {
        return isAvailableByDns("");
    }

    /**
     * 是否可以获得DNS
     *
     * @param domain
     * @return
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailableByDns(final String domain) {
        final String realDomain = TextUtils.isEmpty(domain) ? "www.baidu.com" : domain;
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(realDomain);
            return inetAddress != null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取是否开启移动数据
     *
     * @return
     */
    public static boolean getMobileDataEnabled() {
        try {
            TelephonyManager tm =
                    (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm.isDataEnabled();
            }
            @SuppressLint("PrivateApi")
            Method getMobileDataEnabledMethod =
                    tm.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            Log.e("NetworkUtils", "getMobileDataEnabled: ", e);
        }
        return false;
    }

    /**
     * 设置是否开启移动数据
     *
     * @param enabled
     * @return
     */
    @RequiresPermission(MODIFY_PHONE_STATE)
    public static boolean setMobileDataEnabled(final boolean enabled) {
        try {
            TelephonyManager tm =
                    (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.setDataEnabled(enabled);
                return false;
            }
            Method setDataEnabledMethod =
                    tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setDataEnabledMethod) {
                setDataEnabledMethod.invoke(tm, enabled);
                return true;
            }
        } catch (Exception e) {
            Log.e("NetworkUtils", "setMobileDataEnabled: ", e);
        }
        return false;
    }

    /**
     * 获取判断移动数据状态
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isMobileData() {
        NetworkInfo info = getActiveNetworkInfo();
        return null != info
                && info.isAvailable()
                && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 是否是4G
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean is4G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null
                && info.isAvailable()
                && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 获取wifi是否开启
     *
     * @return
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) Utils.getApp().getSystemService(WIFI_SERVICE);
        if (manager == null) {
            return false;
        }
        return manager.isWifiEnabled();
    }

    /**
     * 设置wifi开关
     *
     * @param enabled
     */
    @RequiresPermission(CHANGE_WIFI_STATE)
    public static void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) Utils.getApp().getSystemService(WIFI_SERVICE);
        if (manager == null) {
            return;
        }
        if (enabled == manager.isWifiEnabled()) {
            return;
        }
        manager.setWifiEnabled(enabled);
    }

    /**
     * WiFi是否连接
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * wifi是否可以获得
     *
     * @return
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static boolean isWifiAvailable() {
        return getWifiEnabled() && isAvailable();
    }

    /**
     * wifi是否可以获得
     *
     * @param callback
     * @return
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static Utils.Task<Boolean> isWifiAvailableAsync(@NonNull final Utils.Callback<Boolean> callback) {
        return Utils.doAsync(new Utils.Task<Boolean>(callback) {
            @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
            @Override
            public Boolean doInBackground() {
                return isWifiAvailable();
            }
        });
    }

    /**
     * 获取网络运营商
     *
     * @return
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm =
                (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getNetworkOperatorName();
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkType getNetworkType() {
        if (isEthernet()) {
            return NetworkType.NETWORK_ETHERNET;
        }
        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetworkType.NETWORK_2G;

                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetworkType.NETWORK_3G;

                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetworkType.NETWORK_4G;

                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return NetworkType.NETWORK_3G;
                        }
                }
            }
        }
        return NetworkType.NETWORK_UNKNOWN;
    }

    /**
     * 是否是以太网
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static boolean isEthernet() {
        final ConnectivityManager cm =
                (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        final NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (info == null) {
            return false;
        }
        NetworkInfo.State state = info.getState();
        if (null == state) {
            return false;
        }
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
    }

    /**
     * 获取当前网络的信息
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm =
                (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return null;
        }
        return cm.getActiveNetworkInfo();
    }

    /**
     * 获取ip地址
     *
     * @param useIPv4
     * @param callback
     * @return
     */
    public static Utils.Task<String> getIPAddressAsync(final boolean useIPv4,
                                                       @NonNull final Utils.Callback<String> callback) {
        return Utils.doAsync(new Utils.Task<String>(callback) {
            @RequiresPermission(INTERNET)
            @Override
            public String doInBackground() {
                return getIPAddress(useIPv4);
            }
        });
    }

    /**
     * 获取ip地址
     *
     * @param useIPv4
     * @return
     */
    @RequiresPermission(INTERNET)
    public static String getIPAddress(final boolean useIPv4) {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp() || ni.isLoopback()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement());
                }
            }
            for (InetAddress add : adds) {
                if (!add.isLoopbackAddress()) {
                    String hostAddress = add.getHostAddress();
                    boolean isIPv4 = hostAddress.indexOf(':') < 0;
                    if (useIPv4) {
                        if (isIPv4) {
                            return hostAddress;
                        }
                    } else {
                        if (!isIPv4) {
                            int index = hostAddress.indexOf('%');
                            return index < 0
                                    ? hostAddress.toUpperCase()
                                    : hostAddress.substring(0, index).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 返回广播的ip地址
     *
     * @return
     */
    public static String getBroadcastIpAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp() || ni.isLoopback()) {
                    continue;
                }
                List<InterfaceAddress> ias = ni.getInterfaceAddresses();
                for (int i = 0, size = ias.size(); i < size; i++) {
                    InterfaceAddress ia = ias.get(i);
                    InetAddress broadcast = ia.getBroadcast();
                    if (broadcast != null) {
                        return broadcast.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取域名地址
     *
     * @param domain
     * @param callback
     * @return
     */
    @RequiresPermission(INTERNET)
    public static Utils.Task<String> getDomainAddressAsync(final String domain,
                                                           @NonNull final Utils.Callback<String> callback) {
        return Utils.doAsync(new Utils.Task<String>(callback) {
            @RequiresPermission(INTERNET)
            @Override
            public String doInBackground() {
                return getDomainAddress(domain);
            }
        });
    }

    /**
     * 获取域名地址
     *
     * @param domain
     * @return
     */
    @RequiresPermission(INTERNET)
    public static String getDomainAddress(final String domain) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取WiFi的ip地址
     *
     * @return
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getIpAddressByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().ipAddress);
    }

    /**
     * 获取wifi的网关
     *
     * @return
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getGatewayByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().gateway);
    }

    /**
     * 获取wifi的网络掩码
     *
     * @return
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getNetMaskByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().netmask);
    }

    /**
     * 获取WiFi的服务地址
     *
     * @return
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getServerAddressByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().serverAddress);
    }
}
