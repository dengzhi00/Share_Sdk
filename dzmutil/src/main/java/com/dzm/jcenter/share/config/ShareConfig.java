package com.dzm.jcenter.share.config;

import android.support.annotation.NonNull;

import com.sina.weibo.sdk.WbSdk;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:37
 * 参数初始化
 */

public class ShareConfig {

    private static Map<ShareType,ShareForm> mapCOnfig = new HashMap<>();

    static {
        mapCOnfig.put(ShareType.WEIXIN,new Weixin(ShareType.WEIXIN));
        mapCOnfig.put(ShareType.QQ,new QQ(ShareType.QQ));
        mapCOnfig.put(ShareType.WEIBO,new WeiBo(ShareType.WEIBO));
    }

    /**
     * 设置微信Appid
     * @param appId appId
     */
    public static void setWeiXin(@NonNull String appId){
        Weixin weixin = (Weixin) mapCOnfig.get(ShareType.WEIXIN);
        weixin.setAppKey(appId);
    }

    /**
     * 设置qq appId
     * @param appId appId
     */
    public static void setQQ(@NonNull String appId){
        QQ qq = (QQ) mapCOnfig.get(ShareType.QQ);
        qq.setAppKey(appId);
    }

    /**
     * 设置微博 appKey
     * @param appKey appKey
     */
    public static void setWeiBo(@NonNull String appKey,@NonNull String scope){
        WeiBo weiBo = (WeiBo) mapCOnfig.get(ShareType.WEIBO);
        weiBo.setAppKey(appKey);
        weiBo.setScope(scope);
    }

    /**
     * 设置微博 appKey
     * @param appKey appKey
     */
    public static void setWeiBo(@NonNull String appKey,@NonNull String scope,String redirectUrl){
        WeiBo weiBo = (WeiBo) mapCOnfig.get(ShareType.WEIBO);
        weiBo.setAppKey(appKey);
        weiBo.setScope(scope);
        weiBo.setRedirectUrl(redirectUrl);
    }

    /**
     * 获取 value
     * @param form ShareType
     * @return  ShareForm
     */
    public static ShareForm getShareForm(ShareType form){
        return mapCOnfig.get(form);
    }
}
