package me.gcg.network.Retrofit;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import me.gcg.network.okhttp.HttpsUtils;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gechuanguang on 2017/2/20.
 * 邮箱：1944633835@qq.com
 */

public class RetrofitConfig {

    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient = null;
    private static RetrofitConfig mRetrofitConfig = null;

    private RetrofitConfig() {
    }

    static {
        //为mOkHttpClient去配置参数  类加载的时候开始创建静态代码块，并且只执行一次
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(true); //设置重定向 其实默认也是true

        /*--添加请求头  这个看个人需求 --*/
//        okHttpClientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request()
//                        .newBuilder()
//                        .addHeader("User-Agent", "Android—Mobile") // 标明发送本次请求的客户端
//                        .build();
//                return chain.proceed(request);
//            }
//        });

        //添加https支持
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        /**
         * trust all the https point
         */
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    public static RetrofitConfig newInstance() {
        if (mRetrofitConfig == null) {
            synchronized (RetrofitConfig.class) {
                if (mRetrofitConfig == null) {
                    mRetrofitConfig = new RetrofitConfig();
                }
            }
        }
        return mRetrofitConfig;
    }

    /**
     * 配置Retrofit.Builder
     *
     * @return
     */
    public Retrofit.Builder config() {
        Retrofit.Builder builder = new Retrofit.Builder();
        /*--添加https支持--*/
        builder.client(mOkHttpClient);
        /*--添加Gson支持--*/
        builder.addConverterFactory(GsonConverterFactory.create());
        /*--添加Rxjava支持--*/
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        return builder;

    }
}
