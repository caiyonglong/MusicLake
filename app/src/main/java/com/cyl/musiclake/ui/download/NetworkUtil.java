package com.cyl.musiclake.ui.download;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    private static Context context;

    public NetworkUtil(Context context) {
        this.context = context;
    }


    /**
     * 检查当前网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
//
//            if (networkInfo != null && networkInfo.length > 0)
//            {
//                for (int i = 0; i < networkInfo.length; i++)
//                {
//                    System.out.println(i + "===状态===" + networkInfo[i].getState());
//                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
//                    // 判断当前网络状态是否为连接状态
//                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
//                    {
//                        return true;
//                    }
//                }
//            }

        return false;
    }


    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return
     */
    private static boolean isWifiEnabled(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static String getconnectinfo() {
        String NetworkEnvironment = null;
        if (isWifiEnabled(context)) {
            NetworkEnvironment = "WiFi";
        } else if (isNetworkAvailable(context)) {
            NetworkEnvironment = "Normal";
        } else {
            NetworkEnvironment = "Error";
        }
        return NetworkEnvironment;
    }


}
