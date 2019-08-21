package com.cyl.musiclake.utils.rom

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.cyl.musiclake.common.Constants
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object HuaweiUtils {
    private val TAG = "HuaweiUtils"

    /**
     * 检测 Huawei 悬浮窗权限
     */
    fun checkFloatWindowPermission(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) //OP_SYSTEM_ALERT_WINDOW = 24;
        } else true
    }

    /**
     * 去华为权限申请页面
     */
    fun applyPermission(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            //   ComponentName comp = new ComponentName("com.huawei.systemmanager",
            //      "com.huawei.permissionmanager.ui.SingleAppActivity");//华为权限管理，跳转到指定app的权限管理位置需要华为接口权限，未解决
            var comp = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity")//悬浮窗管理页面
            intent.component = comp
            if (RomUtils.emuiVersion == 3.1) {
                //emui 3.1 的适配
                (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            } else {
                //emui 3.0 的适配
                comp = ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity")//悬浮窗管理页面
                intent.component = comp
                (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            }
        } catch (e: SecurityException) {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            val comp = ComponentName("com.huawei.systemmanager",
                    "com.huawei.permissionmanager.ui.MainActivity")//华为权限管理，跳转到本app的权限管理页面,这个需要华为接口权限，未解决
            //      ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
            intent.component = comp
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            Log.e(TAG, Log.getStackTraceString(e))
        } catch (e: ActivityNotFoundException) {
            /**
             * 手机管家版本较低 HUAWEI SC-UL10
             */
            //   Toast.makeText(MainActivity.this, "act找不到", Toast.LENGTH_LONG).show();
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName("com.Android.settings", "com.android.settings.permission.TabItem")//权限管理页面 android4.4
            //   ComponentName comp = new ComponentName("com.android.settings","com.android.settings.permission.single_app_activity");//此处可跳转到指定app对应的权限管理页面，但是需要相关权限，未解决
            intent.component = comp
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            e.printStackTrace()
            Log.e(TAG, Log.getStackTraceString(e))
        } catch (e: Exception) {
            //抛出异常时提示信息
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show()
            Log.e(TAG, Log.getStackTraceString(e))
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val clazz = AppOpsManager::class.java
                val method = clazz.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                return AppOpsManager.MODE_ALLOWED == method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
            } catch (e: Exception) {
                Log.e(TAG, Log.getStackTraceString(e))
            }

        } else {
            Log.e(TAG, "Below API 19 cannot invoke!")
        }
        return false
    }
}

object MeizuUtils {
    private val TAG = "MeizuUtils"

    /**
     * 检测 meizu 悬浮窗权限
     */
    fun checkFloatWindowPermission(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) //OP_SYSTEM_ALERT_WINDOW = 24;
        } else true
    }

    /**
     * 去魅族权限申请页面
     */
    fun applyPermission(context: Context) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            //            intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");//remove this line code for fix flyme6.3
            intent.putExtra("packageName", context.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
        } catch (e: Exception) {
            try {
                Log.e(TAG, "获取悬浮窗权限, 打开AppSecActivity失败, " + Log.getStackTraceString(e))
                // 最新的魅族flyme 6.2.5 用上述方法获取权限失败, 不过又可以用下述方法获取权限了
                FloatUtil.commonROMPermissionApplyInternal(context)
            } catch (eFinal: Exception) {
                Log.e(TAG, "获取悬浮窗权限失败, 通用获取方法失败, " + Log.getStackTraceString(eFinal))
            }

        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val clazz = AppOpsManager::class.java
                val method = clazz.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                return AppOpsManager.MODE_ALLOWED == method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
            } catch (e: Exception) {
                Log.e(TAG, Log.getStackTraceString(e))
            }

        } else {
            Log.e(TAG, "Below API 19 cannot invoke!")
        }
        return false
    }
}

object MiuiUtils {
    private val TAG = "MiuiUtils"

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    val miuiVersion: Int
        get() {
            val version = RomUtils.getSystemProperty("ro.miui.ui.version.name")
            if (version != null) {
                try {
                    return Integer.parseInt(version.substring(1))
                } catch (e: Exception) {
                    Log.e(TAG, "get miui version code error, version : $version")
                    Log.e(TAG, Log.getStackTraceString(e))
                }

            }
            return -1
        }

    /**
     * 检测 miui 悬浮窗权限
     */
    fun checkFloatWindowPermission(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT

        return if (version >= 19) {
            checkOp(context, 24) //OP_SYSTEM_ALERT_WINDOW = 24;
        } else {
            //            if ((context.getApplicationInfo().flags & 1 << 27) == 1) {
            //                return true;
            //            } else {
            //                return false;
            //            }
            true
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val clazz = AppOpsManager::class.java
                val method = clazz.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                return AppOpsManager.MODE_ALLOWED == method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
            } catch (e: Exception) {
                Log.e(TAG, Log.getStackTraceString(e))
            }

        } else {
            Log.e(TAG, "Below API 19 cannot invoke!")
        }
        return false
    }

    /**
     * 小米 ROM 权限申请
     */
    fun applyMiuiPermission(context: Context) {
        val versionCode = miuiVersion
        when (versionCode) {
            5 -> goToMiuiPermissionActivity_V5(context)
            6 -> goToMiuiPermissionActivity_V6(context)
            7 -> goToMiuiPermissionActivity_V7(context)
            else -> goToMiuiPermissionActivity_V8(context)
//            else -> Log.e(TAG, "this is a special MIUI rom version, its version code $versionCode")
        }
    }

    private fun isIntentAvailable(intent: Intent?, context: Context): Boolean {
        return if (intent == null) {
            false
        } else context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0
    }

    /**
     * 小米 V5 版本 ROM权限申请
     */
    private fun goToMiuiPermissionActivity_V5(context: Context) {
        var intent: Intent?
        val packageName = context.packageName
        intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (isIntentAvailable(intent, context)) {
//            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
        } else {
            Log.e(TAG, "intent is not available!")
        }

        //设置页面在应用详情页面
        //        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        //        PackageInfo pInfo = null;
        //        try {
        //            pInfo = context.getPackageManager().getPackageInfo
        //                    (HostInterfaceManager.getHostInterface().getApp().getPackageName(), 0);
        //        } catch (PackageManager.NameNotFoundException e) {
        //            AVLogUtils.e(TAG, e.getMessage());
        //        }
        //        intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
        //        intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        if (isIntentAvailable(intent, context)) {
        //            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW);
        //        } else {
        //            AVLogUtils.e(TAG, "Intent is not available!");
        //        }
    }

    /**
     * 小米 V6 版本 ROM权限申请
     */
    fun goToMiuiPermissionActivity_V6(context: Context) {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
        intent.putExtra("extra_pkgname", context.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        if (isIntentAvailable(intent, context)) {
//            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
        } else {
            Log.e(TAG, "Intent is not available!")
        }
    }

    /**
     * 小米 V7 版本 ROM权限申请
     */
    fun goToMiuiPermissionActivity_V7(context: Context) {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
        intent.putExtra("extra_pkgname", context.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        if (isIntentAvailable(intent, context)) {
//            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
        } else {
            Log.e(TAG, "Intent is not available!")
        }
    }

    /**
     * 小米 V8 版本 ROM权限申请
     */
    fun goToMiuiPermissionActivity_V8(context: Context) {
        var intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
        //        intent.setPackage("com.miui.securitycenter");
        intent.putExtra("extra_pkgname", context.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        if (isIntentAvailable(intent, context)) {
//            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
        } else {
            intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.setPackage("com.miui.securitycenter")
            intent.putExtra("extra_pkgname", context.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            if (isIntentAvailable(intent, context)) {
//                (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
                (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            } else {
                Log.e(TAG, "Intent is not available!")
            }
        }
    }
}

object OppoUtils {

    private val TAG = "OppoUtils"

    /**
     * 检测 360 悬浮窗权限
     */
    fun checkFloatWindowPermission(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) //OP_SYSTEM_ALERT_WINDOW = 24;
        } else true
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val clazz = AppOpsManager::class.java
                val method = clazz.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                return AppOpsManager.MODE_ALLOWED == method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
            } catch (e: Exception) {
                Log.e(TAG, Log.getStackTraceString(e))
            }

        } else {
            Log.e(TAG, "Below API 19 cannot invoke!")
        }
        return false
    }

    /**
     * oppo ROM 权限申请
     */
    fun applyOppoPermission(context: Context) {
        //merge request from https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //com.coloros.safecenter/.sysfloatwindow.FloatWindowListActivity
            val comp = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity")//悬浮窗管理页面
            intent.component = comp
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

object QikuUtils {
    private val TAG = "QikuUtils"

    /**
     * 检测 360 悬浮窗权限
     */
    fun checkFloatWindowPermission(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) //OP_SYSTEM_ALERT_WINDOW = 24;
        } else true
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val clazz = AppOpsManager::class.java
                val method = clazz.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                return AppOpsManager.MODE_ALLOWED == method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
            } catch (e: Exception) {
                Log.e(TAG, Log.getStackTraceString(e))
            }

        } else {
            Log.e("", "Below API 19 cannot invoke!")
        }
        return false
    }

    /**
     * 去360权限申请页面
     */
    fun applyPermission(context: Context) {
        val intent = Intent()
        intent.setClassName("com.android.settings", "com.android.settings.Settings\$OverlaySettingsActivity")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (isIntentAvailable(intent, context)) {
            (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
        } else {
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity")
            if (isIntentAvailable(intent, context)) {
                (context as AppCompatActivity).startActivityForResult(intent, Constants.REQUEST_CODE_FLOAT_WINDOW)
            } else {
                Log.e(TAG, "can't open permission page with particular name, please use " + "\"adb shell dumpsys activity\" command and tell me the name of the float window permission page")
            }
        }
    }

    private fun isIntentAvailable(intent: Intent?, context: Context): Boolean {
        return if (intent == null) {
            false
        } else context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0
    }
}

object RomUtils {
    private val TAG = "RomUtils"

    /**
     * 获取 emui 版本号
     * @return
     */
    val emuiVersion: Double
        get() {
            try {
                val emuiVersion = RomUtils.getSystemProperty("ro.build.version.emui")
                val version = emuiVersion!!.substring(emuiVersion.indexOf("_") + 1)
                return java.lang.Double.parseDouble(version)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 4.0
        }

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    val miuiVersion: Int
        get() {
            val version = RomUtils.getSystemProperty("ro.miui.ui.version.name")
            if (version != null) {
                try {
                    return Integer.parseInt(version.substring(1))
                } catch (e: Exception) {
                    Log.e(TAG, "get miui version code error, version : $version")
                }

            }
            return -1
        }

    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            Log.e(TAG, "Unable to read sysprop $propName", ex)
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Exception while closing InputStream", e)
                }

            }
        }
        return line
    }

    fun checkIsHuaweiRom(): Boolean {
        return Build.MANUFACTURER.contains("HUAWEI")
    }

    /**
     * check if is miui ROM
     */
    fun checkIsMiuiRom(): Boolean {
        return !TextUtils.isEmpty(RomUtils.getSystemProperty("ro.miui.ui.version.name"))
    }

    fun checkIsMeizuRom(): Boolean {
        //return Build.MANUFACTURER.contains("Meizu");
        val meizuFlymeOSFlag = RomUtils.getSystemProperty("ro.build.display.id")
        return if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
            false
        } else meizuFlymeOSFlag!!.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme")
    }

    fun checkIs360Rom(): Boolean {
        //fix issue https://github.com/zhaozepeng/FloatWindowPermission/issues/9
        return Build.MANUFACTURER.contains("QiKU") || Build.MANUFACTURER.contains("360")
    }

    fun checkIsOppoRom(): Boolean {
        //https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        return Build.MANUFACTURER.contains("OPPO") || Build.MANUFACTURER.contains("oppo")
    }
}

