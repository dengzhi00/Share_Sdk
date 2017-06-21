# Share_Sdk

集成qq，微博，微信分享功能，不必引入第三方库，直接使用qq，微博，微信自己的最新额分享接口



android 7.0通过系统安装apk报错：

7.0特殊写法：

```
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
```



创建xml资源文件：

res/xml/file_paths.xml

```
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path path="Android/data/包名/" name="files_root" />
    <external-path path="." name="external_storage_root" />
</paths>
```



manifest清单文件：

```
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="包名.fileProvider"
    android:grantUriPermissions="true"
    android:exported="false">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```