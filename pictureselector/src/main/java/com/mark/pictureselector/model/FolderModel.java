package com.mark.pictureselector.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.animation.AnimationUtils;

import com.mark.pictureselector.AndroidQUtils;
import com.mark.pictureselector.L;
import com.mark.pictureselector.PictureSelector;
import com.mark.pictureselector.base.IModel;
import com.mark.pictureselector.bean.Folder;
import com.mark.pictureselector.bean.Picture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderModel<D> implements IModel<D> {


    static final int INDEX_ALL_PHOTOS = 0;

    static final String[] projection = new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_MODIFIED,
    };

    static final String[] qprojection = new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_MODIFIED,
    };

    static final String selection = MediaStore.Images.ImageColumns.MIME_TYPE + "=?"
            + " OR "
            + MediaStore.Images.ImageColumns.MIME_TYPE + "=?"
            + " AND " + MediaStore.Images.ImageColumns.SIZE + ">0";

    static final String[] selectionArgs = new String[]{
            "image/png", "image/jpeg"
    };

    static final String order = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC";


    @Override
    public void load(OnLoadListener<D> listener) {
        if (AndroidQUtils.isAndroidQ()) {
            loadQ(listener);
        } else {
            loadBeforeQ(listener);
        }
    }

    private void loadQ(OnLoadListener<D> listener) {
        ContentResolver contentResolver = PictureSelector.getContext().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                order);
        if (cursor == null) {
            listener.onLoadError("获取图片失败");
        } else {
            List<Folder> folders = new ArrayList<>();

            Folder photoAll = new Folder();
            photoAll.setFolderName("所有图片");
            photoAll.setId("ALL");

            while (cursor.moveToNext()) {
                long imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID));
                String bucketId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));

                //String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));

                Uri photoUri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + File.separator + imageId);


                L.e("FolderModel Q ", photoUri.toString());

                String uriString = photoUri.toString();

                Folder folder = new Folder();
                folder.setId(bucketId);
                folder.setFolderName(name);
                if (!folders.contains(folder)) {
                    folder.setCoverPath(uriString);
                    folder.addPicture(new Picture(imageId, uriString));
                    folders.add(folder);
                } else {
                    folders.get(folders.indexOf(folder)).addPicture(new Picture(imageId, uriString));
                }
                photoAll.addPicture(new Picture(imageId, uriString));
            }
            if (photoAll.getPictureCount() > 0) {
                photoAll.setCoverPath(photoAll.getPictureList().get(0).getPicturePath());
            }
            folders.add(INDEX_ALL_PHOTOS, photoAll);
            listener.onLoadComplete((D) folders);
        }
    }


    private void loadBeforeQ(OnLoadListener<D> listener) {
        ContentResolver contentResolver = PictureSelector.getContext().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                order);
        if (cursor == null) {
            listener.onLoadError("获取图片失败");
        } else {
            List<Folder> folders = new ArrayList<>();

            Folder photoAll = new Folder();
            photoAll.setFolderName("所有图片");
            photoAll.setId("ALL");

            while (cursor.moveToNext()) {
                long imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID));
                String bucketId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));


                L.e("FolderModel ", path);

                Folder folder = new Folder();
                folder.setId(bucketId);
                folder.setFolderName(name);
                if (!folders.contains(folder)) {
                    folder.setCoverPath(path);
                    folder.addPicture(new Picture(imageId, path));
                    folders.add(folder);
                } else {
                    folders.get(folders.indexOf(folder)).addPicture(new Picture(imageId, path));
                }
                photoAll.addPicture(new Picture(imageId, path));
            }
            if (photoAll.getPictureCount() > 0) {
                photoAll.setCoverPath(photoAll.getPictureList().get(0).getPicturePath());
            }
            folders.add(INDEX_ALL_PHOTOS, photoAll);
            listener.onLoadComplete((D) folders);
        }

    }


}
