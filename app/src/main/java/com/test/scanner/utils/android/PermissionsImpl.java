package com.test.scanner.utils.android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class PermissionsImpl implements Permissions {

    private final Context context;
    private WeakReference<Activity> currentActivity;
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private static final int PERMISSIONS_REQUEST_CODE = 1001;

    @Inject
    public PermissionsImpl(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void attach(Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    @Override
    public boolean arePermissionsGranted() {
        for (String permission : PERMISSIONS) {
            boolean granted = ActivityCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void requestPermissions() {
        Activity activity = currentActivity.get();
        ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSIONS_REQUEST_CODE);
    }

}
