package com.calmmycode.cogniance.di.module;

import com.calmmycode.cogniance.api.GitHubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RestModule {

    private String baseUrl;

    public RestModule(String baseUrl){
        this.baseUrl = baseUrl;
    }

    @Singleton
    @Provides
    GitHubService getRestApi(Retrofit retrofit){
        return retrofit.create(GitHubService.class);
    }

    @Provides
    Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
