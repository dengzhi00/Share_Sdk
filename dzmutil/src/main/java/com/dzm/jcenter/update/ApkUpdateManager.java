package com.dzm.jcenter.update;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 83642 on 2017/6/16.
 */

public class ApkUpdateManager {

    private static DownloadTask downloadTask;

    public static DownloadTask with(Context context) {

        if (null == downloadTask) {
            downloadTask = new DownloadTask(context);
        } else {
            if (downloadTask.isDestroy()) {
                onDestroy();
                downloadTask = new DownloadTask(context);
            }
        }
        return downloadTask;
    }


    public static class DownloadTask extends Thread {

        public class ApkBuild {
            String downloadUrl;// 下载链接地址
            int threadNum;// 开启的线程数
            String filePath;// 保存文件路径地址
            String md5;    //文件MD5值
            boolean neadInstallApk; //是否需要自动安装apk
            String versionCode;   //需要更新的apk版本

            public ApkBuild setDownloadUrl(String downloadUrl) {
                this.downloadUrl = downloadUrl;
                return this;
            }

            public ApkBuild setFilePath(String filePath) {
                this.filePath = filePath;
                return this;
            }

            public ApkBuild setThreadNum(int threadNum) {
                this.threadNum = threadNum;
                return this;
            }

            public ApkBuild setMd5(String md5) {
                this.md5 = md5;
                return this;
            }


            public ApkBuild setNeedInstallApk(boolean bool) {
                apkBuild.neadInstallApk = bool;
                return this;
            }

            public ApkBuild setVersionCode(String versionCode) {
                this.versionCode = versionCode;
                return this;
            }

            public DownloadTask build() {
                return DownloadTask.this;
            }
        }

        private ApkBuild apkBuild;

        private Context context;
        private OnFileUpdateListener onFileUpdateProgressListeners;
        private final int count = 1;
        private long total_data = TrafficStats.getTotalRxBytes();

        private FileDownloadThread[] threads;
        private boolean isDestroy;

        private Handler handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 2) {
                    BundData bundData = (BundData) msg.obj;
                    if (null != onFileUpdateProgressListeners) {
                        onFileUpdateProgressListeners.onUpdate(bundData.ratio, bundData.all_size);
                    }
                    if (bundData.ratio >= 1) {
                        if (apkBuild.neadInstallApk) {
                            File file = new File(apkBuild.filePath);
                            if (TextUtils.isEmpty(apkBuild.md5)) {
                                onEnd(file);
                            } else {
                                if (TextUtils.equals(FileMd5Util.getFileMD5String(file), apkBuild.md5)) {
                                    onEnd(file);
                                } else {
                                    if (null != onFileUpdateProgressListeners) {
                                        onFileUpdateProgressListeners.onErre("MD5 didn't erual");
                                    }
                                }
                            }
                        }
                        if (null != onFileUpdateProgressListeners) {
                            handler.removeCallbacks(speedRunnable);
                        }
                    }
                }
            }
        };

        private Runnable speedRunnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, count * 500);
                if (null != onFileUpdateProgressListeners) {
                    onFileUpdateProgressListeners.updateSpeed(getNetSpeed() / 1024 + "kb/s");
                }
            }
        };

        public DownloadTask setOnFileUpdateProgressListener(OnFileUpdateListener onFileUpdateProgressListener) {
            this.onFileUpdateProgressListeners = onFileUpdateProgressListener;
            handler.postDelayed(speedRunnable, 0);
            return this;
        }

        public ApkBuild build() {
            return apkBuild;
        }

        @Override
        public synchronized void start() {
            if (TextUtils.isEmpty(apkBuild.filePath)) {
                Toast.makeText(context, "filePath is null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(apkBuild.downloadUrl)) {
                Toast.makeText(context, "downloadUrl is null", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(apkBuild.filePath);
            if (file.exists()) {
                if (!TextUtils.isEmpty(apkBuild.md5)) {
                    if (TextUtils.equals(FileMd5Util.getFileMD5String(file), apkBuild.md5)) {
                        onEnd(file);
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(apkBuild.versionCode)) {
                        if (FileUpdateUtils.getVersionCode(file, context) > FileUpdateUtils.getVersionCode(context)) {
                            onEnd(file);
                            return;
                        }
                    } else {
                        if (TextUtils.equals(apkBuild.versionCode, String.valueOf(FileUpdateUtils.getVersionCode(file, context)))) {
                            onEnd(file);
                            return;
                        }
                    }
                }
            }
            if (isDestroy) {
                super.start();
            } else {
                Toast.makeText(context, "is in update", Toast.LENGTH_SHORT).show();
            }

        }

        private void onEnd(File file) {
            if (null != onFileUpdateProgressListeners) {
                onFileUpdateProgressListeners.onEnd(file);
            }
            if (apkBuild.neadInstallApk) {
                FileUpdateUtils.install(file, context);
            }
        }

        /**
         * 核心方法，得到当前网速
         * @return
         */
        private int getNetSpeed() {
            long traffic_data = TrafficStats.getTotalRxBytes() - total_data;
            total_data = TrafficStats.getTotalRxBytes();
            return (int) traffic_data / count;
        }

        private DownloadTask(Context context) {
            this.context = context;
            apkBuild = new ApkBuild();
            apkBuild.threadNum = 5;
            isDestroy = true;
        }

        @Override
        public void run() {
            threads = new FileDownloadThread[apkBuild.threadNum];
            int index;
            isDestroy = false;
            while (!isDestroy) {
                index = 5;
                try {
                    URL url = new URL(apkBuild.downloadUrl);
                    URLConnection conn = url.openConnection();
                    // 读取下载文件总大小
                    int fileSize = conn.getContentLength();
                    if (fileSize <= 0) {
                        System.out.println("读取文件失败");
                    } else {
                        if (null != onFileUpdateProgressListeners) {
                            onFileUpdateProgressListeners.onStart(fileSize);
                        }
                        // 计算每条线程下载的数据长度
                        int blockSize = (fileSize % apkBuild.threadNum) == 0 ? fileSize / apkBuild.threadNum : fileSize / apkBuild.threadNum + 1;
                        File file = new File(apkBuild.filePath);
                        for (int i = 0; i < threads.length; i++) {
                            // 启动线程，分别下载每个线程需要下载的部分
                            threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
                            threads[i].setName("Thread:" + i);
                            threads[i].start();
                        }

                        boolean isfinished = false;
                        boolean isChilcErrer = false;
                        int downloadedAllSize = 0;
                        while (!isfinished) {
                            isfinished = true;
                            isChilcErrer = false;
                            // 当前所有线程下载总量
                            downloadedAllSize = 0;
                            for (FileDownloadThread thread : threads) {
                                downloadedAllSize += thread.getDownloadLength();
                                if (!thread.isCompleted()) {
                                    isfinished = false;
                                }
                                isChilcErrer = thread.isOnErre();
                            }
                            // 通知handler去更新视图组件
//                            Log.d("all_size", "all_size  :" + downloadedAllSize + "   ratio  " + (float) downloadedAllSize / fileSize);
                            Message msg = handler.obtainMessage();
                            msg.what = 2;
                            BundData bundData = new BundData();
                            bundData.all_size = downloadedAllSize;
                            bundData.ratio = (float) downloadedAllSize / fileSize;
                            msg.obj = bundData;
                            handler.sendMessage(msg);
                            if (isChilcErrer) {
                                Thread.sleep(1000);// 休息1秒后再读取下载进度
                            } else {
                                Thread.sleep(10);// 休息1秒后再读取下载进度
                            }
                        }
                        isDestroy = true;
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    while (index > 0) {
                        sleep(1000);
                        index--;
                        Log.d("update_thread", "connect filed" + index);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onDestroy() {
            if (null != threads) {
                for (FileDownloadThread thread : threads) {
                    if (thread.isAlive()) {
                        thread.interrupt();
                    }
                }
            }
            isDestroy = true;
        }

        public boolean isDestroy() {
            return isDestroy;
        }
    }

    private static class BundData {
        int all_size;
        float ratio;
    }

    public static void onDestroy() {
        if (null != downloadTask) {
            if (downloadTask.isAlive()) {
                downloadTask.onDestroy();
                downloadTask.interrupt();
            }
            downloadTask = null;
        }
    }

}
