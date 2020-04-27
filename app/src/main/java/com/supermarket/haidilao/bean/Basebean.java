package com.supermarket.haidilao.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Basebean implements Parcelable {

    private int code;
    private boolean success;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
    }

    public Basebean() {
    }

    protected Basebean(Parcel in) {
        this.code = in.readInt();
        this.success = in.readByte() != 0;
        this.message = in.readString();
    }

    public static final Parcelable.Creator<Basebean> CREATOR = new Parcelable.Creator<Basebean>() {
        @Override
        public Basebean createFromParcel(Parcel source) {
            return new Basebean(source);
        }

        @Override
        public Basebean[] newArray(int size) {
            return new Basebean[size];
        }
    };
}
