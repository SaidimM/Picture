//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.picture.base.data.music;

import java.io.Serializable;

public class BaseMusicItem<A extends BaseArtistItem> implements Serializable {
    private String musicId;
    private String coverImg;
    private String url;
    private String title;
    private A artist;

    public BaseMusicItem() {
    }

    public BaseMusicItem(String musicId, String coverImg, String url, String title, A artist) {
        this.musicId = musicId;
        this.coverImg = coverImg;
        this.url = url;
        this.title = title;
        this.artist = artist;
    }

    public String getMusicId() {
        return this.musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getCoverImg() {
        return this.coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public A getArtist() {
        return this.artist;
    }

    public void setArtist(A artist) {
        this.artist = artist;
    }
}
