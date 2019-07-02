package com.mark.pictureselector;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class PictureSelector {

    private static Application app;


    private static Handler handler;//主线程handler

    static Toast toast;
    static TextView toastTextView;


    public static void init(Application a) {
        app = a;
    }


    public static Context getContext() {
        return app;
    }


    /**
     * 获取Drawable
     *
     * @param drawableId
     * @return Drawable
     */
    public static Drawable getDrawable(@DrawableRes int drawableId) {
        return app.getResources().getDrawable(drawableId);
    }


    /**
     * 通过资源id获取字符串
     */
    public static String getString(@StringRes int id) {
        return app.getResources().getString(id);
    }

    /**
     * 获取应用包名
     */
    public static String getPackageName() {
        return app.getPackageName();
    }


    /**
     * 获取应用版本号名称
     */
    public static String getAppVersionName() {
        try {
            return app.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前应用程序的版本号。
     *
     * @return 当前应用程序的版本号。
     */
    public static int getAppVersion() {
        try {
            return app.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return 1;
        }
    }


    /**
     * 获取当前应用的图标
     *
     * @return
     */
    public static Drawable getAppIcon() {
        return getAppIcon(getPackageName());
    }

    /**
     * 获取指定应用包名的图标
     *
     * @param packageName
     * @return
     */
    public static Drawable getAppIcon(String packageName) {
        try {
            return app.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


    private static Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    /**
     * 任意线程
     * 显示自定义toast 默认位置 ： 屏幕底居上
     */
    public static void showToast(final String msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            show(msg);
        } else {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    show(msg);
                }
            });
        }
    }


    private static void show(String msg) {
        if (toast == null) {
            toastTextView = new TextView(app);
            toastTextView.setGravity(Gravity.CENTER);
            //toastTextView.setBackgroundColor(Color.BLACK);
            toastTextView.setBackground(ContextCompat.getDrawable(app, R.drawable.bg_toast));
            toastTextView.setTextColor(Color.WHITE);
            toastTextView.setMaxHeight(dp2px(45f));
            int horizontalPadding = dp2px(10f);
            int verticalPadding = dp2px(4f);
            toastTextView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);

            toast = new Toast(app);
            toast.setView(toastTextView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, dp2px(80f));
        }
        toastTextView.setText(msg);
        toast.show();
    }


    /**
     * dp 转 px
     *
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, app.getResources().getDisplayMetrics());
    }

    /**
     * px 转 dp
     *
     * @param px
     * @return
     */
    public static int px2dp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, app.getResources().getDisplayMetrics());
    }


}
