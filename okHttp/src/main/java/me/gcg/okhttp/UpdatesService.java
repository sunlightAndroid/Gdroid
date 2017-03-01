package me.gcg.okhttp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDownloadListener;

/**
 * Created by gechuanguang on 2017/2/27.
 * 邮箱：1944633835@qq.com
 *
 * @function 负责apk文件的下载
 */
public class UpdatesService extends Service {

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

        RequestManager.downloadApk(new DisposeDownloadListener<File>() {
            @Override
            public void onProgress(int progrss) {
                updateNotification(progrss);
            }

            @Override
            public void onSuccess(File file) {
                Toast.makeText(UpdatesService.this, "" + "下载成功", Toast.LENGTH_SHORT).show();

                cancelNotification();
                stopSelf();
            }
            @Override
            public void onFailure(OkHttpException e) {

                cancelNotification();
                stopSelf();

            }
        });

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
                .setSmallIcon(R.mipmap.ic_launcher)
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
