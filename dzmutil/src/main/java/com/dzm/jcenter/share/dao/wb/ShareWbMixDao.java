package com.dzm.jcenter.share.dao.wb;

import android.os.Parcel;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/12 10:42
 * 微博混合分享 图片，文本，路径
 */
public class ShareWbMixDao extends BaseShareDao{

    /**分享消息文本*/
    public String share_text;

    /**分享图片*/
    public int share_bitmap_resource;

    /**多媒体标题*/
    public String share_media_title;

    /**多媒体路径*/
    public String share_media_actionurl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.share_text);
        dest.writeInt(this.share_bitmap_resource);
        dest.writeString(this.share_media_title);
        dest.writeString(this.share_media_actionurl);
    }

    public ShareWbMixDao() {
    }

    protected ShareWbMixDao(Parcel in) {
        super(in);
        this.share_text = in.readString();
        this.share_bitmap_resource = in.readInt();
        this.share_media_title = in.readString();
        this.share_media_actionurl = in.readString();
    }

    public static final Creator<ShareWbMixDao> CREATOR = new Creator<ShareWbMixDao>() {
        @Override
        public ShareWbMixDao createFromParcel(Parcel source) {
            return new ShareWbMixDao(source);
        }

        @Override
        public ShareWbMixDao[] newArray(int size) {
            return new ShareWbMixDao[size];
        }
    };
}
