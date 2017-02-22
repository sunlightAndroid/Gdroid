package me.gcg.network.Retrofit;

import retrofit2.Retrofit;

/**
 * Created by gechuanguang on 2017/2/20.
 * 邮箱：1944633835@qq.com
 */

public class RetrofitManager {

    private static RetrofitManager instance = null;
    private static RetrofitConfig mRetrofitConfig = null;

    private Retrofit retrofit = null;

    private RetrofitManager() {
        mRetrofitConfig = RetrofitConfig.newInstance();
    }

    public static RetrofitManager newInstance() {
        if (instance == null) {

            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    /**
     * 配置完成Retrofit
     *
     * @param baseUrl
     */
    private void createRetrofit(String baseUrl) {
        Retrofit.Builder config = mRetrofitConfig.config();
        config.baseUrl(baseUrl);
        retrofit = config.build();
    }

    /**
     *  创建service
     */
    public  <S> S  createService(String baseUrl,Class<S> serviceClass){
        createRetrofit(baseUrl);
        if(retrofit!=null){
            return retrofit.create(serviceClass);
        }
        return null;
    }

}
