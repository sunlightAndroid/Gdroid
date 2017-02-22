package me.gcg.okhttp;

import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.request.RequestParams;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;
import me.gcg.okhttp.httpConstant.UrlConstant;

/**
 * Created by gechuanguang on 2017/2/22.
 * 邮箱：1944633835@qq.com
 * @Function  来管理一些网络请求
 */
public class RequestManager {


    //根据参数发送所有get请求
    private static void getRequest(String url, RequestParams params, DisposeDataListener<?> listener, Class<?> clazz) {
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(url,params),
                new CommonJsonCallback(new DisposeDataHandle(listener,clazz)));
    }

    //根据参数发送所有post请求
    private static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(url,params),
                new CommonJsonCallback(new DisposeDataHandle(listener,clazz)));
    }

    /**
     * 请求主页数据
     *
     * @param listener
     */
    public static void requestHomeData(Class clazz,DisposeDataListener<?> listener) {
        RequestManager.getRequest(UrlConstant.wxUrl,null,listener,clazz);
    }

    /**
     *  Login
     *
     */
}
