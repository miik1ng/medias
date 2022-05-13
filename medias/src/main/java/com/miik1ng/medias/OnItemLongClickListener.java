package com.miik1ng.medias;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Mi on 2022/5/10
 */
public interface OnItemLongClickListener {
    void onLongClick(RecyclerView.ViewHolder holder, int position, View view);
}
