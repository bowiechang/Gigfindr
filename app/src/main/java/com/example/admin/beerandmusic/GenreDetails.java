package com.example.admin.beerandmusic;

/**
 * Created by admin on 7/6/17.
 */

public class GenreDetails {

    private String genre;
    private int photo;

    public GenreDetails(String genre, int photo){
        this.genre = genre;
        this.photo = photo;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
