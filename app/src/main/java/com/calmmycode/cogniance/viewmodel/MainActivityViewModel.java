package com.calmmycode.cogniance.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import com.calmmycode.cogniance.R;
import com.calmmycode.cogniance.model.data.Repo;
import com.calmmycode.cogniance.model.data.User;

import java.util.ArrayList;

import androidx.lifecycle.AndroidViewModel;
import kotlin.Pair;

public class MainActivityViewModel extends AndroidViewModel {
    private User user;
    private ArrayList<Repo> repos;
    private Application mApplication;

    public MainActivityViewModel(Application application){
        super(application);
        this.mApplication = application;
    }

    public User getUser(){
        return user;
    }

    public ArrayList<Repo> getRepos(){
        return repos;
    }

    public void updateData(Pair<User,ArrayList<Repo>> data){
        this.user = data.getFirst();
        this.repos = data.getSecond();
    }

    public String getUserAndCompany(){
        if(!TextUtils.isEmpty(user.getCompany())){
            return mApplication.getString(R.string.name_company, user.getName(), user.getCompany());
        }else {
            return user.getName();
        }
    }
}
