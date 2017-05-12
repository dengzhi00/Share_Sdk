package com.dzm.jcenter.share.dao.qq;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/10 16:03
 * 分享音乐
 */

public class ShareQQMusicDao extends BaseShareDao {

    /**必填---标题*/
    public String title;

    /**必填---摘要*/
    public String summary;

    /**必填--这条分享消息被好友点击后的跳转URL*/
    public String target_url;

    /**必填--音乐文件的远程链接*/
    public String audio_url;

    /**是否分享到qq空间*/
    public boolean isToQozen;

    /**分享图片的URL*/

    public String image_url;

    /**分享图片的本地路径*/
    public String local_image_url;

    /**手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替*/
    public String app_name;

}
