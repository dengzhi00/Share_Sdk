package com.dzm.jcenter.share.dao.wx;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/10 17:58
 */

public class ShareWXTextDao extends BaseShareDao{

    /**分享的文字*/
    public String message;

    /**分享类型--true 朋友圈，false 好友*/
    public boolean isFrendCircle;

}
