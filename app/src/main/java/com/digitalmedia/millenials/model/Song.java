package com.digitalmedia.millenials.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Digital Media on 9/19/2015.
 */
public class Song {
    @SerializedName("title") @Getter private String title;
    @SerializedName("artist") @Getter private String artist;
    @SerializedName("note") @Getter private String note;
    @SerializedName("lyrics") @Getter private String lyrics;
    @SerializedName("link") @Getter private String link;
}
