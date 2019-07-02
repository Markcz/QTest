package com.mark.pictureselector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class AndroidQUtils {


    static final String TAG = "AndroidQUtils";


    /**
     * 是否是Android Q及更高版本
     *
     * @return
     */
    public static boolean isAndroidQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /***
     * 使用SAF框架 获取授权目录访问权限
     * @param activity
     * @param requestCode
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void triggerStorageAccessFramework(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取已授权的检查某个folder或者文件有访问权限？
     *
     * @param context
     * @return
     */
    public static List<UriPermission> getStorageAccessPermission(Context context) {
        return context.getContentResolver().getPersistedUriPermissions();
    }


    /**
     * 判断 某个Uri 是否已授权访问
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean checkUriPermission(Context context, Uri uri) {
        List<UriPermission> uriPermissions = getStorageAccessPermission(context);
        List<String> strings = new ArrayList<>();
        for (UriPermission uriPermission : uriPermissions) {
            strings.add(uriPermission.getUri().toString());
            Log.e(TAG,"uriPermissions " + uriPermission.getUri().toString());
        }
        return strings.contains(uri);
    }

    /**
     * 拿权限
     *
     * @param context
     * @param uri
     */
    public static void takePersistedUriPermissions(Context context, Uri uri) {
        context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }



}
