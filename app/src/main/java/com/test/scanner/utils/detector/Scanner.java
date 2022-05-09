package com.test.scanner.utils.detector;

import android.content.Context;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.test.scanner.data.Scan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class Scanner {

    private final CameraImageAnalyzer analyzer;
    private final ImageAnalysis imageAnalysis;
    private final Context context;
    private LifecycleOwner lifecycleOwner;
    private ProcessCameraProvider provider;
    private PreviewView previewCamera;
    private final Map<String, Integer> scans = new HashMap<>();
    private final Comparator<Scan> comparator = (scan, t1) -> scan.compareTo(t1);
    private List<Scan> scanList;
    private boolean isScanning = false;
    private ScanListener listener;

    @Inject
    public Scanner(CameraImageAnalyzer analyzer, @ApplicationContext Context context) {
        this.analyzer = analyzer;
        System.out.println("scan___ analyzer: " + this.analyzer);
        imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), analyzer);
        this.context = context;
    }

    public Binder getBinder() {
        return new Binder();
    }

    public void startCamera() {
        ListenableFuture<ProcessCameraProvider> providerFuture = ProcessCameraProvider
                .getInstance(context);
        providerFuture.addListener(() -> {
            try {
                provider = providerFuture.get();
                float ratio = previewCamera.getHeight() / (float) previewCamera.getWidth();
                analyzer.setRatio(ratio);
                Preview preview = new Preview.Builder()
                        .build();
                preview.setSurfaceProvider(previewCamera.getSurfaceProvider());
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                provider.unbindAll();
                UseCaseGroup useCase = new UseCaseGroup.Builder()
                        .addUseCase(preview)
                        .build();
                provider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        useCase
                );
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(context));
        setupDetectionListener();
    }

    private void setupDetectionListener() {
        analyzer.setTextDetectionListener(text -> {
            System.out.println("scan___ text scanner: " + text);
            String result = adjustScanResult(text);
            if (result.length() > 0) {
                Integer resultProbability = scans.get(result);
                if (resultProbability == null) {
                    resultProbability = 1;
                } else {
                    resultProbability++;
                }
                scans.put(result, resultProbability);
                scanList = new ArrayList<>(scans.size());
                for (Map.Entry<String, Integer> e : scans.entrySet()) {
                    scanList.add(new Scan(e.getKey(), e.getValue()));
                }
                Collections.sort(scanList, comparator);
                if (listener != null) {
                    listener.onNewScan(getMostProbableResult());
                }
            }
        });
    }

    private String adjustScanResult(String text) {
        return text.replaceAll("\\D", "");
    }

    public void setScanListener(ScanListener listener) {
        this.listener = listener;
    }

    public void startScan() {
        System.out.println("scan___ start scan");
        scans.clear();
        scanList = new ArrayList<>();
        provider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                imageAnalysis
        );
        isScanning = true;
    }

    public void stopScan() {
        System.out.println("scan___ stop scan");
        if (provider.isBound(imageAnalysis)) {
            provider.unbind(imageAnalysis);
        }
        isScanning = false;
    }

    public boolean isScanning() {
        return isScanning;
    }

    public Scan getMostProbableResult() {
        return scanList.get(0);
    }

    public class Binder {

        private Binder() {
        }

        public void bindTo(LifecycleOwner lifecycleOwner, PreviewView previewCamera) {
            Scanner.this.lifecycleOwner = lifecycleOwner;
            Scanner.this.previewCamera = previewCamera;
        }

        public CameraImageAnalyzer getAnalyzer() {
            return Scanner.this.analyzer;
        }

    }

    public interface ScanListener {
        void onNewScan(Scan scan);
    }

}
