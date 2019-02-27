package com.calmmycode.cogniance.api;

import com.calmmycode.cogniance.model.data.Repo;
import com.calmmycode.cogniance.model.data.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("/users/{user}")
    Observable<User> getUserObservable(@Path("user") String userName);

    @GET("/users/{user}")
    User getUser(@Path("user") String userName);

    @GET("/users/{user}/repos")
    Observable<ArrayList<Repo>> getUserRepos(@Path("user") String userName);
}
