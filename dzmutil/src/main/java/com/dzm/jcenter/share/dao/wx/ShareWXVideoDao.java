package com.dzm.jcenter.share.dao.wx;

import android.graphics.Bitmap;
import android.os.Parcel;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:36
 * 微信 视频分享
 */

public class ShareWXVideoDao extends BaseShareDao{

    /**视频url地址*/
    public String video_url;

    /**视屏标题*/
    public String video_title;

    /**视频描述*/
    public String video_descrip;

    /**视频图片*/
    public Bitmap video_bitmap;

    /**分享类型--true 朋友圈，false 好友*/
    public boolean isFrendCircle;

}
