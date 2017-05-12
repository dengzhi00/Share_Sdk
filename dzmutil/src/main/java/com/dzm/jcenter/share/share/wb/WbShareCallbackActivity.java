package com.dzm.jcenter.share.share.wb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.dzm.jcenter.R;
import com.dzm.jcenter.share.ShareApi;
import com.dzm.jcenter.share.config.ShareConfig;
import com.dzm.jcenter.share.config.ShareType;
import com.dzm.jcenter.share.config.WeiBo;
import com.dzm.jcenter.share.dao.BaseShareDao;
import com.sina.weibo.sdk.share.WbShareCallback;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:23
 * 微博分享发起结束activity
 */
public class WbShareCallbackActivity extends Activity implements WbShareCallback {

    private ShareWb shareWb;

    public static final String DZM_DATA = "dzm_data_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareWb = (ShareWb) ShareApi.getInstance(this).getShare(ShareType.WEIBO);
        shareWb.onCreate(this, (WeiBo) ShareConfig.getShareForm(ShareType.WEIBO));
        if(null != getIntent()){
            Bundle bundle = getIntent().getExtras();
            if(null != bundle){
                if(bundle.containsKey(DZM_DATA)){
                    BaseShareDao shareDao = bundle.getParcelable(DZM_DATA);
                    shareWb.share(shareDao,null);
                }else if(bundle.containsKey("_weibo_resp_errcode")){
                    shareWb.onNewIntent(getIntent(),this);
                }else{
                    finish();
                }
            }else{
                finish();
            }
        }else{
            finish();
        }

        Log.d("onCreate","***************************");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareWb = (ShareWb) ShareApi.getInstance(this).getShare(ShareType.WEIBO);
        shareWb.onCreate(this, (WeiBo) ShareConfig.getShareForm(ShareType.WEIBO));
        shareWb.onNewIntent(intent,this);
        Log.d("onNewIntent","***************************");
    }

    @Override
    public void onWbShareSuccess() {
        shareWb.onWbShareSuccess();
        finish();
    }

    @Override
    public void onWbShareCancel() {
        shareWb.onWbShareCancel();
        finish();
    }

    @Override
    public void onWbShareFail() {
        shareWb.onWbShareFail();
        finish();
    }
}
