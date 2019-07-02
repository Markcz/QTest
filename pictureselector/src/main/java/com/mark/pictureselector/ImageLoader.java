package com.mark.pictureselector;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoader {


    public static void displayImageView(ImageView imageView, String picturePath) {
        RequestOptions options = RequestOptions.centerCropTransform();
        Glide.with(imageView.getContext()).applyDefaultRequestOptions(options).load(picturePath).into(imageView);
    }

    public static void displayImageView(ImageView imageView, Uri uri) {
        RequestOptions options = RequestOptions.centerCropTransform();
        Glide.with(imageView.getContext()).applyDefaultRequestOptions(options).load(uri).into(imageView);
    }

    public static void displayImageViewWithRound(ImageView imageView, String coverPath, int corner) {
        RequestOptions options = new RequestOptions().transform(new CenterCrop(), new RoundedCorners(corner));
        Glide.with(imageView.getContext()).applyDefaultRequestOptions(options).load(coverPath).into(imageView);
    }
}
