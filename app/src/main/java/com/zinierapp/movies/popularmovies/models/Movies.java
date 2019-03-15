package com.zinierapp.movies.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.google.gson.annotations.SerializedName;


public class Movies implements Parcelable {

    @SerializedName("backdrop_path")
    private String backdropImageKey;
    @SerializedName("poster_path")
    private String posterImageKey;
    String title ;
    String original_title ;
    String original_language  ;
    String release_date ;
    String overview ;
    float vote_average ;
    int vote_count ;
    Double popularity;
    boolean adult ;
    int id;
    boolean bIsFavoties ;
    public static final DiffUtil.ItemCallback<Movies> DIFF_CALL = new DiffUtil.ItemCallback<Movies>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movies oldItem, @NonNull Movies newItem) {
            return  oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movies oldItem, @NonNull Movies newItem) {
            return  oldItem.id == newItem.id;
        }

    };


    public Movies(String posterPath)
    {
        posterImageKey = posterPath ;
    }



    //parcel part
    public Movies(Parcel in){
        String[] data= new String[12];

        in.readStringArray(data);

        this.backdropImageKey= data[0];
        this.posterImageKey= data[1];
        this.title= data[2];
        this.original_title= data[3];
        this.original_language= data[4];
        this.release_date= data[5];
        this.overview= data[6];

        this.vote_average= Float.parseFloat(data[7]);
        this.vote_count= Integer.parseInt(data[8]);
        this.popularity= Double.parseDouble(data[9]);
        this.adult= Boolean.parseBoolean(data[10]);

        this.id= Integer.parseInt(data[11]);

        bIsFavoties = false;
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movies other = (Movies) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

        dest.writeStringArray(new String[]{this.backdropImageKey,
                this.posterImageKey,
                this.title,
                this.original_title,
                this.original_language,
                this.release_date,
                this.overview,
                String.valueOf(this.vote_average),
                String.valueOf(this.vote_count),
                String.valueOf(this.popularity),
                String.valueOf(this.adult),
                String.valueOf(this.id)});
    }

    public static final Parcelable.Creator<Movies> CREATOR= new Parcelable.Creator<Movies>() {

        @Override
        public Movies createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Movies(source);  //using parcelable constructor
        }

        @Override
        public Movies[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Movies[size];
        }
    };

    public String getBackdropImageKey() {
        return backdropImageKey;
    }

    public void setBackdropImageKey(String backdropImageKey) {
        this.backdropImageKey = backdropImageKey;
    }

    public String getPosterImageKey() {
        return posterImageKey;
    }

    public void setPosterImageKey(String posterImageKey) {
        this.posterImageKey = posterImageKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
}
