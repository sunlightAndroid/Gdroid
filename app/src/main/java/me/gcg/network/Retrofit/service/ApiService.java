package me.gcg.network.Retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by gechuanguang on 2017/2/20.
 * 邮箱：1944633835@qq.com
 * @function Retrofit的一些接口
 */
public interface ApiService {



    //网易新闻:http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
    @GET("nc/article/headline/{type}/{start}-20.html")
    Call<ResponseBody> getNewsData(@Path("type") String type, @Path("start") int start);

    //访问https:  https://kyfw.12306.cn/otn/

    @GET("otn/")
    Call<ResponseBody> getHttpsData();

}
