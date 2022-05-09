package com.test.scanner.navigation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public abstract class Screen {

    public abstract Fragment getFragment();
    public abstract String getTag();

    public Bundle getArguments() {
        return null;
    }

    public boolean isAddToBackStack() {
        return true;
    }

    public TransactionMode getTransactionMode() {
        return TransactionMode.REPLACE;
    }

}
