package me.gcg.network;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import me.gcg.network.Retrofit.RetrofitManager;
import me.gcg.network.Retrofit.service.ApiService;
import me.gcg.network.okhttp.RequestCenter.RequestCenter;
import me.gcg.network.okhttp.client.CommonOkhttpClient;
import me.gcg.network.okhttp.httpConstant.UrlConstant;
import me.gcg.network.okhttp.listener.DisposeDataHandle;
import me.gcg.network.okhttp.listener.DisposeDataListener;
import me.gcg.network.okhttp.request.CommonRequest;
import me.gcg.network.okhttp.request.RequestParams;
import me.gcg.network.okhttp.response.CommonJsonCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mTextView;

    private String appKey = "019edfd7e2c45c72";
    private String url = "http://api.jisuapi.com/weather/query?appkey=" + appKey +
            "&city=安顺";
    private String url2 = "https://www.imooc.com";
    private String url3 = "http://music.163.com/api/search/get/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tvContent);


        findViewById(R.id.btn_RequestData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                CommonRequest();
//                CommonRxjavaRequest();
//                RetrofitRequest();
                RetrofitHttpsRequest();

            }

        });


    }

    /**
     *  retrofit 进行https请求
     */
    private void RetrofitHttpsRequest() {
        ApiService service = RetrofitManager.newInstance().createService("https://kyfw.12306.cn/", ApiService.class);
        retrofit2.Call<ResponseBody> httpsData = service.getHttpsData();
        httpsData.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.d(TAG, "onResponse() returned: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure() returned: " + t);
            }
        });
    }

    /**
     *  普通的retrofit请求 --> 默认的回调是在主线程
     */
    private void RetrofitRequest() {
        ApiService service = RetrofitManager.newInstance().createService("http://c.m.163.com/", ApiService.class);

        retrofit2.Call<ResponseBody> call = service.getNewsData("T1348647909107", 0);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
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
    }

    /**
     *  普通的okHttp 请求，然后使用rxjava来处理数据
     */
    private void CommonRxjavaRequest() {
        RequestCenter.newInstance().requestWeatherList(appKey, "安顺", new DisposeDataListener() {
            @Override
            public void onSuccess(final Object responseObj) {
                Log.d(TAG, "onSuccess() returned: " + ((WeatherBean) responseObj).getMsg());
                /**
                 *  rxjava的支持
                 */
                Observable<WeatherBean> observable = Observable.create(new Observable.OnSubscribe<WeatherBean>() {
                    @Override
                    public void call(Subscriber<? super WeatherBean> subscriber) {
                        subscriber.onNext((WeatherBean) responseObj);
                    }
                });
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<WeatherBean>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(WeatherBean weatherBean) {
                                Log.d(TAG, "onSuccess() returned: " + weatherBean.getMsg() + weatherBean.getStatus() + weatherBean.getResult());
                            }
                        });
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        }, WeatherBean.class);
    }

    /**
     *  okHttp 默认的请求回调Callback 是在子线程的，不能更新UI 需要使用Rxjava 线程切换
     */
    private void CommonRequest() {
        RequestParams params = new RequestParams();
        params.put("appkey", appKey);
        params.put("city", "安顺");

        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(UrlConstant.url, params), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                    Log.d(TAG, "Main Thread");
                } else {
                    Log.d(TAG, "Not Main Thread");
                }
                mTextView.setText(e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String json = response.body().string();

                Log.d(TAG, "onResponse() returned: " + json);

//                final WeatherBean weatherBean = JsonUtils.fromJson(response.body().string(), WeatherBean.class);

                Observable<String> ob = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(json);
                    }
                });
                ob.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                            }
                            @Override
                            public void onError(Throwable e) {
                            }
                            @Override
                            public void onNext(String s) {
                                Log.d(TAG, "call() returned: " + s + "-->" + Thread.currentThread().getName());
                                mTextView.setText(s);
                                Toast.makeText(MainActivity.this, ":" + json, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }


    private void testWeather() {
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
    }

    private void testNet() {

        String cludUrl = "http://music.163.com/api/album/32311/";
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(cludUrl, null),
                new CommonJsonCallback(
                        new DisposeDataHandle(new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                Log.d(TAG, "onSuccess() returned: " + responseObj);
                            }

                            @Override
                            public void onFailure(Object reasonObj) {
                                Log.d(TAG, "onFailure() returned: " + reasonObj);
                            }
                        })));

    }


    private void testNet2() {
        //测试
//        RequestParams params=new RequestParams();
//        params.put("s","玫瑰色的你");
//        params.put("limit","20");
//        params.put("type","1"); params.put("offset","0");
//        params.put("appver","1.5.2");

        //http://music.163.com/api/song/detail/?id={$SongId}&ids=%5B{$SongId}%5D&csrf_token=

        String baseUrl = "http://music.163.com/eapi/batch";
        RequestParams params = new RequestParams();
        params.put("params", "0BD8BB39A78692F1744DEFF63EBC30F7889FA0D28FD18C56783C7BF3AADA4C516E269DCEF72717031B0D0797563D21D74A80931032E90A0DBF772B7B86DAB7B29C47227066BA6859EF81B2BDC94960501592EFDBED2FA4BB612DD34C3BE69C1CB997189A2D14BE23FACD2D81694F87D7D86DD3F48F213C035A89EDEE2F6336478BEBA964633B3DB2A074EA2662FE8AEC18A167403EA0D465ED99F6E0BF1B58D64E2F6FAB87BFB382901FB3F8D753ABABE5361DD03E8767F3CC5BE299EDCBF8CEA82126579A7E11CD9A6B7A95AEB41CEC237356031206C2C94443360BB430F44D4CE1F78FE98FDF4468B40977A33CD3A7AD9A9F926C5E1B3979139277DBCDF27E7EB4BFC0C4996CD069835883475527C7D296034459225E90FC0FD45F259EDAD79318B200CCC01B51E4571EFD93F7E7EFE09D1169A86936C7C3D1E0EAAFE6955D2A72808C6F340B4388E57F4443C22DCB267E6BA157E3256F2924B9A2DD0B1F4C001E848DC9F85F05DE82FCCA50763549329EF9DF1BC9746B9CFB7308D72159C5A5DC242B76960F7E62827FD52B8F4BCF7A667EBDAD93E5D34CB68D92ECBCD7FEE9265DD359457ED508F38B088041E5BBFDB949F891FA490B48B24C2C754762F31DC4C0F0C8E3930D08A628D82D10C6CADDEA0BBDF8D9FF405C9FE9B2E5622BD99757F50109BF2BBE0B6804606EB5EF23E3D772D023013244905739680AC5801E039D02D768DDB47BE085BE698DFA91C29B13F34AFEC3DA8E69251F8EB21D1A11B85F89B6383089FEF4713C1C21972D09E2433FEDADBAB3B6ED239935E06E76AACA3A66B3F11E51EFD0F5AD0CE6A32783");
//        params.put("ids", "20");

        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(baseUrl, params), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onSuccess() returned: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onSuccess() returned: " + response.body().string());
            }
        });


    }


}
