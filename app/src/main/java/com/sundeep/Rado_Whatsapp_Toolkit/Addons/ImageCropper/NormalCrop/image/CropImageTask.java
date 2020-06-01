package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.config.CropIwaSaveConfig;
import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.shape.CropIwaShapeMask;
import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.util.CropIwaUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Yaroslav Polyakov on 22.03.2017.
 * https://github.com/polyak01
 */

class CropImageTask extends AsyncTask<Void, Void, Throwable> {

    private Context context;
    private CropArea cropArea;
    private CropIwaShapeMask mask;
    private Uri srcUri;
    private CropIwaSaveConfig saveConfig;
    private int rotateAngle;
    private boolean flipHorizontal,flipVertical;

    public CropImageTask(
            Context context, CropArea cropArea, CropIwaShapeMask mask,
            Uri srcUri, CropIwaSaveConfig saveConfig,int rotateAngle,Boolean flipHorizontal,Boolean flipVertical) {
        this.context = context;
        this.cropArea = cropArea;
        this.mask = mask;
        this.srcUri = srcUri;
        this.saveConfig = saveConfig;
        this.rotateAngle=rotateAngle;
        this.flipHorizontal=flipHorizontal;
        this.flipVertical=flipVertical;
    }

    @Override
    protected Throwable doInBackground(Void... params) {
        try {
            Bitmap bitmap_temp = CropIwaBitmapManager.get().loadToMemory(
                    context, srcUri, saveConfig.getWidth(),
                    saveConfig.getHeight());

            if (bitmap_temp == null) {
                return new NullPointerException("Failed to load bitmap");
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(rotateAngle);
            Bitmap bitmap=Bitmap.createBitmap(bitmap_temp, 0, 0, bitmap_temp.getWidth(), bitmap_temp.getHeight(), matrix, true);



            Log.d("FlipHorizontal",flipHorizontal+"");

            if(flipHorizontal){
                Matrix matrix1 = new Matrix();
                matrix1.preScale(-1.0f, 1.0f);
                bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);
            }

            if(flipVertical){
                Matrix matrix1 = new Matrix();
                matrix1.preScale(1.0f, -1.0f);
                bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);
            }

            Bitmap cropped = cropArea.applyCropTo(bitmap);

            cropped = mask.applyMaskTo(cropped);

            Uri dst = saveConfig.getDstUri();
            OutputStream os = context.getContentResolver().openOutputStream(dst);
            cropped.compress(saveConfig.getCompressFormat(), saveConfig.getQuality(), os);
            CropIwaUtils.closeSilently(os);

            bitmap.recycle();
            cropped.recycle();
        } catch (IOException e) {
            return e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Throwable throwable) {
        if (throwable == null) {
            CropIwaResultReceiver.onCropCompleted(context, saveConfig.getDstUri());
        } else {
            CropIwaResultReceiver.onCropFailed(context, throwable);
        }
    }
}