package com.dzm.jcenter.share.dao.qq;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/10 15:25
 * 分享纯图
 */

public class ShareQQImageDao extends BaseShareDao {

    /**标题*/
    public String title;

    /**摘要*/
    public String summary;

    /**必填---分享本地图片了路径*/
    public String local_image_url;

    /**手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替*/
    public String app_name;

    /**是否分享到qq空间*/
    public boolean isToQozen;

}
