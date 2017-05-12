package com.dzm.jcenter.share.share.wx;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dzm.jcenter.share.ShareApi;
import com.dzm.jcenter.share.config.ShareConfig;
import com.dzm.jcenter.share.config.ShareType;
import com.dzm.jcenter.share.config.Weixin;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:40
 * 微信分享回调
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private ShareWx shareWx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareWx = (ShareWx) ShareApi.getInstance(this).getShare(ShareType.WEIXIN);
        shareWx.onCreate(this, (Weixin) ShareConfig.getShareForm(ShareType.WEIXIN));
        shareWx.handleIntent(getIntent(),this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        shareWx = (ShareWx) ShareApi.getInstance(this).getShare(ShareType.WEIXIN);
        shareWx.onCreate(this, (Weixin) ShareConfig.getShareForm(ShareType.WEIXIN));
        shareWx.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq req) {
        shareWx.getIwxapiEventHandler().onReq(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        shareWx.getIwxapiEventHandler().onResp(resp);
    }


}