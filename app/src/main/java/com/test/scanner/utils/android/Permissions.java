package com.test.scanner.utils.android;

import android.app.Activity;

public interface Permissions {

    void attach(Activity activity);
    boolean arePermissionsGranted();
    void requestPermissions();

}