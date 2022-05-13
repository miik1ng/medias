package com.miik1ng.medias;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnItemClickListener;
import com.luck.picture.lib.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mi on 2022/5/10
 */
public class MediasGridAdapter extends RecyclerView.Adapter<MediasGridAdapter.ViewHolder> {

    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_MEDIAS = 2;
    private LayoutInflater mInflater;
    private List<MediasBean> list = new ArrayList<>();
    private int layoutType = MediasLayout.TYPE_SHOW;
    private int originalSize = 0;

    public MediasGridAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        if (layoutType == MediasLayout.TYPE_SHOW) {
            return list.size();
        } else {
            return list.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_MEDIAS;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gv_filter_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            holder.mImg.setImageResource(R.drawable.ic_add_image);
            holder.mImg.setOnClickListener(v -> {
                if (onAddMediasClickListener != null) {
                    onAddMediasClickListener.onClick();
                }
            });
            holder.mIvDel.setVisibility(View.GONE);
        } else {
            MediasBean mediasBean = list.get(position);
            holder.mIvDel.setVisibility(layoutType == MediasLayout.TYPE_ADD && !mediasBean.isOriginal() ? View.VISIBLE : View.GONE);
            holder.mIvDel.setOnClickListener(v -> {
                int index = holder.getAdapterPosition();
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                if (index != RecyclerView.NO_POSITION && list.size() > index) {
                    list.remove(index);
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDelete(index - originalSize);
                    }
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index, list.size());
                }
            });
            LocalMedia media = list.get(position).getLocalMedia();
            if (media == null || TextUtils.isEmpty(media.getPath())) {
                return;
            }
            String path;
            if (media.isCut() && !media.isCompressed()) {
                //剪裁过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                //压缩过，或者剪裁同时压缩过，姨最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                //原图
                path = media.getPath();
            }

            long duration = media.getDuration();
            holder.tvDuration.setVisibility(PictureMimeType.isHasVideo(media.getMimeType())
                    ? View.VISIBLE : View.GONE);
            holder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.picture_icon_video, 0, 0, 0);
            holder.tvDuration.setText(DateUtils.formatDurationTime(duration));
            Glide.with(holder.itemView.getContext())
                    .load(PictureMimeType.isContent(path) && !media.isCut() && !media.isCompressed() ? Uri.parse(path) : path)
//                    .centerCrop()
//                    .placeholder(R.color.app_color_f6)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImg);
            //itemView的点击事件
            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> {
                    int adapterPosition = holder.getAdapterPosition();
                    mItemClickListener.onItemClick(v, adapterPosition);
                });
            }

            if (mItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(v -> {
                    int adapterPosition = holder.getAdapterPosition();
                    mItemLongClickListener.onLongClick(holder, adapterPosition, v);
                    return true;
                });
            }
        }
    }

    private void setOriginalSize() {
        originalSize = 0;
        for (MediasBean bean : list) {
            if (bean.isOriginal()) {
                originalSize++;
            }
        }
    }

    private boolean isAddItem(int position) {
        int size = list.size();
        return position == size;
    }

    public void setData(List<MediasBean> list) {
        this.list = list;
        setOriginalSize();
    }

    public List<MediasBean> getData() {
        return list == null ? new ArrayList<>() : list;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        ImageView mIvDel;
        TextView tvDuration;

        public ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.fiv);
            mIvDel = view.findViewById(R.id.iv_del);
            tvDuration = view.findViewById(R.id.tv_duration);
        }
    }

    private OnAddMediasClickListener onAddMediasClickListener;

    public interface OnAddMediasClickListener {
        void onClick();
    }

    public void setOnAddMediasClickListener(OnAddMediasClickListener listener) {
        this.onAddMediasClickListener = listener;
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }

    private OnItemLongClickListener mItemLongClickListener;

    public void setItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
}
