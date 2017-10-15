package com.houtrry.rxnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by houtrry on 2017/10/15.
 */

final public class NetStatusObservable extends Observable<NetworkInfo> {

    private Context mContext;

    public NetStatusObservable(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    protected void subscribeActual(Observer<? super NetworkInfo> observer) {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        NetReReceiver netReReceiver = new NetReReceiver(observer);
        observer.onSubscribe(netReReceiver);
        mContext.registerReceiver(netReReceiver, mFilter);
    }

    static final class NetReReceiver extends BroadcastReceiver implements Disposable {
        private final Observer<? super NetworkInfo> observer;

        public NetReReceiver(Observer<? super NetworkInfo> observer) {
            this.observer = observer;
        }

        private final AtomicBoolean unSubscribed = new AtomicBoolean();

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isDisposed()) {
                handleNetStatus(context, intent);
            }
        }

        @Override
        public void dispose() {
            // TODO: 2017/10/15 没考虑线程问题, 如果是在子线程, 会出现什么问题呢?
            if (unSubscribed.compareAndSet(false, true)) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    onDispose();
                } else {
                    AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                        @Override public void run() {
                            onDispose();
                        }
                    });
                }
            }
        }

        @Override
        public boolean isDisposed() {
            return unSubscribed.get();
        }

        private Context mContext;
        private void handleNetStatus(Context context, Intent intent) {
            mContext = context;
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("mark", "网络状态已经改变");
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if(info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.d("mark", "当前网络名称：" + name);
                } else {
                    Log.d("mark", "没有可用网络");
                }
                observer.onNext(info);
            }
        }

        private void onDispose() {
            mContext.unregisterReceiver(this);
            mContext = null;
        }
    }
}
