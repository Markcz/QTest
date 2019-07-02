package com.mark.pictureselector.presenter;

import com.mark.pictureselector.base.IModel;
import com.mark.pictureselector.base.IView;
import com.mark.pictureselector.base.PBasePresenter;
import com.mark.pictureselector.bean.Folder;
import com.mark.pictureselector.model.FolderModel;

import java.util.List;

public class FolderPresenter<V extends IView> extends PBasePresenter<V> {

    IModel model = new FolderModel();

    public FolderPresenter() {

    }

    //执行实际操作
    public void startScan() {
        if (mViewRef.get() != null){
            mViewRef.get().showProgress();
            if (model != null){
                model.load(new IModel.OnLoadListener<List<Folder>>() {

                    @Override
                    public void onLoadComplete(List<Folder> data) {
                        if (mViewRef.get() != null){
                            if (data.size() > 0){
                                mViewRef.get().showData(data);
                            }else {
                                mViewRef.get().showEmpty();
                            }
                        }
                    }
                    @Override
                    public void onLoadError(String errorMsg) {
                        if (mViewRef.get() != null){
                            mViewRef.get().showError(errorMsg);
                        }
                    }
                });
            }
        }
    }
}
