package com.dzm.jcenter.share.share.wx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.dzm.jcenter.share.share.Share;
import com.dzm.jcenter.share.config.ShareType;
import com.dzm.jcenter.share.config.Weixin;
import com.dzm.jcenter.share.dao.BaseShareDao;
import com.dzm.jcenter.share.dao.wx.ShareWXImageDao;
import com.dzm.jcenter.share.dao.wx.ShareWXMusicDao;
import com.dzm.jcenter.share.dao.wx.ShareWXTextDao;
import com.dzm.jcenter.share.dao.wx.ShareWXVideoDao;
import com.dzm.jcenter.share.dao.wx.ShareWXWebDao;
import com.dzm.jcenter.share.listener.ShareListener;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author 邓治民
 * date 2017/5/11 15:43
 * 微信分享
 */

public class ShareWx extends Share<Weixin> {

    private static final int THUMB_SIZE = 150;

    private IWXAPI api;

    private IWXAPIEventHandler iwxapiEventHandler;

    private ShareListener shareListener;

    private String lastTransaction = "";

    public ShareWx(Activity activity, Weixin sharForm) {
        super(activity, sharForm);
        iwxapiEventHandler = new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {

            }

            @Override
            public void onResp(BaseResp baseResp) {
                if(!TextUtils.equals(lastTransaction,baseResp.transaction)){
                    return;
                }
                switch (baseResp.getType()){
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX: //分享
                        onShareCallback((SendMessageToWX.Resp)baseResp);
                        break;
                }
            }
        };
    }

    /**
     * 分享 回调方法
     * @param baseResp baseResp
     */
    private void onShareCallback(SendMessageToWX.Resp baseResp) {
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK://分享成功
                if(null != shareListener){
                    shareListener.onComplete(ShareType.WEIXIN);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://分享取消
                if(null != shareListener){
                    shareListener.onCancel(ShareType.WEIXIN);
                }
                break;
            default:    //分享失败
                CharSequence err = TextUtils.concat("weixin share error (", String.valueOf(baseResp.errCode), "):", baseResp.errStr);
                if(null != shareListener){
                    shareListener.onError(ShareType.WEIXIN,err.toString());
                }
                break;
        }
    }

    @Override
    public void onCreate(Activity activity,Weixin sharForm){
        api = WXAPIFactory.createWXAPI(activity, sharForm.getAppKey());
        api.registerApp(sharForm.getAppKey());
    }

    @Override
    public void share(BaseShareDao shareDao, ShareListener shareListener) {
        if (!isWXAppInstalled()) {
            toast("您还未安装微信客户端");
            return;
        }
        this.shareListener = shareListener;
        //分享文本
        if (shareDao instanceof ShareWXTextDao) {
            ShareWXTextDao dao = (ShareWXTextDao) shareDao;
            if (TextUtils.isEmpty(dao.message)) {
                toast("message can not null");
                return;
            }

            WXMediaMessage msg = new WXMediaMessage(new WXTextObject(dao.message));
            msg.description = dao.message;
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("text");
            req.message = msg;
            req.scene = dao.isFrendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
            return;
        }

        if (shareDao instanceof ShareWXImageDao) {
            ShareWXImageDao dao = (ShareWXImageDao) shareDao;
            if (null == dao.bitmap) {
                toast("bitmap can not null");
                return;
            }
            WXMediaMessage msg = new WXMediaMessage(new WXImageObject(dao.bitmap));
            //设置缩略图
            Bitmap thumBmp = Bitmap.createScaledBitmap(dao.bitmap, THUMB_SIZE, THUMB_SIZE, true);
            dao.bitmap.recycle();
            msg.thumbData = bmpToByteArray(thumBmp, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = dao.isFrendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
            return;
        }

        if (shareDao instanceof ShareWXMusicDao) {
            ShareWXMusicDao dao = (ShareWXMusicDao) shareDao;
            if (TextUtils.isEmpty(dao.music_url)) {
                toast("music_url can not null");
                return;
            }

            if (TextUtils.isEmpty(dao.music_title)) {
                toast("music_title can not null");
                return;
            }

            if (TextUtils.isEmpty(dao.music_descrip)) {
                toast("music_descrip can not null");
                return;
            }

            if (null == dao.music_bitmap) {
                toast("bitmap can not null");
                return;
            }

            WXMusicObject music = new WXMusicObject();
            music.musicUrl = dao.music_url;
            WXMediaMessage msg = new WXMediaMessage(music);
            msg.title = dao.music_title;
            msg.description = dao.music_descrip;
            msg.thumbData = bmpToByteArray(dao.music_bitmap, true);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("music");
            req.message = msg;
            req.scene = dao.isFrendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
            return;
        }

        if (shareDao instanceof ShareWXVideoDao) {
            ShareWXVideoDao dao = (ShareWXVideoDao) shareDao;
            if (TextUtils.isEmpty(dao.video_url)) {
                toast("video_url can not null");
                return;
            }

            if (TextUtils.isEmpty(dao.video_title)) {
                toast("video_url can not null");
                return;
            }

            if (TextUtils.isEmpty(dao.video_descrip)) {
                toast("video_url can not null");
                return;
            }

            if (null == dao.video_bitmap) {
                toast("video_bitmap can not null");
                return;
            }

            WXVideoObject video = new WXVideoObject();
            video.videoUrl = dao.video_url;
            WXMediaMessage msg = new WXMediaMessage(video);
            msg.title = dao.video_title;
            msg.description = dao.video_descrip;
            msg.thumbData = bmpToByteArray(dao.video_bitmap, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("video");
            req.message = msg;
            req.scene = dao.isFrendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
            return;
        }

        if (shareDao instanceof ShareWXWebDao) {
            ShareWXWebDao dao = (ShareWXWebDao) shareDao;
            if (TextUtils.isEmpty(dao.web_url)) {
                toast("web_url can not null");
                return;
            }

            if(TextUtils.isEmpty(dao.web_title)){
                toast("web_title can not null");
                return;
            }

            if(TextUtils.isEmpty(dao.web_descrip)){
                toast("wen_descrip can not null");
                return;
            }

            if(null == dao.web_bitmap){
                toast("web_bitmap can not null");
                return;
            }

            WXMediaMessage msg = new WXMediaMessage(new WXWebpageObject(dao.web_url));
            msg.title = dao.web_title;
            msg.description = dao.web_descrip;
            msg.thumbData = bmpToByteArray(dao.web_bitmap,true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = dao.isFrendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
            return;
        }
        toast("please select weixin dao");

    }

    /**
     * 获取 IWXAPIEventHandler
     * @return IWXAPIEventHandler
     */
    IWXAPIEventHandler getIwxapiEventHandler() {
        return iwxapiEventHandler;
    }

    /**
     * handleIntent
     * @param intent intent
     * @param handler handler
     */
    void handleIntent(Intent intent, IWXAPIEventHandler handler){
        api.handleIntent(intent,handler);
    }


    /**
     * 是否安装微信
     * @return true 安装 false 未安装
     */
    private boolean isWXAppInstalled() {
        return api.isWXAppInstalled();
    }

    /**
     * 生成唯一标识
     * @param type 类型
     * @return 标识
     */
    private String buildTransaction(String type) {
        lastTransaction = (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
        return lastTransaction;
    }

    /**
     * bitmap 转 字节数组
     * @param bmp         图片
     * @param needRecycle 是否需要重构
     * @return 字节数组
     */
    private byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
