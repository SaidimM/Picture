package com.example.picture.player.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Mp3Info implements Parcelable {
 
	private long id;
	private String title;
	private String artist;
	private String album;
	private long albumId;
	private long duration;
	private long size;
	private String url;
	private int isPlaying;

	public Mp3Info(){

	}

	public Mp3Info(Parcel parcel){
		id = parcel.readLong();
		title = parcel.readString();
		artist = parcel.readString();
		album = parcel.readString();
		albumId = parcel.readLong();
		duration = parcel.readLong();
		size = parcel.readLong();
		url = parcel.readString();
		isPlaying = parcel.readInt();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public int getDuration() {
		return (int)duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsPlaying() {
		return isPlaying;
	}

	public void setIsPlaying(int isPlaying) {
		this.isPlaying = isPlaying;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(title);
		dest.writeString(artist);
		dest.writeString(album);
		dest.writeLong(albumId);
		dest.writeLong(duration);
		dest.writeLong(size);
		dest.writeString(url);
		dest.writeInt(isPlaying);
	}
	
	public static final Parcelable.Creator<Mp3Info> CREATOR = new Creator<Mp3Info>() {
		
		@Override
		public Mp3Info[] newArray(int size) {
			return new Mp3Info[size];
		}
		
		@Override
		public Mp3Info createFromParcel(Parcel source) {
			return new Mp3Info(source);
		}
	};
	
 
}