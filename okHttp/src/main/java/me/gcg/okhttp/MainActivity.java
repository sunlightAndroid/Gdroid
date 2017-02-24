package me.gcg.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDownloadListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;
import me.gcg.okhttp.httpConstant.UrlConstant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * UI
     */
    private Button mButton, mbtn_https, mBtn_downLoadFile;
    private TextView mTextView;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.btn_RequestData);
        mbtn_https = (Button) findViewById(R.id.btn_HttpsRequestData);
        mBtn_downLoadFile = (Button) findViewById(R.id.btn_downLoadFile);
        mTextView = (TextView) findViewById(R.id.tv_responseContent);

        mButton.setOnClickListener(this);
        mBtn_downLoadFile.setOnClickListener(this);
        mbtn_https.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_RequestData:

//                CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(UrlConstant.wxUrl,null),
//                        new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener() {
//                            @Override
//                            public void onSuccess(Object responseObj) {
//                                mTextView.setText(responseObj.toString());
//                            }
//
//                            @Override
//                            public void onFailure(Object reasonObj) {
//                                mTextView.setText(reasonObj.toString());
//                            }
//                        },DataBean.class)));

//                CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(UrlConstant.wxUrl,null),
//                        new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                            @Override
//                            public void onSuccess(String json) {
//                                mTextView.setText("onSuccess:"+json);
//                            }
//                            @Override
//                            public void onFailure(Object reasonObj) {
//                                mTextView.setText("onFailure:"+reasonObj.toString());
//                            }
//                        })));


                RequestManager.requestHomeData(DataBean.class, new DisposeDataListener<DataBean>() {
                    @Override
                    public void onSuccess(DataBean bean) {
                        mTextView.setText(bean.getAds().toString());
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        mTextView.setText(e.toString());
                    }
                });

                break;
            case R.id.btn_HttpsRequestData:

                CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(UrlConstant.HTTPS_URL, null),
                        new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {

                            @Override
                            public void onSuccess(String s) {
                                Log.d(TAG, "onSuccess() returned: " + s);
                                mTextView.setText(s);
                            }

                            @Override
                            public void onFailure(OkHttpException e) {
                                Log.d(TAG, "onFailure() returned: " + e);
                                mTextView.setText(e.toString());
                            }
                        })));
                break;
            case R.id.btn_downLoadFile:


                RequestManager.downloadFile(new DisposeDownloadListener<File>() {
                    @Override
                    public void onProgress(int progrss) {
                        Log.d(TAG, "onProgress() returned: " + progrss);
                    }
                    @Override
                    public void onSuccess(File file) {
                        Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess() returned: " + file.getAbsolutePath());
                    }
                    @Override
                    public void onFailure(OkHttpException e) {
                        Log.d(TAG, "onFailure() returned: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "下载失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
