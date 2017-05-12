package com.dzm.jcenter.share.dao.qq;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/10 15:24
 * 图文分享
 */
public class ShareQQTextImageDao extends BaseShareDao {

    /**必填--分享消息被好友点击后的跳转URL*/
    public String target_url;

    /**分享图片的URL*/
    public String image_url;

    /**分享图片的本地路径*/
    public String local_image_url;

    /**手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替*/
    public String app_name;

    /**必填--标题*/
    public String title;

    /**摘要*/
    public String summary;

    /**是否分享到qq空间*/
    public boolean isToQozen;

}
