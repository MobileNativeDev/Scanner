package com.test.scanner.presentation.scan;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.scanner.R;
import com.test.scanner.base.BaseFragment;
import com.test.scanner.databinding.FragmentScanBinding;
import com.test.scanner.utils.detector.CameraImageAnalyzer;
import com.test.scanner.utils.detector.Scanner;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanFragment extends BaseFragment<ScanViewModel, FragmentScanBinding> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startCamera();
        getBinding().buttonScan.setOnClickListener(button -> getViewModel().onScanClick());
        getBinding().buttonFinish.setOnClickListener(
                button -> getViewModel().navigateBackWithResult());
        getViewModel().getScan().observe(getViewLifecycleOwner(),
                result -> getBinding().textResult.setText(result));
        getViewModel().isScanning().observe(getViewLifecycleOwner(), isScanning -> {
            int scanTextRes;
            if (isScanning) {
                scanTextRes = R.string.action_stop;
            } else {
                scanTextRes = R.string.action_start;
            }
            getBinding().buttonScan.setText(scanTextRes);
        });
    }

    private void startCamera() {
        Scanner.Binder binder = getViewModel().getScannerBinder();
        binder.bindTo(getViewLifecycleOwner(), getBinding().previewCamera);
        CameraImageAnalyzer analyzer = binder.getAnalyzer();
        getBinding().getRoot().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(
                    View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7
            ) {
                setCropperBoundsTo(analyzer);
                getBinding().cropper.setBoundsUpdatedListener(
                        bounds -> setCropperBoundsTo(analyzer));
                getViewModel().startPreview();
                getBinding().getRoot().removeOnLayoutChangeListener(this);
            }
        });

    }

    private void setCropperBoundsTo(CameraImageAnalyzer analyzer) {
        View cropper = getBinding().cropper;
        View previewCamera = getBinding().previewCamera;
        float x = (cropper.getX() - previewCamera.getX()) / (float) previewCamera.getWidth();
        float y = (cropper.getY() - previewCamera.getY()) / (float) previewCamera.getHeight();
        float widthPercent = cropper.getWidth() / (float) previewCamera.getWidth();
        float heightPercent = cropper.getHeight() / (float) previewCamera.getHeight();
        CameraImageAnalyzer.Bounds bounds = new CameraImageAnalyzer.Bounds(
                x, y, widthPercent, heightPercent
        );
        analyzer.setBounds(bounds);
    }

}
