package com.dzm.jcenter.share.dao.wx;

import android.graphics.Bitmap;
import android.os.Parcel;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:35
 * 微信音乐分享
 */

public class ShareWXMusicDao extends BaseShareDao{

    /**分享的音乐url*/
    public String music_url;

    /**分享音乐的标题*/
    public String music_title;

    /**分享音乐的描述*/
    public String music_descrip;

    /**分享音乐的 图片*/
    public Bitmap music_bitmap;

    /**分享类型--true 朋友圈，false 好友*/
    public boolean isFrendCircle;

}
