package me.gcg.network.okhttp.RequestCenter;

import me.gcg.network.okhttp.client.CommonOkhttpClient;
import me.gcg.network.okhttp.httpConstant.UrlConstant;
import me.gcg.network.okhttp.listener.DisposeDataHandle;
import me.gcg.network.okhttp.listener.DisposeDataListener;
import me.gcg.network.okhttp.request.CommonRequest;
import me.gcg.network.okhttp.request.RequestParams;
import me.gcg.network.okhttp.response.CommonJsonCallback;

/**
 * Created by gechuanguang on 2017/2/18.
 * 邮箱：1944633835@qq.com
 * @function 主要来进行一些网络请求业务
 */
public class RequestCenter {


    private  static  RequestCenter instance=null;

    private RequestCenter(){
        // TODO: 2017/2/18    一些初始化配置
    }
    public  static  RequestCenter newInstance(){
        if(instance==null){
            synchronized (RequestCenter.class){
                if(instance==null){
                    instance=new RequestCenter();
                }
            }
        }
        return  instance;
    }

    /**
     * get 请求
     */
    private  void get(String url,RequestParams params,DisposeDataListener disposeDataListener,Class<?> clazz){
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(url,params),new CommonJsonCallback(new DisposeDataHandle(disposeDataListener,clazz)));
    }

    /**
     * post 请求
     */
    private  void post(RequestParams params,DisposeDataListener disposeDataListener){
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(UrlConstant.url,params),new CommonJsonCallback(new DisposeDataHandle(disposeDataListener)));
    }

    /**
     *  天气列表的请求
     */
    public   void   requestWeatherList(String appKey, String city,DisposeDataListener disposeDataListener,Class<?> clazz){

        RequestParams params=new RequestParams();
        params.put("appkey",appKey);
        params.put("city",city);

//        get(UrlConstant.url,params,disposeDataListener,clazz);

        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(UrlConstant.url,params),
                new CommonJsonCallback(new DisposeDataHandle(disposeDataListener,clazz)));

//        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(UrlConstant.url,params),
//                new CommonJsonCallback(new DisposeDataHandle(disposeDataListener)));

    }


}
