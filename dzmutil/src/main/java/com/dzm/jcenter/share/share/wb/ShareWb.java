package com.dzm.jcenter.share.share.wb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.dzm.jcenter.share.config.ShareType;
import com.dzm.jcenter.share.config.WeiBo;
import com.dzm.jcenter.share.dao.BaseShareDao;
import com.dzm.jcenter.share.dao.wb.ShareWbImageDao;
import com.dzm.jcenter.share.dao.wb.ShareWbMediaDao;
import com.dzm.jcenter.share.dao.wb.ShareWbMixDao;
import com.dzm.jcenter.share.dao.wb.ShareWbTextDao;
import com.dzm.jcenter.share.listener.ShareListener;
import com.dzm.jcenter.share.share.Share;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

/**
 *
 * @author 邓治民
 * date 2017/5/12 9:56
 * 微博
 */
public class ShareWb extends Share<WeiBo> implements WbShareCallback{

    /**微博分享*/
    private WbShareHandler shareHandler;

    /**回调事件*/
    private ShareListener shareListener;

    public ShareWb(Activity activity, WeiBo sharForm) {
        super(activity, sharForm);
    }

    @Override
    public void onCreate(Activity activity, WeiBo sharForm) {
        shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();
    }

    @Override
    public void share(BaseShareDao shareDao, ShareListener shareListener) {
        if(shareDao instanceof ShareWbImageDao){
            ShareWbImageDao dao = (ShareWbImageDao) shareDao;
            if(dao.share_bitmap_resource == 0){
                toast("bitmap can not null");
                return;
            }
            WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),dao.share_bitmap_resource);
            weiboMultiMessage.imageObject = getImageObj(bitmap);
            shareHandler.shareMessage(weiboMultiMessage, true);
            return;
        }

        if(shareDao instanceof ShareWbTextDao){
            ShareWbTextDao dao = (ShareWbTextDao) shareDao;
            if(TextUtils.isEmpty(dao.share_text)){
                toast("text can not null");
                return;
            }
            WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
            weiboMultiMessage.textObject = getTextObj(dao.share_text);
            shareHandler.shareMessage(weiboMultiMessage, true);
            return;
        }

        if(shareDao instanceof ShareWbMediaDao){
            ShareWbMediaDao dao = (ShareWbMediaDao) shareDao;
            if(TextUtils.isEmpty(dao.share_title)){
                toast("title can not null");
                return;
            }
            WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
            WebpageObject mediaObject = new WebpageObject();
            mediaObject.identify = Utility.generateGUID();
            mediaObject.title = dao.share_title;
            if(!TextUtils.isEmpty(dao.share_descrip)){
                mediaObject.description = dao.share_descrip;
            }
            if(!TextUtils.isEmpty(dao.share_actionurl)){
                mediaObject.actionUrl = dao.share_actionurl;
            }
            mediaObject.defaultText = "Webpage 默认文案";
            weiboMultiMessage.mediaObject = mediaObject;
            shareHandler.shareMessage(weiboMultiMessage, true);
            return;
        }

        if(shareDao instanceof ShareWbMixDao){
            ShareWbMixDao dao = (ShareWbMixDao) shareDao;
            WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
            if(!TextUtils.isEmpty(dao.share_text)){
                weiboMultiMessage.textObject = getTextObj(dao.share_text);
            }

            if(dao.share_bitmap_resource != 0){
                Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), dao.share_bitmap_resource);
                weiboMultiMessage.imageObject = getImageObj(bitmap);
            }
            WebpageObject mediaObject = new WebpageObject();
            mediaObject.identify = Utility.generateGUID();
            mediaObject.defaultText = "微博分享";
            if(!TextUtils.isEmpty(dao.share_media_title)){
                mediaObject.title = dao.share_media_title;
            }
            if(!TextUtils.isEmpty(dao.share_media_actionurl)){
                mediaObject.actionUrl = dao.share_media_actionurl;
            }
            weiboMultiMessage.mediaObject = mediaObject;
            shareHandler.shareMessage(weiboMultiMessage, true);
            return;
        }
        toast("please select weibo dao");

    }

    @Override
    public void setShareListener(ShareListener shareListener) {
        this.shareListener = shareListener;
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        textObject.title = "xxxx";
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * onNewIntent
     * @param intent Intent
     * @param callback WbShareCallback
     */
    void onNewIntent(Intent intent, WbShareCallback callback){
        shareHandler.doResultIntent(intent,callback);
    }

    @Override
    public void onWbShareSuccess() {
        shareListener.onComplete(ShareType.WEIBO);
    }

    @Override
    public void onWbShareCancel() {
        shareListener.onCancel(ShareType.WEIBO);
    }

    @Override
    public void onWbShareFail() {
        shareListener.onError(ShareType.WEIBO,"wb share failed");
    }
}
