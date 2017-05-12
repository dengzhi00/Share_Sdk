package com.dzm.jcenter.share.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author 邓治民
 * date 2017/5/12 16:36分享内容基类
 */
public class BaseShareDao implements Parcelable{


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public BaseShareDao() {
    }

    protected BaseShareDao(Parcel in) {
    }

    public static final Creator<BaseShareDao> CREATOR = new Creator<BaseShareDao>() {
        @Override
        public BaseShareDao createFromParcel(Parcel source) {
            return new BaseShareDao(source);
        }

        @Override
        public BaseShareDao[] newArray(int size) {
            return new BaseShareDao[size];
        }
    };
}
