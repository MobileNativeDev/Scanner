package com.test.scanner.utils.android;

import android.content.Context;

import androidx.annotation.StringRes;

import com.test.scanner.R;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class AndroidStrings implements Strings {

    private final Context context;

    @Inject
    public AndroidStrings(@ApplicationContext Context context) {
        this.context = context;
    }

    private String getString(@StringRes int stringId) {
        return context.getString(stringId);
    }

    @Override
    public String getToastMessage() {
        return getString(R.string.message_copy);
    }

}
