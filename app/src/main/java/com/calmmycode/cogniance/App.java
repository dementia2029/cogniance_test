package com.calmmycode.cogniance;

import android.app.Application;

import com.calmmycode.cogniance.di.AppComponent;
import com.calmmycode.cogniance.di.DaggerAppComponent;
import com.calmmycode.cogniance.di.module.AppContextModule;
import com.calmmycode.cogniance.di.module.RestModule;

public class App extends Application {
    private static App sInstance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        appComponent = DaggerAppComponent.builder()
                .appContextModule(new AppContextModule(this))
                .restModule(new RestModule(Constants.API_BASE_URL))
                .build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }

    public static App get(){
        return sInstance;
    }
}
