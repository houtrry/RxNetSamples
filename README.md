# RxNetSamples
仿RxBinding实现对网络状态的监听

### 使用

```
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
```  

注意, 为防止内存泄漏, 应该在生命周期方法里disposed上面的监听.