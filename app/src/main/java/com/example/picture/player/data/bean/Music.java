package com.example.picture.player.data.bean;

import com.example.picture.player.util.PinyinUtil;

public class Music implements Comparable<Music> {
    /**歌曲名*/
    private String name;
    /**路径*/
    private String path;
    /**所属专辑*/
    private String album;
    /**艺术家(作者)*/
    private String artist;
    /**文件大小*/
    private long size;
    /**时长*/
    private int duration;
    /**歌曲名的拼音，用于字母排序*/
    private String pinyin;

    public Music(String name, String path, String album, String artist, long size, int duration) {
        this.name = name;
        this.path = path;
        this.album = album;
        this.artist = artist;
        this.size = size;
        this.duration = duration;
        pinyin = PinyinUtil.getPinyin(name);
    }

    @Override
    public int compareTo(Music o) {
        return 0;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public long getSize() {
        return size;
    }

    public int getDuration() {
        return duration;
    }

    public String getPinyin() {
        return pinyin;
    }
}
