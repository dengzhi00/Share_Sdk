package com.dzm.jcenter.share.dao.wb;

import android.os.Parcel;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:33
 * 微博 网络 内容分享
 */

public class ShareWbMediaDao extends BaseShareDao{

    /**分享标题*/
    public String share_title;

    /**分享 描述*/
    public String share_descrip;

    /**分享路径*/
    public String share_actionurl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.share_title);
        dest.writeString(this.share_descrip);
        dest.writeString(this.share_actionurl);
    }

    public ShareWbMediaDao() {
    }

    protected ShareWbMediaDao(Parcel in) {
        super(in);
        this.share_title = in.readString();
        this.share_descrip = in.readString();
        this.share_actionurl = in.readString();
    }

    public static final Creator<ShareWbMediaDao> CREATOR = new Creator<ShareWbMediaDao>() {
        @Override
        public ShareWbMediaDao createFromParcel(Parcel source) {
            return new ShareWbMediaDao(source);
        }

        @Override
        public ShareWbMediaDao[] newArray(int size) {
            return new ShareWbMediaDao[size];
        }
    };
}
