package com.dzm.jcenter.share.dao.wx;

import android.graphics.Bitmap;
import android.os.Parcel;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:36
 * 微信 网页分享
 */

public class ShareWXWebDao extends BaseShareDao{

    /**网页路径*/
    public String web_url;

    /**网页标题*/
    public String web_title;

    /**网页描述*/
    public String web_descrip;

    /**网页图片*/
    public Bitmap web_bitmap;

    /**分享类型--true 朋友圈，false 好友*/
    public boolean isFrendCircle;

}
