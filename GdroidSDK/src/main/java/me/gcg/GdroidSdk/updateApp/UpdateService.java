package me.gcg.GdroidSdk.updateApp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

import me.gcg.GdroidSdk.R;
import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDownloadListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;

/**
 * Created by gechuanguang on 2017/2/27.
 * 邮箱：1944633835@qq.com
 *
 * @function 负责apk文件的下载
 */
public class UpdateService extends Service {

    /**
     * 保存 apk文件地址
     */
    private Context mContext = this;

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    private int preProgress = 0;
    private int NOTIFY_ID = 1000;

    @Override
    public void onCreate() {
        super.onCreate();

        initNotification();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DOWNLOAD_APK";

        CommonOkhttpClient.downloadFile(CommonRequest.createPostRequest("http://fairee.vicp.net:83/2016rm/0116/baishi160116.mp4",null),
                new DisposeDataHandle(new DisposeDownloadListener<File>() {
                    @Override
                    public void onProgress(int progrss) {
                        updateNotification(progrss);
                    }
                    @Override
                    public void onSuccess(File file) {
                        Toast.makeText(UpdateService.this, "" + "下载成功", Toast.LENGTH_SHORT).show();

                        cancelNotification();
                        stopSelf();
                    }
                    @Override
                    public void onFailure(OkHttpException e) {
                        Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                        cancelNotification();
                        stopSelf();
                    }
                },path+File.separator+"hello.mp4"));

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 初始化Notification通知
     */
    public void initNotification() {
        builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("0%")
                .setContentTitle("下载更新")
                .setProgress(100, 0, false);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    /**
     * 更新通知
     */
    public void updateNotification(long progress) {
        int currProgress = (int) progress;
        if (preProgress < currProgress) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, (int) progress, false);
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
        preProgress = (int) progress;
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }
}
