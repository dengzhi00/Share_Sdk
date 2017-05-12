package com.dzm.jcenter.share.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dzm.jcenter.share.config.QQ;
import com.dzm.jcenter.share.config.ShareForm;
import com.dzm.jcenter.share.dao.BaseShareDao;
import com.dzm.jcenter.share.listener.ShareListener;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:40
 * 分享基类
 */

public abstract class Share<T extends ShareForm> {

    /** 上下文 */
    protected Activity activity;

    /** 分享类型 */
    protected T sharForm;

    public Share(Activity activity,T sharForm){
        this.activity = activity;
        this.sharForm = sharForm;
        onCreate(activity,sharForm);
    }

    /**
     * 分享
     * @param shareDao 分享类型
     * @param shareListener 分享事件
     */
    public abstract void share(BaseShareDao shareDao, ShareListener shareListener);

    /**
     * 初始化参数
     * @param activity activity
     * @param sharForm sharForm
     */
    public abstract void onCreate(Activity activity,T sharForm);

    /**
     * onActivityResult
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    /***
     * 弹出toast
     * @param msg 消息
     */
    protected void toast(String msg){
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }

    public void setShareListener(ShareListener shareListener){

    }
}
