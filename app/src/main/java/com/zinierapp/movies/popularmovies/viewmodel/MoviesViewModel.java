package com.zinierapp.movies.popularmovies.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import com.zinierapp.movies.popularmovies.servicegenerator.MovieRetrofitClient;
import com.zinierapp.movies.popularmovies.apiUtils.MoviesFilterType;
import com.zinierapp.movies.popularmovies.R;
import com.zinierapp.movies.popularmovies.apiUtils.MovieRetrofitApi;
import com.zinierapp.movies.popularmovies.apiUtils.NetworkState;
import com.zinierapp.movies.popularmovies.models.Movies;
import com.zinierapp.movies.popularmovies.paging.MoviesDataSource;
import com.zinierapp.movies.popularmovies.paging.MoviesDataSourceFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MoviesViewModel extends ViewModel {

    private static final String TAG = MoviesViewModel.class.getSimpleName();
    private static final int PAGE_SIZE = 20;
    private static final int INITIAL_LOADSIZE = 50;
    private LiveData<PagedList<Movies>> moviesList;
    private LiveData<NetworkState> networkStateLiveData;
    private Executor executor;
    private LiveData<MoviesDataSource> dataSource;
    private MutableLiveData<MoviesFilterType> sortBy = new MutableLiveData<>();

    public MoviesViewModel() {
        executor = Executors.newFixedThreadPool(5);
        MovieRetrofitApi retrofitApi = MovieRetrofitClient.createService(MovieRetrofitApi.class);
        sortBy.setValue(MoviesFilterType.POPULAR);
        MoviesDataSourceFactory factory = new MoviesDataSourceFactory(executor,retrofitApi,getCurrentSorting());
        dataSource =  factory.getMutableLiveData();

        networkStateLiveData = Transformations.switchMap(factory.getMutableLiveData(), new Function<MoviesDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(MoviesDataSource source) {
                Log.d(TAG, "apply: network change");
                return source.getNetworkState();
            }
        });

        PagedList.Config pageConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(INITIAL_LOADSIZE)
                .setPageSize(PAGE_SIZE).build();

        moviesList = (new LivePagedListBuilder<Long, Movies>(factory,pageConfig))
                .setBackgroundThreadExecutor(executor)
                .build();
    }
    public LiveData<PagedList<Movies>> getMoviesList() {
        Log.d(TAG, "getMoviesList: "+moviesList);
        return moviesList;
    }
    public MoviesFilterType getCurrentSorting() {
        return sortBy.getValue();
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

    public void setSortMoviesBy(int id) {
        MoviesFilterType sort = null;
        switch (id) {
            case R.id.action_popular_movies: {
                if (sortBy.getValue() == MoviesFilterType.POPULAR)
                    return;

                sort = MoviesFilterType.POPULAR;
                break;
            }
            case R.id.action_top_rated: {
                if (sortBy.getValue() == MoviesFilterType.TOP_RATED){
                    return;
                }

                sort = MoviesFilterType.TOP_RATED;
                break;
            }
        }
        sortBy.setValue(sort);
    }

}
