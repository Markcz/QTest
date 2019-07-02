package com.mark.pictureselector.base;

public interface IModel<D> {

    void load(OnLoadListener<D> listener);

    interface OnLoadListener<D> {
        void onLoadComplete(D data);

        void onLoadError(String msg);
    }

}
