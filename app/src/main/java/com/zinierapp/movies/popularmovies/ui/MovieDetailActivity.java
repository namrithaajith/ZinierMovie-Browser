package com.zinierapp.movies.popularmovies.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zinierapp.movies.popularmovies.apiUtils.Image;
import com.zinierapp.movies.popularmovies.models.Movies;
import com.zinierapp.movies.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG = MovieDetailActivity.class.getSimpleName();

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.backdrop)
    ImageView backDrop_image;

    @BindView(R.id.movie_poster)
    ImageView poster_image;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.movie_title)
    TextView tVmovieTitle;

    @BindView(R.id.movie_releasedate)
    TextView tVmoviereleaseDate;

    @BindView(R.id.movie_overview)
    TextView tVmovieOverview;

    @BindView(R.id.movie_vote_average)
    TextView tVmovievoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Movies movie = getIntent().getParcelableExtra("selectedmovie");
        String movie_title = movie.getTitle();

        String movie_originalTitle = movie.getOriginal_title();
        String movie_overview = movie.getOverview();
        String user_rating = String.valueOf(movie.getVote_average());
        String movie_releaseDate = movie.getRelease_date();

        collapsingToolbar.setTitle(movie_title);

        String movie_posterImage = movie.getPosterImageKey();
        String movie_backdropImage = movie.getBackdropImageKey();

        if(movie_backdropImage!=null){
            Image image = new Image(movie_backdropImage);
            Glide.with(this).load(image.getHighQualityImagePath()).into(backDrop_image);
        }
        if(movie_posterImage!=null){
            Image image = new Image(movie_posterImage);
            Glide.with(this).load(image.getHighQualityImagePath()).into(poster_image);
        }
        tVmovieTitle.setText(movie_originalTitle);
        tVmoviereleaseDate.setText(movie.getRelease_date());
        tVmovieOverview.setText(movie.getOverview());
        tVmovievoteAverage.setText(String.valueOf(movie.getVote_average()));

    }
}
