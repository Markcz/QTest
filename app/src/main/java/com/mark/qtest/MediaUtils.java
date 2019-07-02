package com.mark.qtest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MediaUtils {

    private static final String TAG = "MediaUtils";

    /**
     * 保存图片文件到集合
     * context
     * mimeType：需要保存文件的mimeType
     * displayName：展示的文件名字
     * description：文件描述信息
     *
     * @return 返回插入数据对应的uri
     */
    public static String insertImageFile(Context context, String mimeType, String displayName, String description, Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        Uri url = null;
        String stringUrl = null; /* value to be returned */
        ContentResolver cr = context.getContentResolver();
        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (url == null) {
                return null;
            }
            byte[] buffer = new byte[1024];
            ParcelFileDescriptor parcelFileDescriptor = cr.openFileDescriptor(url, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            while (true) {
                int numRead = inputStream.read(buffer);
                if (numRead == -1) {
                    break;
                }
                fileOutputStream.write(buffer, 0, numRead);
            }
            fileOutputStream.flush();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, url);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to insert media file", e);
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }
        if (url != null) {
            stringUrl = url.toString();
        }
        return stringUrl;
    }
}
