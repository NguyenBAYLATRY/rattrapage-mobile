package com.ilies.projet_3;

public class MovieDetails {
    private String original_title;
    private String title;
    private String poster_path;
    private String overview;

    public String getOriginal_title() {
        return original_title;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return "https://image.tmdb.org/t/p/w500/" + poster_path;
    }

    public String getOverview() {
        return overview;
    }
}
