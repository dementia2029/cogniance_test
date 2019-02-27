package com.calmmycode.cogniance.di;

import android.app.Application;

import com.calmmycode.cogniance.api.GitHubService;
import com.calmmycode.cogniance.di.module.AppContextModule;
import com.calmmycode.cogniance.di.module.RestModule;
import com.calmmycode.cogniance.ui.fragment.ResultsFragment;
import com.calmmycode.cogniance.ui.fragment.SearchFragment;

import javax.inject.Singleton;

import androidx.fragment.app.Fragment;
import dagger.Component;

@Singleton
@Component (modules = {AppContextModule.class, RestModule.class})
public interface AppComponent {
    Application getApplication();
    GitHubService getApiService();
    void inject(SearchFragment fragment);
    void inject(ResultsFragment fragment);
}
