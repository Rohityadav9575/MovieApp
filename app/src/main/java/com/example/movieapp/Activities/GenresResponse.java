package com.example.movieapp.Activities;

import com.example.movieapp.Model.GenresModel;

import java.util.List;

public class GenresResponse {
    private List<GenresModel> genres;

    public List<GenresModel> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresModel> genres) {
        this.genres = genres;
    }
}
