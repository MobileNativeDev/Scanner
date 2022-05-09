package com.test.scanner.utils.detector;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.test.scanner.utils.android.BitmapUtils;

import javax.inject.Inject;

public class CameraImageAnalyzer implements ImageAnalysis.Analyzer {

    private final TextRecognizer recognizer = TextRecognition
            .getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    private TextDetectionListener textDetectionListener;
    public Bitmap lastProcessedBitmap;
    private Bounds bounds;

    @Inject
    public CameraImageAnalyzer() {
    }

    private float ratio = 1;

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        @SuppressLint("UnsafeOptInUsageError") Image mediaImage = imageProxy.getImage();
        if (mediaImage != null && bounds != null) {
            Bitmap bitmap = BitmapUtils.toBitmap(mediaImage);
            Matrix matrix = new Matrix();
            int rotation = imageProxy.getImageInfo().getRotationDegrees();
            matrix.postRotate(rotation);
            bitmap = BitmapUtils.adjustRatio(bitmap, ratio);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            float frameWidth = bitmap.getWidth() * bounds.widthPercent;
            float frameHeight = bitmap.getHeight() * bounds.heightPercent;
            int offsetX = (int) (bitmap.getWidth() * bounds.x);
            int offsetY = (int) (bitmap.getHeight() * bounds.y);
            lastProcessedBitmap = Bitmap.createBitmap(bitmap, offsetX, offsetY, (int) frameWidth, (int) frameHeight);
            InputImage image = InputImage.fromBitmap(
                    lastProcessedBitmap,
                    0
            );
            recognizer.process(image).addOnSuccessListener(text -> {
                System.out.println("scan___ processed: " + text);
                if (textDetectionListener != null) {
                    textDetectionListener.onTextDetected(text.getText());
                }
            }).addOnFailureListener(Throwable::printStackTrace)
                    .addOnCompleteListener(task -> imageProxy.close());
        }
    }

    public void setTextDetectionListener(TextDetectionListener textDetectionListener) {
        System.out.println("scan___ set listener");
        this.textDetectionListener = textDetectionListener;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public static class Bounds {

        private final float x;
        private final float y;
        private final float widthPercent;
        private final float heightPercent;

        public Bounds(float x, float y, float widthPercent, float heightPercent) {
            this.x = x;
            this.y = y;
            this.widthPercent = widthPercent;
            this.heightPercent = heightPercent;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidthPercent() {
            return widthPercent;
        }

        public float getHeightPercent() {
            return heightPercent;
        }
    }

    public interface TextDetectionListener {
        void onTextDetected(String text);
    }

}
