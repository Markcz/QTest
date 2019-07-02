package com.mark.pictureselector.base;

import java.lang.ref.WeakReference;

public class PBasePresenter<V> {

    protected WeakReference<V> mViewRef;


    protected void attach(V v){
        mViewRef = new WeakReference<>(v);
    }

    protected void detach(){
        mViewRef.clear();
    }


}
