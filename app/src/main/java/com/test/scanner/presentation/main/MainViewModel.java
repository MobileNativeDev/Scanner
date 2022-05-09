package com.test.scanner.presentation.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.scanner.navigation.Command;
import com.test.scanner.navigation.Router;
import com.test.scanner.presentation.scan.ScanScreen;
import com.test.scanner.utils.android.Clipboard;
import com.test.scanner.utils.android.Permissions;
import com.test.scanner.utils.android.Strings;
import com.test.scanner.utils.android.Toaster;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final Router router;
    private final Permissions permissions;
    private final Clipboard clipboard;
    private final Toaster toaster;
    private final Strings strings;
    private final MutableLiveData<String> result = new MutableLiveData<>();

    @Inject
    public MainViewModel(
            Router router,
            Permissions permissions,
            Clipboard clipboard,
            Toaster toaster,
            Strings strings
    ) {
        this.router = router;
        this.permissions = permissions;
        this.clipboard = clipboard;
        this.toaster = toaster;
        this.strings = strings;
    }

    public LiveData<String> getResult() {
        return result;
    }

    public void openScanScreen() {
        if (permissions.arePermissionsGranted()) {
            router.execute(
                    new Command.RegisterResultListener<ScanResult>(
                            ScanResult.REQUEST_SCAN,
                            result -> this.result.setValue(result.getData())
                    ),
                    new Command.Navigate(new ScanScreen())
            );
        } else {
            permissions.requestPermissions();
        }
    }

    public void copy() {
        clipboard.copy(result.getValue());
        toaster.toast(strings.getToastMessage());
    }

}
