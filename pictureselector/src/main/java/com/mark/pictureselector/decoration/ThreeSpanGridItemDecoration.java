package com.mark.pictureselector.decoration;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ThreeSpanGridItemDecoration extends RecyclerView.ItemDecoration {


    static final int spanCount = 3;

    private int spacing;


    public ThreeSpanGridItemDecoration(int margin) {
        this.spacing = margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        if (position < spanCount) {
            outRect.top = spacing;
        }
        outRect.bottom = spacing;
        if (column == 0) {
            outRect.left = spacing;
            outRect.right = spacing / spanCount;
        } else if (column == 2) {
            outRect.left = spacing / spanCount;
            outRect.right = spacing;
        } else {
            outRect.left = 2 * spacing / spanCount;
            outRect.right = 2 * spacing / spanCount;
        }
    }
}

