package com.test.scanner.presentation.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.test.scanner.navigation.Screen;

public class MainScreen extends Screen {

    @Override
    public Fragment getFragment() {
        return new MainFragment();
    }

    @Override
    public String getTag() {
        return MainFragment.class.getCanonicalName();
    }

    @Override
    public Bundle getArguments() {
        return null;
    }

    @Override
    public boolean isAddToBackStack() {
        return false;
    }

}
