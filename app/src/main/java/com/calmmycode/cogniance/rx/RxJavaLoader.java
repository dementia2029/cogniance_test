package com.calmmycode.cogniance.rx;

import android.content.Context;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;

public class RxJavaLoader<T> extends Loader<T> {
    /**
     *  Generic class for observable loaders, so you can use and reuse it through all of your projects.
     *  @see <a href="https://proandroiddev.com/rxjava-2-android-how-to-proper-handle-rotations-with-connectableobservable-loader-9356ff47df61>read on medium</a>
     */

    private final Observable<T> observable;
    private final BehaviorSubject<T> cache = BehaviorSubject.create();

    private Disposable disposable;

    private RxJavaLoader(Context context, Observable<T> observable) {
        super(context);
        this.observable = observable;
    }

    public static <T> ObservableTransformer<T, T> compose(final Fragment fragment, final int id) {
        return upstream -> create(fragment, id, upstream);
    }

    public static <T> Observable<T> create(Fragment fragment, int id, Observable<T> observable) {
        LoaderManager loaderManager = fragment.getLoaderManager();

        CreateLoaderCallback<T> createLoaderCallback = new CreateLoaderCallback<>(fragment.getContext(), observable);

        loaderManager.restartLoader(id, null, createLoaderCallback);

        RxJavaLoader<T> rxLoader = (RxJavaLoader<T>) loaderManager.getLoader(id);
        return rxLoader.cache.hide();
    }

    @Nullable
    public static <T> Observable<T> initializeLoader(Fragment fragment, int id, Observable<T> observable) {
        LoaderManager loaderManager = fragment.getLoaderManager();
        CreateLoaderCallback<T> createLoaderCallback = new CreateLoaderCallback<>(fragment.getContext(), observable);

        if (loaderManager.getLoader(id) != null) {
            loaderManager.initLoader(id, null, createLoaderCallback);
        }

        RxJavaLoader<T> rxLoader = (RxJavaLoader<T>) loaderManager.getLoader(id);
        if (rxLoader != null && !rxLoader.cache.hasComplete()) {
            return rxLoader.cache.hide();
        } else {
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        disposable = observable.subscribeWith(cache).subscribeWith(new DisposableObserver<T>() {
            @Override
            public void onNext(T t) {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    protected void onReset() {
        super.onReset();
        disposable.dispose();
    }

    private static class CreateLoaderCallback<T> implements LoaderManager.LoaderCallbacks<T> {
        private final Context context;
        private final Observable<T> observable;

        public CreateLoaderCallback(Context context, Observable<T> observable) {
            this.context = context;
            this.observable = observable;
        }

        @NotNull
        @Override
        public Loader<T> onCreateLoader(int id, Bundle args) {
            return new RxJavaLoader<>(context, observable);
        }

        @Override
        public void onLoadFinished(@NotNull Loader<T> loader, T data) { /* nothing */ }

        @Override
        public void onLoaderReset(@NotNull Loader<T> loader) { /* nothing */ }
    }
}