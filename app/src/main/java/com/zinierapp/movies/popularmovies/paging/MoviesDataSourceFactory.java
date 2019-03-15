package com.zinierapp.movies.popularmovies.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.zinierapp.movies.popularmovies.apiUtils.MoviesFilterType;
import com.zinierapp.movies.popularmovies.apiUtils.MovieRetrofitApi;

import java.util.concurrent.Executor;

public class MoviesDataSourceFactory extends DataSource.Factory{
    MoviesDataSource moviesDataSource;
    MutableLiveData<MoviesDataSource> mutableLiveData;
    Executor executor;
    MovieRetrofitApi movieRetrofitApi;
    //MutableLiveData<MoviesFilterType> sort_by;
    MoviesFilterType sort_by;

    public MoviesDataSourceFactory(Executor executor, MovieRetrofitApi retrofitApi,MoviesFilterType sort_by ) {
        this.movieRetrofitApi = retrofitApi;
        this.executor = executor;
        this.sort_by = sort_by;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        moviesDataSource = new MoviesDataSource(movieRetrofitApi,executor,sort_by);
        mutableLiveData.postValue(moviesDataSource);
        return moviesDataSource;
    }

    public MutableLiveData<MoviesDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
