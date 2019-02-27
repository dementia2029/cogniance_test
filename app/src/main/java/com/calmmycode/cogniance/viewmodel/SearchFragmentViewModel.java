package com.calmmycode.cogniance.viewmodel;

import android.text.TextUtils;
import android.widget.Toast;

import com.calmmycode.cogniance.R;
import com.calmmycode.cogniance.di.AppComponent;
import com.calmmycode.cogniance.model.data.Repo;
import com.calmmycode.cogniance.model.data.User;


import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchFragmentViewModel extends ViewModel {
    private MutableLiveData<String> userName = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFetchingData = new MutableLiveData<>(false);
    private MediatorLiveData<Boolean> isEnabledSubmitButton = new MediatorLiveData<>();

    public SearchFragmentViewModel(){
        isEnabledSubmitButton.addSource(userName, s -> isEnabledSubmitButton.setValue(!TextUtils.isEmpty(s)));
        isEnabledSubmitButton.addSource(isFetchingData, value -> isEnabledSubmitButton.setValue(!value));
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<Boolean> isFetchingData() {
        return isFetchingData;
    }

    public MutableLiveData<Boolean> isEnabledSubmitButton() {
        return isEnabledSubmitButton;
    }

    public void setEnabledSubmitButton(boolean isEnabled) {
        this.isEnabledSubmitButton.postValue(isEnabled);
    }

    public void setIsDataFetching(Boolean isFetched){
        this.isFetchingData.postValue(isFetched);
    }
}
