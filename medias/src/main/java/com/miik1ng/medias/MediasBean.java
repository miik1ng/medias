package com.miik1ng.medias;

import com.luck.picture.lib.entity.LocalMedia;

/**
 * Created by Mi on 2022/5/12
 */
public class MediasBean {
    private LocalMedia localMedia;
    private boolean original;

    public MediasBean() {
    }

    public MediasBean(LocalMedia localMedia, boolean original) {
        this.localMedia = localMedia;
        this.original = original;
    }

    public LocalMedia getLocalMedia() {
        return localMedia;
    }

    public void setLocalMedia(LocalMedia localMedia) {
        this.localMedia = localMedia;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }
}
