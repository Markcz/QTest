package com.mark.qtest;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mark.pictureselector.AndroidQUtils;
import com.mark.pictureselector.ImageLoader;
import com.mark.pictureselector.ImagePicker;
import com.mark.pictureselector.PermissionHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";


    private static final String KEY = "key_image";

    private static final int Q_SYS_IMAGE_REQUEST_CODE = 1;
    private static final int Q_SYS_MULTIPLE_IMAGE_REQUEST_CODE = 11;
    private static final int Q_CUSTOM_IMAGE_REQUEST_CODE = 2;


    ImageView ivPreview;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ivPreview = findViewById(R.id.iv_preview);
        container = findViewById(R.id.ll_image_container);
    }

    public void onMainClick(View view) {

        switch (view.getId()) {
            case R.id.tv_sys_picker:
                ImagePicker.startSystemImagePickerForSingle(this, Q_SYS_IMAGE_REQUEST_CODE);
                break;

            case R.id.tv_sys_muliti_picker:
                container.removeAllViews();
                ImagePicker.startSystemImagePickerForMultiple(this, Q_SYS_MULTIPLE_IMAGE_REQUEST_CODE);
                break;


            case R.id.tv_custom_picker:
                container.removeAllViews();
                ImagePicker.startCustomImagePickerForMultiple(this, Q_CUSTOM_IMAGE_REQUEST_CODE, KEY);
                break;
            case R.id.tv_insert_image:
                if (PermissionHelper.checkStoragePermission(this)) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
                    MediaUtils.insertImageFile(this, "image/jpeg",
                            System.currentTimeMillis() + "te.jpg", "insert image", bitmap);
                    Toast.makeText(this,"insert success",Toast.LENGTH_SHORT).show();
                } else {
                    PermissionHelper.requestStoragePermission(this, 11);
                }
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            /***
             * 单选
             */
            if (requestCode == Q_SYS_IMAGE_REQUEST_CODE) {
                Uri uri = data.getData();
                Log.e(TAG, "single " + uri.toString());
                Glide.with(this).load(uri).into(ivPreview);
            }
            /**
             * 多选
             */
            else if (requestCode == Q_SYS_MULTIPLE_IMAGE_REQUEST_CODE) {
                //选择 多个
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    int count = clipData.getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(60), dp2px(80));
                        container.addView(imageView, params);
                        Glide.with(this).load(uri).into(imageView);
                        Log.e(TAG, "multiple " + uri.toString());
                    }
                } else {
                    //只选择 1个
                    Uri uri = data.getData();
                    Log.e(TAG, "multiple -- " + uri);
                    Glide.with(this).load(uri).into(ivPreview);
                }
            } else if (requestCode == Q_CUSTOM_IMAGE_REQUEST_CODE) {
                ArrayList<String> datas = data.getStringArrayListExtra(KEY);
                if (datas != null) {
                    for (String s : datas) {
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(60), dp2px(80));
                        container.addView(imageView, params);
                        ImageLoader.displayImageView(imageView, Uri.parse(s));
                    }
                }
            }
        }
    }


    int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    void test() {
        if (AndroidQUtils.isAndroidQ()) {
            showToast("Android Q");
            boolean hasPermission = PermissionHelper.checkStoragePermission(this);
            if (hasPermission) {
                showToast("已有权限");
            } else {
                PermissionHelper.requestStoragePermission(this, Q_CUSTOM_IMAGE_REQUEST_CODE);
            }
        } else {
            showToast("Android Q 以下");
        }
    }

}
