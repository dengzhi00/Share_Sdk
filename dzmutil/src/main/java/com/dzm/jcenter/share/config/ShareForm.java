package com.dzm.jcenter.share.config;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:37
 * 数据基类
 */
public abstract class ShareForm {

    private final ShareType shareType;

    private String appKey = null;

    public ShareForm(ShareType shareType){
        this.shareType = shareType;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public ShareType getShareType() {
        return shareType;
    }
}
