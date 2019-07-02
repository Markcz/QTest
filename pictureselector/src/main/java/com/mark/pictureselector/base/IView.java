package com.mark.pictureselector.base;

public interface IView<T> {


    void showProgress();//数据加载中...
    void showError(String msg);//数据加载错误...
    void showEmpty();//数据为空
    void showData(T datas);//数据加载完成


}
