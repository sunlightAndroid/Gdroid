### 网络请求的封装

  * okHttp
  * Retrofit


#### 实际上 Retrofit是对okHttp的再次封装

#### okhttp的Callback的两个方法是回调在子线程的
```
 CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(baseUrl, params), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 //当前线程为子线程
                Log.d(TAG, "onSuccess() returned: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //当前线程为子线程
                Log.d(TAG, "onSuccess() returned: " + response.body().string());
            }
        });
```

> 之所以回调在子线程，应该是有他的道理的，比如说我们下载一个很大文件，那么回调的这个response数据肯定很大，那么如果回调在主线程肯定不合适了

#### 为了使用方便呢，一般会在封装一层，使得直接回调在主线程，这样就可以直接使用。
```
   RequestCenter.newInstance().requestWeatherList("019edfd7e2c45c72", "安顺", new DisposeDataListener() {
            @Override
            public void onSuccess(final Object responseObj) {
                mTextView.setText(responseObj.toString());
            }

            @Override
            public void onFailure(Object reasonObj) {
                mTextView.setText(reasonObj.toString());
            }
        }, null);
```

#### Retrofit也对此做了一层的封装，他的也是回调到主线程的:
```
ApiService service = RetrofitManager.newInstance().createService("http://c.m.163.com/", ApiService.class);

                retrofit2.Call<ResponseBody> call = service.getNewsData("T1348647909107", 0);

                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            try {
                                String json = response.body().string();
                                Log.d(TAG, "onResponse() returned: " + json);
                                mTextView.setText(json);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure() returned: " + t);
                    }
                });

```

