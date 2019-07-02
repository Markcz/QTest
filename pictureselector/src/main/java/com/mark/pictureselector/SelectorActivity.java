package com.mark.pictureselector;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.pictureselector.adapter.PictureAdapter;
import com.mark.pictureselector.adapter.PopupFolderListAdapter;
import com.mark.pictureselector.base.IView;
import com.mark.pictureselector.base.PBaseActivity;
import com.mark.pictureselector.bean.Folder;
import com.mark.pictureselector.decoration.ThreeSpanGridItemDecoration;
import com.mark.pictureselector.presenter.FolderPresenter;

import java.util.ArrayList;
import java.util.List;

public class SelectorActivity extends PBaseActivity<IView, FolderPresenter<IView>> implements IView<List<Folder>>, View.OnClickListener {


    static final String TAG = "SelectorActivity";

    static final int STORAGE_PERMISSION_REQUEST_CODE = 1;


    static String aKey = null;

    ImageView ivBack, ivArrow;
    TextView tvTitle, tvCompleAndSize;
    RecyclerView selectorRecyclerView;
    ListPopupWindow listPopupWindow;

    List<Folder> folderList = new ArrayList<>();
    PictureAdapter mAdapter;
    PopupFolderListAdapter mFolderAdapter;


    public static void start(Activity activity, int requestCode, String key) {
        aKey = key;
        Intent intent = new Intent(activity, SelectorActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PermissionHelper.checkStoragePermission(this)) {
            super.onCreate(savedInstanceState);
        } else {
            PermissionHelper.requestStoragePermission(this, STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initLayout();
            initViews();
            initOthers();
        }
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_selector);
    }

    @Override
    protected void initViews() {
        tvCompleAndSize = findViewById(R.id.tv_complete_and_size);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivArrow = findViewById(R.id.iv_arrow);
        selectorRecyclerView = findViewById(R.id.selector_recycler_view);
    }

    @Override
    protected FolderPresenter<IView> createPresenter() {
        return new FolderPresenter();
    }

    @Override
    protected void initOthers() {
        super.initOthers();
        ivBack.setOnClickListener(this);
        tvCompleAndSize.setVisibility(View.GONE);
        tvCompleAndSize.setOnClickListener(this);
        //picture
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        selectorRecyclerView.setLayoutManager(layoutManager);
        selectorRecyclerView.setItemAnimator(new DefaultItemAnimator());
        int margin = PictureSelector.dp2px(5);
        selectorRecyclerView.addItemDecoration(new ThreeSpanGridItemDecoration(margin));
        mAdapter = new PictureAdapter(folderList);
        selectorRecyclerView.setAdapter(mAdapter);
        presenter.startScan();

        mAdapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String path, int position) {
                //item 点击
                int count = mAdapter.getSelectedPicturesSize();
                if (count > 0) {
                    tvCompleAndSize.setVisibility(View.VISIBLE);
                    tvCompleAndSize.setText(String.format("完成(%d)", count));
                } else {
                    tvCompleAndSize.setVisibility(View.GONE);
                }
            }
        });

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleListPopupWindow();
            }
        });
    }

    @Override
    public void showProgress() {
        //PictureSelector.showToast("加载中...");
    }

    @Override
    public void showError(String msg) {
        PictureSelector.showToast(msg);
    }

    @Override
    public void showEmpty() {
        PictureSelector.showToast("数据为空");
    }

    @Override
    public void showData(List<Folder> datas) {
        folderList.clear();
        folderList.addAll(datas);
        mAdapter.notifyDataSetChanged();

        // folder列表弹窗
        initPopupFolderWindow();
    }

    private void initPopupFolderWindow() {
        mFolderAdapter = new PopupFolderListAdapter(this, folderList);
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        listPopupWindow.setAnchorView(tvTitle);
        listPopupWindow.setAdapter(mFolderAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        listPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        listPopupWindow.setHeight(PictureSelector.dp2px(320));

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideListPopupWindow();
                int size = folderList.size();
                if (position >= 0 && position < size) {
                    Folder folder = folderList.get(position);
                    tvTitle.setText(folder.getFolderName());
                    mAdapter.setCurrentFolderIndex(position);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivArrow.setRotation(0);
            }
        });
    }

    void toggleListPopupWindow() {
        if (listPopupWindow != null) {
            if (listPopupWindow.isShowing()) {
                hideListPopupWindow();
            } else {
                showListPopupWindow();
            }
        }
    }

    void hideListPopupWindow() {
        if (!isFinishing() && listPopupWindow != null) {
            listPopupWindow.dismiss();
        }
    }

    void showListPopupWindow() {
        if (!isFinishing() && listPopupWindow != null) {
            listPopupWindow.show();
            listPopupWindow.getListView().setDivider(PictureSelector.getDrawable(R.drawable.h_divider));
            listPopupWindow.getListView().setDividerHeight(1);
            ivArrow.setRotation(180);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.anim_activity_close_push_down);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.tv_complete_and_size) {
            //数据返回
            if (aKey == null) {
                L.e(TAG, "aKey == null");
                return;
            }
            if (mAdapter == null) {
                L.e(TAG, "mAdapter == null");
                return;
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(aKey, mAdapter.getSelectedPicturesPath());
            setResult(RESULT_OK, intent);
            finish();
        }
    }


}
