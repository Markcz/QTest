package com.mark.pictureselector.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.pictureselector.AndroidQUtils;
import com.mark.pictureselector.ImageLoader;
import com.mark.pictureselector.ImagePicker;
import com.mark.pictureselector.L;
import com.mark.pictureselector.R;
import com.mark.pictureselector.bean.Folder;
import com.mark.pictureselector.bean.Picture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {


    List<Picture> selectedPictures = new ArrayList<>();

    int currentFolderIndex = 0;
    List<Folder> folderList;

    public PictureAdapter(List<Folder> folders) {
        this.folderList = folders;
    }

    public List<Picture> getSelectedPictures() {
        return selectedPictures;
    }

    public ArrayList<String> getSelectedPicturesPath() {
        ArrayList<String> selectPaths = new ArrayList<>();
        synchronized (selectedPictures) {
            for (Picture selectedPicture : selectedPictures) {
                selectPaths.add(selectedPicture.getPicturePath());
            }
        }
        return selectPaths;
    }

    public int getSelectedPicturesSize() {
        return selectedPictures.size();
    }


    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new PictureViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_select_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PictureViewHolder pictureViewHolder, final int i) {
        final Picture picture = getCurrentPictures().get(i);

        // TODO: 2019-06-28  此处解决viewholder复用问题 性能上有待提高 应该从数据源上修改
        if (selectedPictures.contains(picture)) {
            pictureViewHolder.ivCheck.setSelected(true);
        } else {
            pictureViewHolder.ivCheck.setSelected(false);
        }
        ImageLoader.displayImageView(pictureViewHolder.imageView, picture.getPicturePath());
        pictureViewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    boolean select = pictureViewHolder.ivCheck.isSelected();
                    pictureViewHolder.ivCheck.setSelected(!select);
                    boolean afterSelect = pictureViewHolder.ivCheck.isSelected();
                    if (afterSelect) {
                        selectedPictures.add(picture);
                    } else {
                        if (selectedPictures.contains(picture)) {
                            selectedPictures.remove(picture);
                        }
                    }
                    onItemClickListener.onItemClick(picture.getPicturePath(), i);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String path, int position);
    }

    @Override
    public int getItemCount() {
        return folderList == null ? 0 : getCurrentPictures().size();
    }

    static class PictureViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView, ivCheck;
        public View clickView;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_picture);
            clickView = itemView.findViewById(R.id.v_click);
            ivCheck = itemView.findViewById(R.id.iv_check);
        }
    }

    public void setCurrentFolderIndex(int currentIndex) {
        this.currentFolderIndex = currentIndex;
    }

    public List<Picture> getCurrentPictures() {
        return folderList.get(currentFolderIndex).getPictureList();
    }
}