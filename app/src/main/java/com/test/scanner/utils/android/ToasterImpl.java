package com.test.scanner.utils.android;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ToasterImpl implements Toaster {

    private final Context context;
    private Toast toast;

    @Inject
    public ToasterImpl(@ApplicationContext Context context) {
        this.context = context;
    }


    @Override
    public void toast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
