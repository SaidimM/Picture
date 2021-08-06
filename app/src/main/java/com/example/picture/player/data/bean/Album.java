/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.picture.player.data.bean;

import com.example.picture.base.data.music.BaseAlbumItem;
import com.example.picture.base.data.music.BaseArtistItem;
import com.example.picture.base.data.music.BaseMusicItem;

/**
 * Create by KunMinX at 19/10/31
 */
public class Album extends BaseAlbumItem<Album.Music, Album.Artist> {

    private String albumMid;

    public String getAlbumMid() {
        return albumMid;
    }

    public void setAlbumMid(String albumMid) {
        this.albumMid = albumMid;
    }

    public static class Music extends BaseMusicItem<Artist> {

        private String songMid;

        public String getSongMid() {
            return songMid;
        }

        public void setSongMid(String songMid) {
            this.songMid = songMid;
        }
    }

    public static class Artist extends BaseArtistItem {

        private String birthday;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }
}

