package com.dzm.jcenter.share.dao.wx;

import android.graphics.Bitmap;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:35
 * 微信 图片分享
 */

public class ShareWXImageDao extends BaseShareDao{

    /**要分享额图片*/
    public Bitmap bitmap;

    /**分享类型--true 朋友圈，false 好友*/
    public boolean isFrendCircle;

}
