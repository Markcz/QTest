package com.mark.pictureselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mark.pictureselector.ImageLoader;
import com.mark.pictureselector.L;
import com.mark.pictureselector.R;
import com.mark.pictureselector.bean.Folder;
import java.util.List;

public class PopupFolderListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Folder> folders;

    public PopupFolderListAdapter(Context context, List<Folder> folders) {
        this.context = context;
        this.folders = folders;
        this.mLayoutInflater = LayoutInflater.from(context);
        L.e("Popup", "size " + folders.size());
    }

    @Override
    public int getCount() {
        return folders == null ? 0 : folders.size();
    }

    @Override
    public Object getItem(int position) {
        return folders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_folder, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bindData(folders.get(position));
        return convertView;
    }

    private static class ViewHolder {

        ImageView ivCover, ivSelect;
        TextView tvFolderName, tvFolderChildCount;

        public ViewHolder(View itemView) {
            ivCover = itemView.findViewById(R.id.iv_folder_cover);
            ivSelect = itemView.findViewById(R.id.iv_select);
            tvFolderName = itemView.findViewById(R.id.tv_folder_name);
            tvFolderChildCount = itemView.findViewById(R.id.tv_folder_child_count);
        }

        public void bindData(Folder folder) {
            ImageLoader.displayImageViewWithRound(ivCover, folder.getCoverPath(),10);
            tvFolderName.setText(folder.getFolderName());
            tvFolderChildCount.setText(String.valueOf(folder.getPictureCount()));
            //dirNum.setText(String.valueOf(directory.getPhotoNums()));
        }
    }

}
