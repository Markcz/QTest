package com.mark.pictureselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import java.io.FileDescriptor;
import java.io.IOException;

public class ImagePicker {


    /**
     * 使用系统 图片选择器(单选)
     *
     * @param activity
     * @param requestCode
     */
    public static void startSystemImagePickerForSingle(@NonNull Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 使用系统 图片选择器多选)
     *
     * @param activity
     * @param requestCode
     */
    public static void startSystemImagePickerForMultiple(@NonNull Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 使用自定义图片选择器
     *
     * @param activity
     * @param requestCode
     */
    public static void startCustomImagePickerForMultiple(@NonNull Activity activity, int requestCode, @NonNull String key) {
        SelectorActivity.start(activity, requestCode, key);
        activity.overridePendingTransition(R.anim.anim_activity_enter_push_up, android.R.anim.fade_out);
    }


    /**
     * 返回选择的Bitmap
     *
     * @param context
     * @param uri
     * @return
     * @throws IOException
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}
