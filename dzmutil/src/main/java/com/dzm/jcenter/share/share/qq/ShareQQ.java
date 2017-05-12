package com.dzm.jcenter.share.share.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.dzm.jcenter.share.share.Share;
import com.dzm.jcenter.share.config.QQ;
import com.dzm.jcenter.share.dao.BaseShareDao;
import com.dzm.jcenter.share.dao.qq.ShareQQImageDao;
import com.dzm.jcenter.share.dao.qq.ShareQQMusicDao;
import com.dzm.jcenter.share.dao.qq.ShareQQTextImageDao;
import com.dzm.jcenter.share.listener.ShareListener;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * @author 邓治民
 *         date 2017/5/10 15:05
 *         qq 分享
 */

public class ShareQQ extends Share<QQ> {

    /** 分享结构 */
    private Tencent tencent;


    /**
     * 构造函数
     * @param activity  上下文
     * @param shareForm 分享类型
     */
    public ShareQQ(Activity activity, QQ shareForm) {
        super(activity, shareForm);
    }


    @Override
    public void onCreate(Activity activity, QQ sharForm) {
        tencent = Tencent.createInstance(sharForm.getAppKey(), activity);
    }

    @Override
    public void share(BaseShareDao shareDao, ShareListener shareListener) {
        Bundle params = new Bundle();
        //图文分享
        if (shareDao instanceof ShareQQTextImageDao) {
            ShareQQTextImageDao dao = (ShareQQTextImageDao) shareDao;
            //分享的类型
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            //标题
            if (TextUtils.isEmpty(dao.title)) {
                toast("title can not null");
                return;
            }
                params.putString(QQShare.SHARE_TO_QQ_TITLE, dao.title);
            //这条分享消息被好友点击后的跳转URL
            if (TextUtils.isEmpty(dao.target_url)) {
                toast("target_url can not null");
                return;
            }
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, dao.target_url);
            //摘要
            if (!TextUtils.isEmpty(dao.summary)) {
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, dao.summary);
            }
            //分享图片的URL或者本地路径
            if (!TextUtils.isEmpty(dao.image_url)) {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, dao.image_url);
            } else if (!TextUtils.isEmpty(dao.local_image_url)) {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, dao.local_image_url);
            }
            //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            if (!TextUtils.isEmpty(dao.app_name)) {
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, dao.app_name);
            }
            if(dao.isToQozen){
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            }
            shareToMainThread(params, shareListener);
            return;
        }
        //分享纯图
        if (shareDao instanceof ShareQQImageDao) {
            ShareQQImageDao dao = (ShareQQImageDao) shareDao;
            //分享的类型
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            //需要分享的本地图片路径。
            if (TextUtils.isEmpty(dao.local_image_url)) {
                toast("local_image_url can not null");
                return;
            } else {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, dao.local_image_url);
            }
            //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            if (!TextUtils.isEmpty(dao.app_name)) {
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, dao.app_name);
            }
            //
            if(dao.isToQozen){
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            }
            shareToMainThread(params, shareListener);
            return;
        }

        //分享音乐
        if (shareDao instanceof ShareQQMusicDao) {
            ShareQQMusicDao dao = (ShareQQMusicDao) shareDao;
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
            //分享的标题
            if (TextUtils.isEmpty(dao.title)) {
                toast("title can not null");
                return;
            }
            params.putString(QQShare.SHARE_TO_QQ_TITLE, dao.title);
            //这条分享消息被好友点击后的跳转URL。
            if (TextUtils.isEmpty(dao.target_url)) {
                toast("target_url can not null");
                return;
            }
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, dao.target_url);
            //音乐文件的远程链接,
            if (TextUtils.isEmpty(dao.audio_url)) {
                toast("audio_url can not null");
                return;
            }
                params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, dao.audio_url);
            //摘要
            if (!TextUtils.isEmpty(dao.summary)) {
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, dao.summary);
            }
            //分享图片的URL或者本地路径
            if (!TextUtils.isEmpty(dao.image_url)) {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, dao.image_url);
            } else if (!TextUtils.isEmpty(dao.local_image_url)) {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, dao.local_image_url);
            }
            //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            if (!TextUtils.isEmpty(dao.app_name)) {
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, dao.app_name);
            }
            if(dao.isToQozen){
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            }
            shareToMainThread(params, shareListener);
            return;
        }
        toast("please select qq dao");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
    }


    /**
     * 分享
     * @param bundle 内容
     */
    private void shareToMainThread(final Bundle bundle, final ShareListener shareListener) {
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                tencent.shareToQQ(activity, bundle, new MyIUiListener(shareListener));
            }
        });
    }

    /**
     * 分享回调事件
     */
    private class MyIUiListener implements IUiListener {

        private ShareListener shareListener;

        MyIUiListener(ShareListener shareListener) {
            this.shareListener = shareListener;
        }

        @Override
        public void onComplete(Object o) {
            if (null != shareListener) {
                shareListener.onComplete(sharForm.getShareType());
            }

        }

        @Override
        public void onError(UiError uiError) {
            String errmsg = "errcode=" + uiError.errorCode + " errmsg=" + uiError.errorMessage + " errdetail=" + uiError.errorDetail;
            if (null != shareListener) {
                shareListener.onError(sharForm.getShareType(), errmsg);
            }

        }

        @Override
        public void onCancel() {
            if (null != shareListener) {
                shareListener.onCancel(sharForm.getShareType());
            }

        }
    }

}
