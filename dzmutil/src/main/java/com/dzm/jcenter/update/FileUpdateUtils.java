package com.dzm.jcenter.update;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUpdateUtils {

    public static final String DIRECTORY = "ej";
    private static final String TEMP = "update";
    public static final String UPGRADE_APK = "ej.apk";

    public static File getApkupdateFile(Context context){
        return new File(getTempFile(context), UPGRADE_APK);
    }

    public static File getTempFile(Context context) {
        File file = new File(getExternalDirectory(context), TEMP);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    /**
     * 升级或者安装
     *
     * @param apkFile
     */
    public static void install(File apkFile,Context context) {
        // 如果存在
        if (apkFile.exists()) {
            // 如果是在data/data下修改文件权限
            if (apkFile.getAbsolutePath().contains(
                    context.getPackageName())) {
                Runtime runtime = Runtime.getRuntime();
                String command = "chmod -R 777 "
                        + context.getFilesDir();
                try {
                    runtime.exec(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, getPachet(context) + ".fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    public static int getVersionCode(File file,Context context){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        return packageInfo.versionCode;
    }

    public static int getVersionCode(Context context){
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getPachet(Context context){
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 获得可用文件夹
     * @return
     */
    private static File getExternalDirectory(Context context) {
        if (getSDFreeSize() > 20) {
            File file = new File(Environment.getExternalStorageDirectory(), DIRECTORY);
            return file;
        }
        return context.getFilesDir();
    }


    @SuppressWarnings("deprecation")
    private static long getSDFreeSize() {
        if (!hasSdCard()) {
            return 0;
        }
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }


    /**
     * 是否存在sd卡
     *
     * @return
     */
    public static boolean hasSdCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}