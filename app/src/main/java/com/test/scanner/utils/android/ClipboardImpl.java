package com.test.scanner.utils.android;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.test.scanner.R;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ClipboardImpl implements Clipboard {

    private final Context context;
    private final ClipboardManager clipboardManager;

    @Inject
    public ClipboardImpl(@ApplicationContext Context context) {
        this.context = context;
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void copy(String text) {
        ClipData clip = ClipData.newPlainText(context.getString(R.string.label_copy), text);
        clipboardManager.setPrimaryClip(clip);
    }
}
