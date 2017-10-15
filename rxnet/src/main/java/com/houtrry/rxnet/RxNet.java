package com.houtrry.rxnet;

import android.content.Context;
import android.net.NetworkInfo;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import io.reactivex.Observable;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by houtrry on 2017/10/15.
 */

public class RxNet {
/*
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
 */

    @CheckResult
    @NonNull
    public static Observable<Boolean> netAvailable(@NonNull Context context) {
        checkNotNull(context, "context == null");
        return new NetAvailableObservable(context);
    }

    @CheckResult
    @NonNull
    public static Observable<NetworkInfo> netStatus(@NonNull Context context) {
        checkNotNull(context, "context == null");
        return new NetStatusObservable(context);
    }
}
