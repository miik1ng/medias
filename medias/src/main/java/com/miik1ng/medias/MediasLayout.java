package com.miik1ng.medias;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mi on 2022/5/10
 */
@SuppressLint("NotifyDataSetChanged")
public class MediasLayout extends LinearLayout {

    public static final int TYPE_SHOW = 0;
    public static final int TYPE_ADD = 1;

    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String VIDEO_MP$ = "video/mp4";

    private MediasGridAdapter adapter;
    private PopupWindow pop;
    private Activity activity;

    private List<MediasBean> mediaList = new ArrayList<>();
    private List<MediasBean> originalList = new ArrayList<>();
    private List<MediasBean> newList = new ArrayList<>();
    private OnMediasListener onMediasListener;

    public MediasLayout(Context context) {
        this(context, null);
    }

    public MediasLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediasLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.medias_layout, this);
        activity = (Activity) context;
        RecyclerView recyclerView = findViewById(R.id.rv_medias);
        MediasGridManager manager = new MediasGridManager(activity, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dip2px(activity, 8), false));
        adapter = new MediasGridAdapter(getContext());
        adapter.setOnAddMediasClickListener(onAddMediasClickListener);
        adapter.setOnDeleteClickListener(onDeleteClickListener);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            List<MediasBean> temList = adapter.getData();
            ArrayList<LocalMedia> temData = new ArrayList<>();
            for (MediasBean bean : temList) {
                temData.add(bean.getLocalMedia());
            }

            PictureSelector.create(getContext())
                    .openPreview()
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .startActivityPreview(position, false, temData);
        });
    }

    private MediasGridAdapter.OnAddMediasClickListener onAddMediasClickListener = new MediasGridAdapter.OnAddMediasClickListener() {
        @Override
        public void onClick() {
            showPop();
        }
    };

    private MediasGridAdapter.OnDeleteClickListener onDeleteClickListener = new MediasGridAdapter.OnDeleteClickListener() {
        @Override
        public void onDelete(int position) {
            if (position >= 0 && position < newList.size()) {
                newList.remove(position);
            }
        }
    };

    public void setType(int type) {
        adapter.setLayoutType(type);
        adapter.notifyDataSetChanged();
    }

    public void setData(List<NewMedia> list) {
        if (list == null) return;
        for (NewMedia media : list) {
            LocalMedia localMedia = new LocalMedia();
            localMedia.setMimeType(media.getType());
            localMedia.setPath(media.getPath());
            originalList.add(new MediasBean(localMedia, true));
        }
        tidyData();
        adapter.setData(mediaList);
        adapter.notifyDataSetChanged();
    }

    public List<NewMedia> getData() {
        List<NewMedia> temList = new ArrayList<>();
        for (MediasBean bean : newList) {
            temList.add(new NewMedia(bean.getLocalMedia().getPath(), bean.getLocalMedia().getMimeType()));
        }
        return temList;
    }

    private void tidyData() {
        mediaList.clear();
        mediaList.addAll(originalList);
        mediaList.addAll(newList);
    }

    private void showPop() {
        View view = View.inflate(getContext(), R.layout.pop_bottom, null);
        TextView tvImage = view.findViewById(R.id.pop_image);
        TextView tvVideo = view.findViewById(R.id.pop_video);
        TextView tvCancel = view.findViewById(R.id.pop_cancel);
        pop = new PopupWindow(view, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        activity.getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
                pop = null;
            }
        });
        pop.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tvImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(getContext())
                        .openCamera(SelectMimeType.ofImage())
                        .forResult(new ResultCallback(adapter));

                closePop();
            }
        });
        tvVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(activity)
                        .openCamera(SelectMimeType.ofVideo())
                        .setRecordVideoMaxSecond(10)
                        .setRecordVideoMinSecond(5)
                        .forResult(new ResultCallback(adapter));
                closePop();
            }
        });
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
            }
        });
    }

    private class ResultCallback implements OnResultCallbackListener<LocalMedia> {

        private WeakReference<MediasGridAdapter> adapterWeakReference;

        public ResultCallback(MediasGridAdapter adapter) {
            super();
            this.adapterWeakReference = new WeakReference<>(adapter);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResult(ArrayList<LocalMedia> result) {
            newList.add(new MediasBean(result.get(0), false));
            tidyData();
            if (onMediasListener != null) {
                onMediasListener.setMedias(new NewMedia(result.get(0).getPath(), result.get(0).getMimeType()));
            }

            if (adapterWeakReference.get() != null) {
                adapterWeakReference.get().setData(mediaList);
                adapterWeakReference.get().notifyDataSetChanged();
            }
        }

        @Override
        public void onCancel() {

        }
    }

    private void closePop() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnMediasListener {
        void setMedias(NewMedia newMedia);
    }

    public void setOnMediasListener(OnMediasListener listener) {
        this.onMediasListener = listener;
    }
}
