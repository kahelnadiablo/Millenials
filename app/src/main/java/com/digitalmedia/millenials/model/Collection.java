package com.digitalmedia.millenials.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Digital Media on 9/19/2015.
 */
public class Collection {
    @SerializedName("artist") @Getter private String artist;
    @SerializedName("title") @Getter private String title;
    @SerializedName("lyrics") @Getter private String lyrics;
    @SerializedName("image_link") @Getter private String image_link;
    @SerializedName("note") @Getter private String note;
    @SerializedName("path") @Getter private String path;
}
