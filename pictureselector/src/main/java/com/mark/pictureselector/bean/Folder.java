package com.mark.pictureselector.bean;

import java.util.ArrayList;
import java.util.List;

public class Folder {

    private String id;
    private String folderName;
    private String coverPath;
    private List<Picture> pictureList = new ArrayList<>();

    public Folder() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Folder)) {
            return false;
        }
        Folder directory = (Folder) obj;
        if (!id.equals(directory.id)) {
            return false;
        }
        return folderName.equals(directory.folderName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + folderName.hashCode();
        return result;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<Picture> getPictureList() {
        return pictureList;
    }

    public void addPicture(Picture picture) {
        this.pictureList.add(picture);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public int getPictureCount() {
        return null == pictureList ? 0 : pictureList.size();
    }
}
