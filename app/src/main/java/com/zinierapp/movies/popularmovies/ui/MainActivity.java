package com.zinierapp.movies.popularmovies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zinierapp.movies.popularmovies.AutoFitGridLayoutManager;
import com.zinierapp.movies.popularmovies.ConnectionDetector;
import com.zinierapp.movies.popularmovies.models.Movies;
import com.zinierapp.movies.popularmovies.adapters.MoviesAdapter;
import com.zinierapp.movies.popularmovies.apiUtils.MoviesFilterType;
import com.zinierapp.movies.popularmovies.viewmodel.MoviesViewModel;
import com.zinierapp.movies.popularmovies.apiUtils.NetworkState;
import com.zinierapp.movies.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.movies_rcv)

    RecyclerView mRecyclerView;
    private MoviesViewModel mMoviesViewModel;

    private MoviesAdapter adapter;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        connectionDetector = new ConnectionDetector(this);
        if(!connectionDetector.isConnectingToInternet()) {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        adapter = new MoviesAdapter(this);

        mMoviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        mMoviesViewModel.getMoviesList().observe(this, new Observer<PagedList<Movies>>() {
            @Override
            public void onChanged(@Nullable PagedList<Movies> movies) {
                Log.d(TAG, "onChanged: "+movies.size());
                adapter.submitList(movies);
            }
        });
        mMoviesViewModel.getNetworkStateLiveData().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                Log.d(TAG, "onChanged: network state changed");
                adapter.setNetworkState(networkState);
            }
        });

        mRecyclerView.setLayoutManager(new AutoFitGridLayoutManager(this,500));
        mRecyclerView.setAdapter(adapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (mMoviesViewModel.getCurrentSorting() == MoviesFilterType.POPULAR) {
            menu.findItem(R.id.action_popular_movies).setChecked(true);
        } else {
            menu.findItem(R.id.action_top_rated).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.menu_sort_group) {
            mMoviesViewModel.setSortMoviesBy(item.getItemId());
            mMoviesViewModel.getMoviesList().observe(this, new Observer<PagedList<Movies>>() {
                @Override
                public void onChanged(@Nullable PagedList<Movies> movies) {
                    Log.d(TAG, "onChanged: onOptionaitemselected"+movies.size());
                    adapter.submitList(movies);
                }
            });
            item.setChecked(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
