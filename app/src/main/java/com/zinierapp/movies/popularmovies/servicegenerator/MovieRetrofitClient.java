package com.zinierapp.movies.popularmovies.servicegenerator;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRetrofitClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL ="https://image.tmdb.org/t/p/";
    public static final int API_DEFAULT_PAGE_KEY = 1;

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Interceptor apiKeyInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            HttpUrl originalHttpUrl = originalRequest.url();

            HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("api_key","57dbd6fccd5a35c73800d7f2da96ce76")
                    .build();
            Request request = originalRequest.newBuilder().url(url).build();
            return chain.proceed(request);
        }
    };

    public static <S> S createService(Class<S> movieRetrofitClient){
        if(!httpClient.interceptors().contains(logging)){
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        if (!httpClient.interceptors().contains(apiKeyInterceptor)){
            httpClient.addInterceptor(apiKeyInterceptor);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return  retrofit.create(movieRetrofitClient);
    }
}
