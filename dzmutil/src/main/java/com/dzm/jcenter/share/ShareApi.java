package com.dzm.jcenter.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.dzm.jcenter.share.config.QQ;
import com.dzm.jcenter.share.config.ShareConfig;
import com.dzm.jcenter.share.config.ShareType;
import com.dzm.jcenter.share.config.WeiBo;
import com.dzm.jcenter.share.config.Weixin;
import com.dzm.jcenter.share.dao.BaseShareDao;
import com.dzm.jcenter.share.listener.ShareListener;
import com.dzm.jcenter.share.share.qq.ShareQQ;
import com.dzm.jcenter.share.share.Share;
import com.dzm.jcenter.share.share.wb.ShareWb;
import com.dzm.jcenter.share.share.wb.WbShareCallbackActivity;
import com.dzm.jcenter.share.share.wx.ShareWx;
import com.dzm.jcenter.update.ApkUpdateManager;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;
import static com.dzm.jcenter.share.share.wb.WbShareCallbackActivity.DZM_DATA;

/**
 *
 * @author 邓治民
 * date 2017/5/10 16:32
 * 分享管理器
 */

public class ShareApi {

    private static volatile ShareApi shareApi;

    /**上下文*/
    private Context context;

    private final Map<ShareType,Share> shareMap = new HashMap<>();

    private ShareApi(Context context) {
        if(context instanceof Activity){
            this.context = context;
            Activity activity = (Activity) context;
            if(!TextUtils.isEmpty(ShareConfig.getShareForm(ShareType.QQ).getAppKey())){
                shareMap.put(ShareType.QQ,new ShareQQ(activity, (QQ) ShareConfig.getShareForm(ShareType.QQ)));
            }
            if(!TextUtils.isEmpty(ShareConfig.getShareForm(ShareType.WEIXIN).getAppKey())){
                shareMap.put(ShareType.WEIXIN,new ShareWx(activity, (Weixin) ShareConfig.getShareForm(ShareType.WEIXIN)));
            }
            WeiBo weiBo = (WeiBo) ShareConfig.getShareForm(ShareType.WEIBO);
            if(!TextUtils.isEmpty(weiBo.getAppKey()) && !TextUtils.isEmpty(weiBo.getScope())){
                if(TextUtils.isEmpty(weiBo.getRedirectUrl())){
                    WbSdk.install(activity,new AuthInfo(activity, weiBo.getAppKey(), "https://api.weibo.com/oauth2/default.html", weiBo.getScope()));
                }else{
                    WbSdk.install(activity,new AuthInfo(activity, weiBo.getAppKey(), weiBo.getRedirectUrl(), weiBo.getScope()));
                }
                shareMap.put(ShareType.WEIBO,new ShareWb(activity, weiBo));
            }
        }else{
            throw new IllegalArgumentException("context must be activity");
        }
    }

    public static ShareApi getInstance(Context context){
        if(null == shareApi){
            synchronized (ShareApi.class){
                shareApi = new ShareApi(context);
            }
        }
        return shareApi;
    }

    /**
     * 分享
     * @param shareDao BaseShareDao
     * @param shareListener ShareListener
     * @param shareType ShareType 分享类型 QQ  WEIXIN WEIBO
     */
    public void share(BaseShareDao shareDao, ShareListener shareListener,ShareType shareType){
        Share share = shareMap.get(shareType);
        if(null != share){
            if(shareType == ShareType.WEIBO){
                share.setShareListener(shareListener);
                Intent intent = new Intent(context, WbShareCallbackActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(WbShareCallbackActivity.DZM_DATA,shareDao);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }else{
                share.share(shareDao,shareListener);
            }
        }else{
            switch (shareType){
                case QQ:
                    Toast.makeText(context,"qq_appid can not null",Toast.LENGTH_SHORT).show();
                    break;
                case WEIXIN:
                    Toast.makeText(context,"weixin_appid can not null",Toast.LENGTH_SHORT).show();
                    break;
                case WEIBO:
                    Toast.makeText(context,"weibo_apikey can not null",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    /**
     * onActivityResult
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        for (Map.Entry<ShareType,Share> entry : shareMap.entrySet()){
            entry.getValue().onActivityResult(requestCode,resultCode,data);
        }
    }

    /**
     * 获取Share
     * @param shareType shareType
     * @return Share
     */
    public Share getShare(ShareType shareType){
        return shareMap.get(shareType);
    }

}
