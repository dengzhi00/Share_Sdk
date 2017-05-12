package com.dzm.jcenter.share.config;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:37
 * 微博 数据
 */

public class WeiBo extends ShareForm{

    private String scope;

    private String redirectUrl;

    WeiBo(ShareType shareType){
       super(shareType);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}