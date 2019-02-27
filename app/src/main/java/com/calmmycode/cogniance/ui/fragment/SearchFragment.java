package com.calmmycode.cogniance.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.calmmycode.cogniance.App;
import com.calmmycode.cogniance.R;
import com.calmmycode.cogniance.api.GitHubService;
import com.calmmycode.cogniance.databinding.FragmentSearchBinding;
import com.calmmycode.cogniance.model.data.Repo;
import com.calmmycode.cogniance.model.data.User;
import com.calmmycode.cogniance.rx.RxJavaLoader;
import com.calmmycode.cogniance.ui.interfaces.DataFetchListener;
import com.calmmycode.cogniance.ui.util.ViewUtil;
import com.calmmycode.cogniance.viewmodel.MainActivityViewModel;
import com.calmmycode.cogniance.viewmodel.SearchFragmentViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import kotlin.Pair;


public class SearchFragment extends Fragment {
    public static final String TAG = SearchFragment.class.getName();
    private int loaderId = 1;
    private SearchFragmentViewModel searchFragmentViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    GitHubService gitHubService;
    @BindView(R.id.searchButton)
    public MaterialButton mMaterialButton;
    @BindView(R.id.nameEditText)
    public TextInputEditText textInputEditText;
    @BindView(R.id.txtInputLayout)
    public TextInputLayout textInputLayout;
    private Disposable loadDataDisposible;
    private DataFetchListener dataFetchListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        App.get().getAppComponent().inject(this);
        mainActivityViewModel = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);
        dataFetchListener = (DataFetchListener)context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchFragmentViewModel = ViewModelProviders.of(this).get(SearchFragmentViewModel.class);
        FragmentSearchBinding fragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        fragmentSearchBinding.setViewModel(searchFragmentViewModel);
        fragmentSearchBinding.setLifecycleOwner(this);
        ButterKnife.bind(this, fragmentSearchBinding.getRoot());
        return fragmentSearchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View fragmnetView, @Nullable Bundle savedInstanceState) {
        searchFragmentViewModel.isFetchingData().observe(this, aBoolean -> textInputEditText.setEnabled(!aBoolean));
        searchFragmentViewModel.isEnabledSubmitButton().observe(this, aBoolean -> mMaterialButton.setEnabled(aBoolean));
        compositeDisposable.add(RxTextView.textChanges(textInputEditText).subscribe(charSequence -> {
            textInputLayout.setError(null);
            searchFragmentViewModel.setEnabledSubmitButton(!TextUtils.isEmpty(charSequence));
        }));
        compositeDisposable.add(RxTextView.editorActions(textInputEditText).subscribe(unit -> {if (!TextUtils.isEmpty(searchFragmentViewModel.getUserName().getValue())) {
            executeObservable();
        }}));
        compositeDisposable.add(RxView.clicks(mMaterialButton).subscribe(unit -> {if (!TextUtils.isEmpty(searchFragmentViewModel.getUserName().getValue())) {
            executeObservable();
        }}));
        resumeObservableIfPossible();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        if (this.loadDataDisposible != null) this.loadDataDisposible.dispose();
        super.onDestroy();
    }

    /**
     * I use ConnectableObservable for ability to catch rxjava observer
     * after configuration changes. You can try this feauture on emulator
     * with GPRS net mode or simlply add a delay to obserbavle. This helps
     * to catch api response even after activity recreation.
     */
    private void executeObservable(){
        if(loadDataDisposible != null){
            loadDataDisposible.dispose();
        }
        searchFragmentViewModel.setIsDataFetching(true);
        ConnectableObservable<Pair<User,ArrayList<Repo>>> connectableObservable = getObservable().compose(RxJavaLoader.compose(this, loaderId)).publish();
        connectableObservable.subscribeWith(getSubscriber());
        this.loadDataDisposible = connectableObservable.connect();
    }

    /* Try to resume observable after activity recreated. */
    private void resumeObservableIfPossible(){
        Observable<Pair<User,ArrayList<Repo>>> observable = RxJavaLoader.initializeLoader(this, loaderId, getObservable());
        if (observable != null){
            searchFragmentViewModel.setIsDataFetching(true);
            observable.publish();
            this.loadDataDisposible = observable.subscribeWith(getSubscriber());
        }
    }

    /* Zip observables to catch response from two queries. */
    private Observable<Pair<User,ArrayList<Repo>>> getObservable(){
        return Observable
                .zip(gitHubService.getUserObservable(searchFragmentViewModel.getUserName().getValue()), gitHubService.getUserRepos(searchFragmentViewModel.getUserName().getValue()), Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private DisposableObserver<Pair<User,ArrayList<Repo>>> getSubscriber(){
        return new DisposableObserver<Pair<User,ArrayList<Repo>>>() {
            @Override
            public void onNext(Pair<User,ArrayList<Repo>> result) {
                mainActivityViewModel.updateData(result);
                searchFragmentViewModel.setIsDataFetching(false);
                textInputLayout.setError(null);
                dataFetchListener.dataFetched();
            }

            @Override
            public void onError(Throwable e) {
                searchFragmentViewModel.setIsDataFetching(false);
                textInputLayout.setError(e.getMessage());
            }

            @Override
            public void onComplete() {}
        };
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return ViewUtil.getFragmentFadeInFadeOutAnimation(getContext(),enter,getView());
    }
}
