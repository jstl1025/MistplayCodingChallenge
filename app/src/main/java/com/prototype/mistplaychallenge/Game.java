package com.prototype.mistplaychallenge;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
    @JsonProperty("genre")
    private String genre;

    @JsonProperty("imgURL")
    private String imgURL;

    @JsonProperty("subGenre")
    private String subGenre;

    @JsonProperty("title")
    private String title;

    @JsonProperty("pid")
    private String pid;

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("rCount")
    private int rCount;

    public String getGenre(){
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(String subGenre) {
        this.subGenre = subGenre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getrCount() {
        return rCount;
    }

    public void setrCount(int rCount) {
        this.rCount = rCount;
    }
}
