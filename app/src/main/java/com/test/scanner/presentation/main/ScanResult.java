package com.test.scanner.presentation.main;

import android.os.Parcel;
import android.os.Parcelable;

public class ScanResult implements Parcelable {

    public static final String REQUEST_SCAN = "scan";
    private final String data;

    public ScanResult(String data) {
        this.data = data;
    }

    protected ScanResult(Parcel in) {
        data = in.readString();
    }

    public static final Creator<ScanResult> CREATOR = new Creator<ScanResult>() {
        @Override
        public ScanResult createFromParcel(Parcel in) {
            return new ScanResult(in);
        }

        @Override
        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(data);
    }

    public String getData() {
        return data;
    }

}
