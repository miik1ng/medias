package com.miik1ng.medias;

/**
 * Created by Mi on 2022/5/12
 */
public class NewMedia {
    private String path;
    private String type;

    public NewMedia() {
    }

    public NewMedia(String path, String type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
