package com.test.scanner.utils.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class BitmapUtils {

    public static Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public static Bitmap adjustRatio(Bitmap bitmap, float ratio) {
        float bitmapRatio = bitmap.getHeight() / (float) bitmap.getWidth();
        if (bitmapRatio == ratio) {
            return bitmap;
        }
        int offsetX = 0;
        int offsetY = 0;
        int adjustedWidth = bitmap.getWidth();
        int adjustedHeight = bitmap.getHeight();
        if (ratio > bitmapRatio) {
            adjustedHeight = (int) (adjustedWidth / ratio);
            offsetY = (int) ((bitmap.getHeight() - adjustedHeight) / 2f);
        } else {
            adjustedWidth = (int) (adjustedHeight * ratio);
            offsetX = (int) ((bitmap.getWidth() - adjustedWidth) / 2f);
        }
        return Bitmap.createBitmap(bitmap, offsetX, offsetY, adjustedWidth, adjustedHeight);
    }


}
