package com.test.scanner.presentation.scan;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.scanner.data.Scan;
import com.test.scanner.navigation.Command;
import com.test.scanner.navigation.Router;
import com.test.scanner.presentation.main.ScanResult;
import com.test.scanner.utils.detector.Scanner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScanViewModel extends ViewModel {

    private final Router router;
    private final Scanner scanner;
    private final MutableLiveData<Boolean> isScanning = new MutableLiveData<>();
    private final MutableLiveData<String> scan = new MutableLiveData<>();

    @Inject
    public ScanViewModel(Router router, Scanner scanner) {
        this.router = router;
        this.scanner = scanner;
        scanner.setScanListener(scan -> {
            System.out.println("scan___ scanned: " + scan.getValue());
            this.scan.setValue(scan.getValue());
        });
    }

    public LiveData<String> getScan() {
        return scan;
    }

    public LiveData<Boolean> isScanning() {
        return isScanning;
    }

    public Scanner.Binder getScannerBinder() {
        return scanner.getBinder();
    }

    public void onScanClick() {
        if (scanner.isScanning()) {
            stopScan();
        } else {
            startScan();
        }
    }

    public void startPreview() {
        scanner.startCamera();
    }

    private void startScan() {
        scanner.startScan();
        isScanning.setValue(scanner.isScanning());
    }

    private void stopScan() {
        scanner.stopScan();
        isScanning.setValue(scanner.isScanning());
    }

    public void navigateBackWithResult() {
        Scan mostProbable = scanner.getMostProbableResult();
        String result;
        if (mostProbable == null) {
            result = "";
        } else {
            result = mostProbable.getValue();
        }
        router.execute(
                new Command.SetResult<>(ScanResult.REQUEST_SCAN, new ScanResult(result)),
                Command.Back.getInstance()
        );
    }

}
