package me.gcg.GdroidSdk.okhttp.listener;

/**
 * @author vision
 * @function 监听下载进度
 */
public interface DisposeDownloadListener<T> extends DisposeDataListener<T> {
	public void onProgress(int progrss);
}
