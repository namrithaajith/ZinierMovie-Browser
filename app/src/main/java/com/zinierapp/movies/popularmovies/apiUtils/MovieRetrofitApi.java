package com.zinierapp.movies.popularmovies.apiUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieRetrofitApi{

    @GET("movie/popular")
    Call<ResponseBody> getMovies(@Query("page") long pageNumber);

    @GET("movie/top_rated")
    Call<ResponseBody> getTopRatedMovies(@Query("page") long pageNumber);

}