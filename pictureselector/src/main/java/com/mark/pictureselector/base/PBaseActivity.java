package com.mark.pictureselector.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class PBaseActivity<V, P extends PBasePresenter<V>> extends AppCompatActivity {

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initViews();
        initOthers();
    }

    protected abstract void initLayout();

    protected abstract void initViews();

    protected P createPresenter() {
        return null;
    }

    protected void initOthers() {
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attach((V) this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }

}
