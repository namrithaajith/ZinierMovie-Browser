package com.zinierapp.movies.popularmovies.apiUtils;

import com.zinierapp.movies.popularmovies.servicegenerator.MovieRetrofitClient;

public class Image {

    public static final String SMALL_IMAGE_SIZE = "w300/";
    public static final String MEDIUM_IMAGE_SIZE = "w700/";
    public static final String BIG_IMAGE_SIZE = "w1280/";

    private String baseUrl = MovieRetrofitClient.IMAGE_BASE_URL;
    private String imageKey;


    public Image(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getLowQualityImagePath(){
        return baseUrl +SMALL_IMAGE_SIZE+imageKey;

    }
    public String getMediumQualityImagePath(){
        return baseUrl +MEDIUM_IMAGE_SIZE+imageKey;

    }
    public String getHighQualityImagePath(){
        return baseUrl +BIG_IMAGE_SIZE+imageKey;
    }
}
