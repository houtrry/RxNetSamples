package com.houtrry.rxnetsamples;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.houtrry.rxnet.RxNet;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Disposable mNetAvailableSubscribe;
    private Disposable mNetStatusSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetAvailableSubscribe = RxNet.netAvailable(this)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "当前的网络连接状态: aBoolean: " + aBoolean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "当前的网络状态: 出现异常: throwable: " + throwable);
                    }
                });


        mNetStatusSubscribe = RxNet.netStatus(this).subscribe(new Consumer<NetworkInfo>() {
            @Override
            public void accept(NetworkInfo networkInfo) throws Exception {
                if (networkInfo == null) {
                    Log.d(TAG, "未知网络");
                } else {
                    Log.d(TAG, "当前网络状态: " + networkInfo.getTypeName());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: 当前网络状态: 出现异常, throwable: "+throwable);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNetAvailableSubscribe != null) {
            mNetAvailableSubscribe.dispose();
        }

        if (mNetStatusSubscribe != null) {
            mNetStatusSubscribe.dispose();
        }
    }
}
