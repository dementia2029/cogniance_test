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
import com.calmmycode.cogniance.databinding.FragmentResultsBinding;
import com.calmmycode.cogniance.databinding.FragmentSearchBinding;
import com.calmmycode.cogniance.model.data.Repo;
import com.calmmycode.cogniance.model.data.User;
import com.calmmycode.cogniance.rx.RxJavaLoader;
import com.calmmycode.cogniance.ui.adapter.RepoAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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


public class ResultsFragment extends Fragment {
    public static final String TAG = ResultsFragment.class.getName();
    private MainActivityViewModel mainActivityViewModel;

    @BindView(R.id.listRepos)
    RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        App.get().getAppComponent().inject(this);
        mainActivityViewModel = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentResultsBinding fragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_results, container, false);
        fragmentSearchBinding.setViewModel(mainActivityViewModel);
        fragmentSearchBinding.setLifecycleOwner(this);
        ButterKnife.bind(this, fragmentSearchBinding.getRoot());
        return fragmentSearchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View fragmnetView, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmnetView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new RepoAdapter());
        ((RepoAdapter) recyclerView.getAdapter()).updateData(mainActivityViewModel.getRepos());
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return ViewUtil.getFragmentFadeInFadeOutAnimation(getContext(),enter,getView());
    }

}
