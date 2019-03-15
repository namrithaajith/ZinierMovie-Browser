package com.zinierapp.movies.popularmovies.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zinierapp.movies.popularmovies.servicegenerator.MovieRetrofitClient;
import com.zinierapp.movies.popularmovies.models.Movies;
import com.zinierapp.movies.popularmovies.apiUtils.MoviesFilterType;
import com.zinierapp.movies.popularmovies.apiUtils.JSONParser;
import com.zinierapp.movies.popularmovies.apiUtils.MovieRetrofitApi;
import com.zinierapp.movies.popularmovies.apiUtils.NetworkState;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDataSource extends PageKeyedDataSource<Long, Movies> {
    private  static final String TAG = MoviesDataSource.class.getSimpleName();

    private MovieRetrofitApi movieRetrofitApi;
    private Executor retryExecutor;
    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    //private MutableLiveData<MoviesFilterType> sortBy;
    private MoviesFilterType sortBy;

    public MoviesDataSource(MovieRetrofitApi movieRetrofitApi, Executor retryExecutor,MoviesFilterType sortBy) {
        this.movieRetrofitApi = movieRetrofitApi;
        this.retryExecutor = retryExecutor;
        networkState = new MutableLiveData<>();
        initialLoading = new MutableLiveData<>();
        this.sortBy = sortBy;
    }
    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {

        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Movies> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        Call<ResponseBody> request;
        if (sortBy == MoviesFilterType.POPULAR) {
            request = movieRetrofitApi.getMovies(MovieRetrofitClient.API_DEFAULT_PAGE_KEY);
        } else {
            request = movieRetrofitApi.getTopRatedMovies(MovieRetrofitClient.API_DEFAULT_PAGE_KEY);
        }

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseString;
                List<Movies> moviesList;
                if (response.isSuccessful() && response.code() ==200) {
                    try {
                        initialLoading.postValue(NetworkState.LOADING);
                        networkState.postValue(NetworkState.LOADED);
                        responseString = response.body().string();
                        moviesList = JSONParser.getMovieList(responseString);
                        callback.onResult(moviesList,null, (long) 2);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED,response.message()));
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED,response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e(TAG, "onFailure: "+errorMessage );

                networkState.postValue(new NetworkState(NetworkState.Status.FAILED,errorMessage));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movies> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Movies> callback) {
        networkState.postValue(NetworkState.LOADING);
        movieRetrofitApi.getMovies(params.key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject responseJson;
                String responseString;
                List<Movies> moviesList;
                Long nextKey;

                if (response.isSuccessful() && response.code() ==200) {
                    try {
                        initialLoading.postValue(NetworkState.LOADING);
                        networkState.postValue(NetworkState.LOADED);

                        responseString = response.body().string();
                        moviesList = JSONParser.getMovieList(responseString);

                        responseJson = new JSONObject(responseString);
                        nextKey = (params.key == responseJson.getInt("total_pages")) ? null : params.key+1;

                        callback.onResult(moviesList, nextKey);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.e(TAG, "onResponse error "+response.message());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED,response.message()));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e(TAG, "onFailure: "+errorMessage );
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED,errorMessage));
            }
        });
    }



}


