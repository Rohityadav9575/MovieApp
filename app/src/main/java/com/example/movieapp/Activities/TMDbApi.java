package com.example.movieapp.Activities;

import com.example.movieapp.Model.FilmDetails;
import com.example.movieapp.Model.FlimList;
import com.example.movieapp.Model.GenresModel;
import com.example.movieapp.Model.ImageFetch;
import com.example.movieapp.Model.Result;
import com.example.movieapp.Model.VideoModel;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {


    @GET("movie/popular")
    Call<FlimList> getBestMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<FlimList> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<FilmDetails> getMovieDetail(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );
    @GET("movie/{movie_id}/images")
    Call<ImageFetch> getMovieImages(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );
    @GET("genre/movie/list")
    Call<GenresResponse> getCategories(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<VideoModel> getMovieTrailerKey(@Path("movie_id") int movieId,
                                        @Query("api_key") String apiKey);

}
