package com.zinierapp.movies.popularmovies.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zinierapp.movies.popularmovies.models.Movies;
import com.zinierapp.movies.popularmovies.R;
import com.zinierapp.movies.popularmovies.apiUtils.Image;
import com.zinierapp.movies.popularmovies.apiUtils.NetworkState;
import com.zinierapp.movies.popularmovies.ui.MovieDetailActivity;

public class MoviesAdapter extends PagedListAdapter<Movies, RecyclerView.ViewHolder> {
    private static final String TAG = "MoviesAdapter";
    public static final int MOVIE_ITEM_VIEW_TYPE = 1;
    public static final int LOAD_ITEM_VIEW_TYPE  = 0;
    private Context mContext;
    private NetworkState mNetworkState;

    public MoviesAdapter(Context context) {
        super(Movies.DIFF_CALL);
        mContext = context;
    }


    @Override
    public int getItemViewType(int position) {
        return ( isLoadingData() && position == getItemCount()-1 )  ? LOAD_ITEM_VIEW_TYPE : MOVIE_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == MOVIE_ITEM_VIEW_TYPE) {
            view = inflater.inflate(R.layout.movie_item, parent, false);
            return  new MovieViewHolder(view);
        }

        else{
            view = inflater.inflate(R.layout.load_progress_item, parent, false);
            return new ProgressViewHolder(view);
         }


    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Image image;
        if( holder instanceof MovieViewHolder){
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            Movies movie = getItem(position);
            movieViewHolder.bind(movie,mContext);

        }


    }

    public void setNetworkState(NetworkState networkState) {
        //NetworkState prevState = networkState;
        boolean wasLoading = isLoadingData();
        //mNetworkState = networkState;
        boolean willLoad =  isLoadingData();
        if(wasLoading != willLoad){
            if (wasLoading) notifyItemRemoved(getItemCount()); else  notifyItemInserted(getItemCount());
        }
    }

    public boolean isLoadingData(){
        return  ( mNetworkState != null && mNetworkState != NetworkState.LOADED);
    }

    private static class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView movieTitle;
        ImageView moviePosterImageView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movie_title);
            moviePosterImageView = itemView.findViewById(R.id.movie_poster);
        }
        public void bind(final Movies movie, final Context context){
            movieTitle.setText(movie.getTitle());
            Image image = new Image(movie.getPosterImageKey());
            Glide.with(context).load(image.getLowQualityImagePath()).thumbnail(0.1f).into(moviePosterImageView);
            moviePosterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("selectedmovie",movie);
                    context.startActivity(intent);
                }
            });
        }
    }
    private static class ProgressViewHolder extends RecyclerView.ViewHolder{

        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}
