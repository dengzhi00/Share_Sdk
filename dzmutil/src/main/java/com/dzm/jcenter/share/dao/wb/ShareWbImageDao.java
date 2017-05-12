package com.dzm.jcenter.share.dao.wb;

import android.os.Parcel;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:33
 * 微博图片分享
 */

public class ShareWbImageDao extends BaseShareDao{

    /**要分享的图片*/
    public int share_bitmap_resource;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.share_bitmap_resource);
    }

    public ShareWbImageDao() {
    }

    protected ShareWbImageDao(Parcel in) {
        super(in);
        this.share_bitmap_resource = in.readInt();
    }

    public static final Creator<ShareWbImageDao> CREATOR = new Creator<ShareWbImageDao>() {
        @Override
        public ShareWbImageDao createFromParcel(Parcel source) {
            return new ShareWbImageDao(source);
        }

        @Override
        public ShareWbImageDao[] newArray(int size) {
            return new ShareWbImageDao[size];
        }
    };
}
