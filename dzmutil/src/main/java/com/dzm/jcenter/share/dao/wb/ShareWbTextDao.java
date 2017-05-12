package com.dzm.jcenter.share.dao.wb;

import android.os.Parcel;

import com.dzm.jcenter.share.dao.BaseShareDao;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:34
 * 微博文本分享
 */

public class ShareWbTextDao extends BaseShareDao{

    /**要分享额文字*/
    public String share_text;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.share_text);
    }

    public ShareWbTextDao() {
    }

    protected ShareWbTextDao(Parcel in) {
        super(in);
        this.share_text = in.readString();
    }

    public static final Creator<ShareWbTextDao> CREATOR = new Creator<ShareWbTextDao>() {
        @Override
        public ShareWbTextDao createFromParcel(Parcel source) {
            return new ShareWbTextDao(source);
        }

        @Override
        public ShareWbTextDao[] newArray(int size) {
            return new ShareWbTextDao[size];
        }
    };
}
