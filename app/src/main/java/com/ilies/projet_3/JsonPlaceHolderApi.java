package com.ilies.projet_3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("/3/search/movie")
    Call<MovieSearch> getMovies(@Query("api_key") String api_key,
                               @Query("query") String query,
                               @Query("page") String page);

    @GET("/3/movie/{MOVIE_ID}")
    Call<MovieDetails> getMovieDetails(@Path("MOVIE_ID") int movieID,
                                      @Query("api_key") String api_key);
}