package com.dzm.jcenter.share.listener;

import com.dzm.jcenter.share.config.ShareType;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:39
 *  分享回调
 */

public interface ShareListener {

    void onComplete(ShareType shareType);

    void onError(ShareType shareType,String err_msg);

    void onCancel(ShareType shareType);

}
