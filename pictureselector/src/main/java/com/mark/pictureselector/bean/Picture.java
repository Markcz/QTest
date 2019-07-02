package com.mark.pictureselector.bean;

public class Picture {

    private long id;
    private String picturePath;

    public Picture() {
    }

    public Picture(long id, String picturePath) {
        this.id = id;
        this.picturePath = picturePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Picture)) {
            return false;
        }
        Picture photo = (Picture) o;

        return id == photo.id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
