package com.example.picture.base.data.music;

import java.io.Serializable;
import java.util.List;

public class BaseAlbumItem<M extends BaseMusicItem<A>, A extends BaseArtistItem> implements Serializable {
    private String albumId;
    private String title;
    private String summary;
    private A artist;
    private String coverImg;
    private List<M> musics;

    public BaseAlbumItem() {
    }

    public BaseAlbumItem(String albumId, String title, String summary, A artist, String coverImg, List<M> musics) {
        this.albumId = albumId;
        this.title = title;
        this.summary = summary;
        this.artist = artist;
        this.coverImg = coverImg;
        this.musics = musics;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public A getArtist() {
        return this.artist;
    }

    public void setArtist(A artist) {
        this.artist = artist;
    }

    public String getCoverImg() {
        return this.coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public List<M> getMusics() {
        return this.musics;
    }

    public void setMusics(List<M> musics) {
        this.musics = musics;
    }
}
