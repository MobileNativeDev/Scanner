package com.test.scanner.presentation.scan;

import androidx.fragment.app.Fragment;

import com.test.scanner.navigation.Screen;
import com.test.scanner.navigation.TransactionMode;

public class ScanScreen extends Screen {

    @Override
    public Fragment getFragment() {
        return new ScanFragment();
    }

    @Override
    public String getTag() {
        return ScanFragment.class.getCanonicalName();
    }

//    @Override
//    public TransactionMode getTransactionMode() {
//        return TransactionMode.ADD;
//    }
}
